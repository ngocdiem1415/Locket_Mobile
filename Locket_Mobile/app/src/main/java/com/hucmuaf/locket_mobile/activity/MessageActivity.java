package com.hucmuaf.locket_mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.service.ApiClient;
import com.hucmuaf.locket_mobile.model.Message;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hucmuaf.locket_mobile.service.MessageListAPIService;

public class MessageActivity extends AppCompatActivity {
    //Service to call API from backend (get all messages of userid)
    private MessageListAPIService apiService;
    private List<Message> messageList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentUserId;
    private ImageView avt;
    private TextView username;
//    private TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        //Initialize firebase auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //Initialize api service
        apiService = ApiClient.getMessageListApiService();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        avt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });
        if (currentUser != null && currentUser.getUid() != null) {
//            loadMessages(currentUser.getUid());
            loadUserProfile(currentUser.getUid());
//            loadMessages(currentUser.getUid());
        } else {
            initializeAfterUserId();
        }
    }

    private void loadUserProfile(String userId) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("fullName");
                        String avtUrl = documentSnapshot.getString("avatarUrl");
                        if (name != null) {
                            username.setText(name);
                        } else {
                            username.setText("Unkown User");
                        }
                        if (avtUrl != null & !avtUrl.isEmpty()) {
                            Glide.with(MessageActivity.this)
                                    .load(avtUrl)
                                    .circleCrop()
                                    .placeholder(R.drawable.default_avatar)
                                    .into(avt);
                        } else {
                            avt.setImageResource(R.drawable.default_avatar);
                        }
                    } else {
                        Log.w("MessageActivity", "User not found: " + userId);
                        username.setText("Unkown User");
                        avt.setImageResource(R.drawable.default_avatar);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("MessageActivity", "Error getting user profile: " + e.getMessage());
                    username.setText("Unkown User");
                    avt.setImageResource(
                            R.drawable.default_avatar
                    );
                });
    }

//    private void loadMessages(String userId) {
//        Call<List<Message>> call = apiService.getMessageWithUserId(userId);
//        call.enqueue(new Callback<List<Message>>() {
//            @Override
//            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    currentUserId = response.body().toString();
//                    Log.d("MessageActivity", "Successfully got User ID: " + currentUserId);
//                    initializeAfterUserId();
//                } else {
//                    currentUserId = "";
//                    Log.w("MessageActivity", "User not found, using fallback: " + currentUserId);
//                    initializeAfterUserId();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Message>> call, Throwable t) {
//                Log.e("MessageActivity", "API call failed: " + t.getMessage());
//                currentUserId = "";
//                initializeAfterUserId();
//            }
//        });
//    }

    private void initializeAfterUserId() {
        initializeViews();
//        loadMessages(currentUserId);
    }

    private void initializeViews() {
        avt = findViewById(R.id.account2);
        username = findViewById(R.id.textView2);


    }
}
