package com.hucmuaf.locket_mobile;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFriendActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FriendAdapter friendAdapter;
    private List<User> friendsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friend);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        friendAdapter = new FriendAdapter(this, friendsList);
        recyclerView.setAdapter(friendAdapter);

        loadFriendList();
    }

    private void loadFriendList() {
        // Implement the logic to load the friend list from the backend
    }

    private void removeFriend(User user) {
        // Implement the logic to remove a friend from the backend
        // This is a placeholder and should be replaced with the actual implementation
        // using Retrofit or any other method to communicate with the backend

        // This is a placeholder and should be replaced with the actual implementation
        // using Retrofit or any other method to communicate with the backend
        Call<String> call = null; // Replace with the actual call object
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    // Tắt Toast thông báo xóa thành công
                    // Toast.makeText(ListFriendActivity.this, "Đã xóa bạn!", Toast.LENGTH_SHORT).show();
                    
                    // Cập nhật giao diện ngay sau khi xóa thành công
                    int position = friendsList.indexOf(user);
                    if (position != -1) {
                        friendsList.remove(position);
                        friendAdapter.notifyItemRemoved(position);
                    }
                    
                    // Load lại danh sách để cập nhật UI từ backend
                    loadFriendList();
                } else {
                    Toast.makeText(ListFriendActivity.this, "Không thể xóa bạn bè", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
} 