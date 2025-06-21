//package com.hucmuaf.locket_mobile.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.hucmuaf.locket_mobile.R;
//import com.hucmuaf.locket_mobile.adapter.ChatMessageAdapter;
//import com.hucmuaf.locket_mobile.model.Message;
//import com.hucmuaf.locket_mobile.service.ApiClient;
//import com.hucmuaf.locket_mobile.service.MessageListAPIService;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MessageActivity extends AppCompatActivity {
//    private MessageListAPIService apiService;
//    private List<Message> messageList = new ArrayList<>();
//    private FirebaseAuth mAuth;
//    private FirebaseFirestore db;
//    private String currentUserId;
//    private ImageView backButton;
//    private ImageView avt;
//    private TextView username;
//    private RecyclerView recyclerViewMessages;
//    private ChatMessageAdapter messageAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.item_message_list);
//
//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
//        apiService = ApiClient.getMessageListApiService();
//
//        initializeViews();
//
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null && currentUser.getUid() != null) {
//            currentUserId = currentUser.getUid();
//            loadUserProfile(currentUserId);
//            loadMessages(currentUserId);
//        } else {
//            Log.w("MessageActivity", "No user logged in");
//            username.setText("Unknown User");
//            avt.setImageResource(R.drawable.default_avatar);
//        }
//
//        backButton.setOnClickListener(v -> onBackPressed());
//        if (avt != null) {
//            avt.setOnClickListener(v -> {
//                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
//                startActivity(intent);
//            });
//        }
//        if (username != null) {
//            username.setOnClickListener(v -> {
//                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
//                startActivity(intent);
//            });
//        }
//    }
//
//    private void initializeViews() {
//        backButton = findViewById(R.id.backButton);
//        avt = findViewById(R.id.messageAvatar);
//        username = findViewById(R.id.username);
//        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
//
//        // Set up RecyclerView
//        messageAdapter = new ChatMessageAdapter(messageList, currentUserId);
//        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
//        recyclerViewMessages.setAdapter(messageAdapter);
//        Log.w("Message Activity", "Can not initialize views");
//    }
//
//    private void loadUserProfile(String userId) {
//        db.collection("users").document(userId).get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    if (documentSnapshot.exists()) {
//                        String name = documentSnapshot.getString("fullName");
//                        String avtUrl = documentSnapshot.getString("urlAvatar");
//                        if (name != null) {
//                            username.setText(name);
//                        } else {
//                            username.setText("Unknown User");
//                        }
//                        if (avtUrl != null && !avtUrl.isEmpty()) {
//                            Glide.with(MessageActivity.this)
//                                    .load(avtUrl)
//                                    .circleCrop()
//                                    .placeholder(R.drawable.default_avatar)
//                                    .into(avt);
//                        } else {
//                            avt.setImageResource(R.drawable.default_avatar);
//                        }
//                    } else {
//                        Log.w("MessageActivity", "User not found: " + userId);
//                        username.setText("Unknown User");
//                        avt.setImageResource(R.drawable.default_avatar);
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("MessageActivity", "Error getting user profile: " + e.getMessage());
//                    username.setText("Unknown User");
//                    avt.setImageResource(R.drawable.default_avatar);
//                });
//    }
//
//    private void loadMessages(String userId) {
//        apiService.getMessageWithUserId(userId).enqueue(new retrofit2.Callback<List<Message>>() {
//            @Override
//            public void onResponse(retrofit2.Call<List<Message>> call, retrofit2.Response<List<Message>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    messageList.clear();
//                    messageList.addAll(response.body());
//                    messageAdapter.notifyDataSetChanged();
//                } else {
//                    Log.w("MessageActivity", "Failed to load messages: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<List<Message>> call, Throwable t) {
//                Log.e("MessageActivity", "Error loading messages: " + t.getMessage());
//            }
//        });
//    }
//}
package com.hucmuaf.locket_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.adapter.ChatMessageAdapter;
import com.hucmuaf.locket_mobile.model.Message;
import com.hucmuaf.locket_mobile.service.ApiClient;
import com.hucmuaf.locket_mobile.service.MessageListAPIService;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    private MessageListAPIService apiService;
    private List<Message> messageList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private String currentUserId;
    private ImageView backButton;
    private ImageView avt;
    private TextView username;
    private RecyclerView recyclerViewMessages;
    private ChatMessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        apiService = ApiClient.getMessageListApiService();

//        db.setValue("id","JsnmEU9dB0MKXXNzTzWL8gDZWJs1" )
        initializeViews();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        System.out.println("Userrrrrrrrrrrrrrrrrr" + currentUserId);
        if (currentUser != null && currentUser.getUid() != null) {
            currentUserId = currentUser.getUid();
            System.out.println("This is current user id: " + currentUserId);
            Log.w("MessageActivity", "This is currentId  " + currentUserId);
            loadUserProfile(currentUserId);
            loadMessages(currentUserId);
        } else {
            try {
                Log.w("MessageActivity", "No user logged in");
                setDefaultUserInfo();
            } catch (Exception e) {
                Log.w("MessageActivity", "No user logged in");
                throw new RuntimeException(e);
            }

        }

        // Set click listeners
        if (backButton != null) {
            backButton.setOnClickListener(v -> onBackPressed());
        }
        if (avt != null) {
            avt.setOnClickListener(v -> {
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                startActivity(intent);
            });
        }
        if (username != null) {
            username.setOnClickListener(v -> {
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                startActivity(intent);
            });
        }
    }

    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        avt = findViewById(R.id.currentUserAvatar);
        username = findViewById(R.id.currentUsername);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);

        // Set up RecyclerView
        if (recyclerViewMessages != null) {
            messageAdapter = new ChatMessageAdapter(messageList, currentUserId);
            recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewMessages.setAdapter(messageAdapter);
        } else {
            Log.e("MessageActivity", "recyclerViewMessages is null");
        }
        Log.d("MessageActivity", "Views initialized - avt: " + (avt != null) + ", username: " + (username != null));
    }

    private void loadUserProfile(String userId) {
        if (db == null || username == null || avt == null) {
            Log.e("MessageActivity", "Database or views are not initialized");
            setDefaultUserInfo();
            return;
        }

        // Lấy dữ liệu từ node users/<userId>
        db.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("fullName").getValue(String.class);
                    String avtUrl = dataSnapshot.child("urlAvatar").getValue(String.class);

                    if (name != null) {
                        username.setText(name);
                    } else {
                        username.setText("Unknown User");
                    }

                    if (avtUrl != null && !avtUrl.isEmpty()) {
                        Glide.with(MessageActivity.this)
                                .load(avtUrl)
                                .circleCrop()
                                .placeholder(R.drawable.default_avatar)
                                .error(R.drawable.default_avatar)
                                .into(avt);
                    } else {
                        avt.setImageResource(R.drawable.default_avatar);
                    }
                } else {
                    Log.w("MessageActivity", "User not found: " + userId);
                    setDefaultUserInfo();
                }
            }


            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                Log.e("MessageActivity", "Error getting user profile: " + databaseError.getMessage());
                setDefaultUserInfo();
            }
        });
    }

    private void setDefaultUserInfo() {
        if (username != null) {
            username.setText("Unknown User");
        }
        if (avt != null) {
            avt.setImageResource(R.drawable.default_avatar);
        }
    }

    private void loadMessages(String userId) {
        if (apiService == null) {
            Log.e("MessageActivity", "API service is not initialized");
            return;
        }

        apiService.getMessageWithUserId(userId).enqueue(new retrofit2.Callback<List<Message>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Message>> call, retrofit2.Response<List<Message>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    if (messageAdapter != null) {
                        messageAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.w("MessageActivity", "Failed to load messages: " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Message>> call, Throwable t) {
                Log.e("MessageActivity", "Error loading messages: " + t.getMessage());
            }
        });
    }
}