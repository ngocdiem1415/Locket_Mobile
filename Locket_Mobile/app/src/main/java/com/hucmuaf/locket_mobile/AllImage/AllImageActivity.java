package com.hucmuaf.locket_mobile.AllImage;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.ModelDB.Image;
import com.hucmuaf.locket_mobile.R;

import java.util.ArrayList;
import java.util.List;

public class AllImageActivity extends AppCompatActivity {
    private RecyclerView photoGrid;
    private ImageAdapter adapter;
    private List<Image> allPhotos = new ArrayList<>();
    private String currentUserId = "u1"; // Hoặc lấy từ session/login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_images);

        photoGrid = findViewById(R.id.photo_grid);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        photoGrid.setLayoutManager(layoutManager);

//        adapter = new ImageAdapter(this, allPhotos, photo -> {
//            Intent intent = new Intent(AllImageActivity.this, PhotoDetailActivity.class);
//            intent.putExtra("photo", new Gson().toJson(photo)); // Truyền sang chi tiết
//            startActivity(intent);
//        });
//
//        photoGrid.setAdapter(adapter);
//
//        loadAllPhotos(); // Gọi API ban đầu
//        setupFriendFilter(); // Dropdown chọn bạn bè
    }

//    private void loadAllPhotos() {
//        // Giả định gọi từ backend /api/photos/user/{id}
//        ApiClient.getPhotosForUser(currentUserId, new Callback<List<Photo>>() {
//            @Override
//            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
//                if (response.isSuccessful()) {
//                    allPhotos = response.body();
//                    adapter.updateList(allPhotos);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Photo>> call, Throwable t) {
//                Toast.makeText(AllImageActivity.this, "Lỗi tải ảnh", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void setupFriendFilter() {
//        LinearLayout friends = findViewById(R.id.friends);
//        friends.setOnClickListener(v -> {
//            // Gọi dialog chọn bạn -> getFriendId
//            String selectedFriendId = "u2"; // ví dụ
//            filterByFriend(selectedFriendId);
//        });
//    }
//
//    private void filterByFriend(String friendId) {
//        // Gọi /api/photos/user/{currentUserId}/friend/{friendId}
//        ApiClient.getPhotosByFriend(currentUserId, friendId, new Callback<List<Photo>>() {
//            @Override
//            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
//                if (response.isSuccessful()) {
//                    adapter.updateList(response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Photo>> call, Throwable t) {
//                Toast.makeText(AllImageActivity.this, "Lỗi lọc ảnh", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
