package com.hucmuaf.locket_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.adapter.ChatMessageAdapter;
import com.hucmuaf.locket_mobile.model.Message;
import com.hucmuaf.locket_mobile.model.MessageType;
import com.hucmuaf.locket_mobile.service.MessageListAPIService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private MessageListAPIService apiService;
    private ImageView avt;
    private TextView name;
    private TextView timestamp;
    private EditText editTextMessage;
    private ImageView sendButton;
    private RecyclerView recyclerViewMessages;
    private ChatMessageAdapter messageAdapter;
    private List<Message> messageList;
    private String currentUserId;
    private String otherUserId;
    private ActivityResultLauncher<Intent> pickImageLauncher;
    private WebSocket webSocket;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Initialize views
        avt = findViewById(R.id.account6);
        name = findViewById(R.id.textView16);
        timestamp = findViewById(R.id.textView14);
        editTextMessage = findViewById(R.id.editTextText);
        sendButton = findViewById(R.id.account15);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);

        // Initialize RecyclerView
        messageList = new ArrayList<>();
        messageAdapter = new ChatMessageAdapter(messageList, currentUserId);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messageAdapter);

        // Get current user ID
        if (currentUser != null && currentUser.getUid() != null) {
            currentUserId = currentUser.getUid();
            otherUserId = getIntent().getStringExtra("otherUserId");
            if (otherUserId == null) {
                otherUserId = "";
                Log.w("ChatActivity", "otherUserId not provided");
            }
            loadInitialMessages();
            connectWebSocket();
        } else {
            currentUserId = "";
            Log.w("ChatActivity", "No user logged in");
        }

        // Set up image picker
//        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        Uri imageUri = result.getData().getData();
//                        sendImageMessage(imageUri);
//                    }
//                });

        // Handle send button click
        sendButton.setOnClickListener(v -> {
            String messageContent = editTextMessage.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessage(messageContent);
                editTextMessage.setText("");
            }
        });

        // Handle image selection (long press)
        editTextMessage.setOnLongClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
            return true;
        });
    }

    private void loadInitialMessages() {
        // Load initial messages from REST API
        // Note: Adjust the API call based on your backend endpoint
        // For now, using WebSocket for real-time, initial load can be from REST
        // Example: Call apiService.getMessageWithUserId(otherUserId) if available
    }

    private void connectWebSocket() {
        client = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("ws://http://localhost:8080/ws")
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Log.d("WebSocket", "Connection opened");
                // Register user
                Message registerMessage = new Message();
                registerMessage.setSenderId(currentUserId);
                registerMessage.setType(String.valueOf(MessageType.JOIN));
                sendWebSocketMessage(registerMessage);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                runOnUiThread(() -> {
                    try {
                        JSONObject jsonObject = new JSONObject(text);
                        Message message = new Message();
                        message.setSenderId(jsonObject.getString("senderId"));
                        message.setReceiverId(jsonObject.getString("receiverId"));
                        message.setContent(jsonObject.optString("content", null));
//                        message.setImageUrl(jsonObject.optString("imageUrl", null));
//                        message.setCaption(jsonObject.optString("caption", null));
                        message.setTimestamp(jsonObject.getLong("timestamp"));
                        message.setType(String.valueOf(MessageType.valueOf(jsonObject.getString("type"))));

                        if (!message.getSenderId().equals(currentUserId) || message.getType().equals(MessageType.JOIN)) {
                            messageList.add(message);
                            messageAdapter.notifyItemInserted(messageList.size() - 1);
                            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                super.onClosing(webSocket, code, reason);
                Log.d("WebSocket", "Closing: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                super.onFailure(webSocket, t, response);
                Log.e("WebSocket", "Error: " + t.getMessage());
            }
        });
    }

    private void sendWebSocketMessage(Message message) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("senderId", message.getSenderId());
            jsonObject.put("receiverId", message.getReceiverId());
            jsonObject.put("content", message.getContent());
//            jsonObject.put("imageUrl", message.getImageUrl());
//            jsonObject.put("caption", message.getCaption());
            jsonObject.put("timestamp", message.getTimestamp());
            jsonObject.put("type", message.getType().toString());

            if (webSocket != null) {
                webSocket.send(jsonObject.toString());
                Log.d("WebSocket", "Sent: " + jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String content) {
        Message newMessage = new Message(currentUserId, otherUserId, content, System.currentTimeMillis(), "JOIN");
        newMessage.setType(String.valueOf(MessageType.CHAT));
        sendWebSocketMessage(newMessage);
        messageList.add(newMessage);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewMessages.scrollToPosition(messageList.size() - 1);
    }

//    private void sendImageMessage(Uri imageUri) {
//        // Upload image to server and get URL (example using Firebase Storage or your backend)
//        String imageUrl = "http://example.com/image.jpg"; // Replace with actual upload logic
//        String caption = editTextMessage.getText().toString().trim();
//        Message newMessage = new Message(currentUserId, otherUserId, null, imageUrl, caption, System.currentTimeMillis());
//        newMessage.setType(Message.MessageType.CHAT);
//        sendWebSocketMessage(newMessage);
//        messageList.add(newMessage);
//        messageAdapter.notifyItemInserted(messageList.size() - 1);
//        recyclerViewMessages.scrollToPosition(messageList.size() - 1);
//        editTextMessage.setText("");
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webSocket != null) {
            webSocket.close(1000, "Activity destroyed");
            client.dispatcher().executorService().shutdown();
        }
    }
}