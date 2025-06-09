package com.hucmuaf.locket_mobile;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.ShareDialogFragment;
import com.hucmuaf.locket_mobile.api.ApiClient;
import com.hucmuaf.locket_mobile.api.FriendListApiService;
import com.hucmuaf.locket_mobile.model.FriendListResponse;
import com.hucmuaf.locket_mobile.model.User;
import com.hucmuaf.locket_mobile.dto.SearchUserRequest;
import com.hucmuaf.locket_mobile.dto.FriendRequestDto;
import com.hucmuaf.locket_mobile.dto.ShareRequest;
import com.hucmuaf.locket_mobile.adapter.FriendAdapter;
import com.hucmuaf.locket_mobile.adapter.SuggestionAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFriendActivity extends AppCompatActivity {
    
    private FriendListApiService apiService;
    private String currentUserId = "user123"; // Replace with actual user ID from login
    
    // UI Components
    private TextView friendCount;
    private EditText searchFriend;
    private RecyclerView friendsRecyclerView;
    private RecyclerView suggestionsRecyclerView;
    private FriendAdapter friendAdapter;
    private SuggestionAdapter suggestionAdapter;
    
    private List<User> friendsList = new ArrayList<>();
    private List<User> suggestionsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfriend);
        
        // Initialize API service
        apiService = ApiClient.getFriendListApiService();
        
        // Initialize UI components
        initializeViews();
        setupRecyclerViews();
        setupClickListeners();
        
        // Load friend list
        loadFriendList();
    }
    
    private void initializeViews() {
        friendCount = findViewById(R.id.friendCount);
        searchFriend = findViewById(R.id.searchFriend);
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        suggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        
        // Setup search functionality
        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void setupRecyclerViews() {
        // Setup friends recycler view
        friendAdapter = new FriendAdapter(friendsList, this::removeFriend);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(friendAdapter);
        
        // Setup suggestions recycler view
        suggestionAdapter = new SuggestionAdapter(suggestionsList, this::addFriend);
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        suggestionsRecyclerView.setAdapter(suggestionAdapter);
    }
    
    private void setupClickListeners() {
        // Tìm các ImageView mũi tên
        ImageView facebookChevron = findViewById(R.id.facebook_chevron);
        ImageView messengerChevron = findViewById(R.id.messenger_chevron);
        ImageView instagramChevron = findViewById(R.id.instagram_chevron);
        ImageView shareChevron = findViewById(R.id.share_chevron);

        // Xử lý sự kiện click để hiển thị ShareDialogFragment
        facebookChevron.setOnClickListener(v -> {
            ShareDialogFragment shareDialog = new ShareDialogFragment();
            shareDialog.show(getSupportFragmentManager(), "ShareDialog");
        });

        messengerChevron.setOnClickListener(v -> {
            ShareDialogFragment shareDialog = new ShareDialogFragment();
            shareDialog.show(getSupportFragmentManager(), "ShareDialog");
        });

        instagramChevron.setOnClickListener(v -> {
            ShareDialogFragment shareDialog = new ShareDialogFragment();
            shareDialog.show(getSupportFragmentManager(), "ShareDialog");
        });

        shareChevron.setOnClickListener(v -> {
            ShareDialogFragment shareDialog = new ShareDialogFragment();
            shareDialog.show(getSupportFragmentManager(), "ShareDialog");
        });
    }
    
    private void loadFriendList() {
        Call<FriendListResponse> call = apiService.getFriendList(currentUserId);
        call.enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    FriendListResponse friendListResponse = response.body();
                    
                    // Update friend count
                    friendCount.setText(friendListResponse.getTotalFriends() + " / " + 
                                      friendListResponse.getMaxFriends() + " người bạn đã được bổ sung");
                    
                    // Update friends list
                    friendsList.clear();
                    friendsList.addAll(friendListResponse.getFriends());
                    friendAdapter.notifyDataSetChanged();
                    
                    // Update suggestions list
                    suggestionsList.clear();
                    suggestionsList.addAll(friendListResponse.getSuggestions());
                    suggestionAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListFriendActivity.this, "Failed to load friend list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
                Toast.makeText(ListFriendActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void searchUsers(String query) {
        if (query.trim().isEmpty()) {
            return;
        }
        
        SearchUserRequest request = new SearchUserRequest(query, currentUserId);
        Call<List<User>> call = apiService.searchUsers(request);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update suggestions with search results
                    suggestionsList.clear();
                    suggestionsList.addAll(response.body());
                    suggestionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(ListFriendActivity.this, "Search failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void addFriend(User user) {
        FriendRequestDto request = new FriendRequestDto(currentUserId, user.getUserId());
        Call<String> call = apiService.sendFriendRequest(request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ListFriendActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();
                    // Remove from suggestions
                    suggestionsList.remove(user);
                    suggestionAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListFriendActivity.this, "Failed to send friend request", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ListFriendActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void removeFriend(User user) {
        showConfirmationDialog(user);
    }
    
    private void showConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        // Ánh xạ các nút
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            // Call API to remove friend
            Call<String> call = apiService.removeFriend(currentUserId, user.getUserId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ListFriendActivity.this, "Đã xóa bạn!", Toast.LENGTH_SHORT).show();
                        // Remove from friends list
                        friendsList.remove(user);
                        friendAdapter.notifyDataSetChanged();
                        // Reload friend list to update count
                        loadFriendList();
                    } else {
                        Toast.makeText(ListFriendActivity.this, "Failed to remove friend", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ListFriendActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
        });
    }
    
    private void shareToSocialMedia(String platform) {
        ShareRequest request = new ShareRequest(currentUserId, "Join me on Locket!");
        Call<String> call = apiService.shareToSocialMedia(platform, request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ListFriendActivity.this, "Shared to " + platform, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListFriendActivity.this, "Failed to share", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ListFriendActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}