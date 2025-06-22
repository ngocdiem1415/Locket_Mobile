package com.hucmuaf.locket_mobile.repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hucmuaf.locket_mobile.inteface.onFriendLoaded;
import com.hucmuaf.locket_mobile.inteface.onMessageLoaded;
import com.hucmuaf.locket_mobile.model.Message;
import com.hucmuaf.locket_mobile.service.FirebaseService;

import java.util.ArrayList;
import java.util.List;

public class FriendRepository {
    private DatabaseReference db;

    public FriendRepository() {
        this.db = FirebaseService.getInstance().getDatabase().getReference("friendRequests");
    }

    public void getAllFriendWithUserId(String userId, onMessageLoaded callback) {

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> friendIds = new ArrayList<>();
                for (DataSnapshot friendSnap : snapshot.getChildren()) {
                    friendIds.add(friendSnap.getKey());
                }

                Log.d("MessageRepository", "Found friends of " + userId + ": " + friendIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MessageRepository", "Failed to load friends: " + error.getMessage());
                callback.onFailure(error.toException());
            }
        });
    }

    public static void main(String[] args) {
//        FriendRepository friendRepo = new FriendRepository();
//        friendRepo.getAllFriendWithUserId("yourUserId", new onFriendLoaded() {
//            @Override
//            public void onSuccess(List<String> friendIds) {
//                for (String id : friendIds) {
//                    Log.d("TestFriendList", "Friend ID: " + id);
//                }
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e("TestFriendList", "Failed: " + e.getMessage());
//            }
//        });
    }
}
