package com.hucmuaf.locket_mobile.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.service.ApiClient;
import com.hucmuaf.locket_mobile.inteface.FriendListApiService;
import com.hucmuaf.locket_mobile.inteface.OnFriendActionListener;
import com.hucmuaf.locket_mobile.inteface.OnPendingRequestActionListener;
import com.hucmuaf.locket_mobile.model.FriendListResponse;
import com.hucmuaf.locket_mobile.model.User;
import com.hucmuaf.locket_mobile.model.FriendRequest;
import com.hucmuaf.locket_mobile.dto.SearchUserRequest;
//import com.hucmuaf.locket_mobile.dto.FriendRequestDto;
import com.hucmuaf.locket_mobile.dto.ShareRequest;
import com.hucmuaf.locket_mobile.adapter.FriendAdapter;
import com.hucmuaf.locket_mobile.adapter.PendingRequestAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListFriendActivity extends AppCompatActivity {

    private FriendListApiService apiService;

    private String currentUserId;

    private TextView friendCount;
    private RecyclerView friendsRecyclerView;
    private RecyclerView pendingRequestsRecyclerView;
    private FriendAdapter friendAdapter;
    private PendingRequestAdapter pendingRequestAdapter;

    private final List<User> friendsList = new ArrayList<>();
    private final List<FriendRequest> pendingRequestsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfriend);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        apiService = ApiClient.getFriendListApiService();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            loadUserIdFromEmail(currentUser.getEmail());
        } else {
            currentUserId = " ";
            initializeAfterUserId();
        }
    }

    private void loadUserIdFromEmail(String email) {
        Log.d("ListFriendActivity", "Loading user ID for email: " + email);
        // Gọi API để lấy user ID từ email
        Call<String> call = apiService.getUserIdByEmail(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, Response<String> response) {
                Log.d("ListFriendActivity", "API Response Code: " + response.code());
                Log.d("ListFriendActivity", "API Response Body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    currentUserId = response.body();
                    Log.d("ListFriendActivity", "Successfully got User ID: " + currentUserId);
                    initializeAfterUserId();
                } else {
                    currentUserId = "Unknown";
                    Log.w("ListFriendActivity", "User not found, using fallback: " + currentUserId);
                    initializeAfterUserId();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ListFriendActivity", "API call failed: " + t.getMessage());
                currentUserId = " ";
                initializeAfterUserId();
            }
        });
    }

    //      Khởi tạo UI sau khi đã có user ID
    private void initializeAfterUserId() {
        testApiConnection();

        initializeViews();
        setupRecyclerViews();
        setupClickListeners();
        setupAppIconClickListeners();
        loadFriendList();
        loadPendingRequests();
    }

    //      Test kết nối API để kiểm tra backend có hoạt động không
//      Gọi API test đơn giản để đảm bảo kết nối ổn định
    private void testApiConnection() {
        Log.d("ListFriendActivity", "Testing API connection...");
        Call<String> call = apiService.testConnection();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("ListFriendActivity", "API Test Response Code: " + response.code());
                Log.d("ListFriendActivity", "API Test Response Body: " + response.body());

                // Xử lý lỗi JSON parsing
                if (response.code() == 204) {
                    Log.d("ListFriendActivity", "API Test successful - No Content");
                } else if (response.isSuccessful()) {
                    Log.d("ListFriendActivity", "API Test successful");
                } else {
                    Log.e("ListFriendActivity", "API Test failed with code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ListFriendActivity", "API Test Failed: " + t.getMessage());
                // Chỉ hiển thị Toast khi lỗi nghiêm trọng
                if (t.getMessage() != null && !t.getMessage().contains("JSON document was not fully consumed")) {
                    Toast.makeText(ListFriendActivity.this, "Kết nối thất bại, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //      Khởi tạo các UI components và setup tìm kiếm realtime
    private void initializeViews() {
        friendCount = findViewById(R.id.friendCount);
        EditText searchFriend = findViewById(R.id.searchFriend);
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        pendingRequestsRecyclerView = findViewById(R.id.pendingRequestsRecyclerView);

        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Khi user gõ text, gọi hàm tìm kiếm realtime
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupRecyclerViews() {
        friendAdapter = new FriendAdapter(friendsList, new OnFriendActionListener() {
            @Override
            public void onRemoveFriend(User user) {
                removeFriend(user);
            }
        });
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(friendAdapter);

        pendingRequestAdapter = new PendingRequestAdapter(pendingRequestsList, new OnPendingRequestActionListener() {
            @Override
            public void onAcceptRequest(FriendRequest request) {
                acceptFriendRequest(request);
            }

            @Override
            public void onRejectRequest(FriendRequest request) {
                rejectFriendRequest(request);
            }
        });
        pendingRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pendingRequestsRecyclerView.setAdapter(pendingRequestAdapter);
    }

    private void setupClickListeners() {
        ImageView facebookChevron = findViewById(R.id.facebook_chevron);
        ImageView messengerChevron = findViewById(R.id.messenger_chevron);
        ImageView instagramChevron = findViewById(R.id.instagram_chevron);
        ImageView shareChevron = findViewById(R.id.share_chevron);

        String shareText = "Tôi muốn thêm bạn vào Màn hình chính của tôi qua Modis. Chạm vào liên kết để chấp nhận 💛 https://modis.app/invite/" + currentUserId;

        facebookChevron.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.facebook.katana");
            try {
                startActivity(shareIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Facebook chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        messengerChevron.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.facebook.orca");
            try {
                startActivity(shareIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Messenger chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        instagramChevron.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.instagram.android");
            try {
                startActivity(shareIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Instagram chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });
        shareChevron.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
        });
    }

    private void setupAppIconClickListeners() {
        String shareText = "Tôi muốn thêm bạn vào Màn hình chính của tôi qua Modis. Chạm vào liên kết để chấp nhận 💛 https://modis.app/invite/" + currentUserId;

        LinearLayout messengerLayout = findViewById(R.id.messenger_layout);
        LinearLayout facebookLayout = findViewById(R.id.facebook_layout);
        LinearLayout instagramLayout = findViewById(R.id.instagram_layout);
        LinearLayout shareLayout = findViewById(R.id.share_layout);

        messengerLayout.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.facebook.orca");
            try {
                startActivity(shareIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Messenger chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        facebookLayout.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.facebook.katana");
            try {
                startActivity(shareIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Facebook chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        instagramLayout.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.instagram.android");
            try {
                startActivity(shareIntent);
            } catch (Exception e) {
                Toast.makeText(this, "Instagram chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        shareLayout.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));  // Menu share chung
        });
    }

    private void loadFriendList() {
        Log.d("ListFriendActivity", "Loading friend list for user ID: " + currentUserId);

        // Gọi API để lấy danh sách bạn bè từ Firebase
        Call<FriendListResponse> call = apiService.getFriendList(currentUserId);
        call.enqueue(new Callback<FriendListResponse>() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                Log.d("ListFriendActivity", "Friend List API Response Code: " + response.code());
                Log.d("ListFriendActivity", "Friend List API Response Body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    FriendListResponse friendListResponse = response.body();

                    Log.d("ListFriendActivity", "Total Friends: " + friendListResponse.getTotalFriends());
                    Log.d("ListFriendActivity", "Max Friends: " + friendListResponse.getMaxFriends());
                    Log.d("ListFriendActivity", "Friends List Size: " + friendListResponse.getFriends().size());

                    friendCount.setText(friendListResponse.getTotalFriends() + " / " +
                            friendListResponse.getMaxFriends() + " người bạn đã được bổ sung");

                    // Hiển thị đúng danh sách bạn bè hiện có trên Firebase
                    friendsList.clear();
                    friendsList.addAll(friendListResponse.getFriends());
                    friendAdapter.notifyDataSetChanged();

                    Log.d("ListFriendActivity", "Successfully updated UI with friend data");
                } else {
                    Log.e("ListFriendActivity", "Failed to load friend list - Response not successful or null");
                    friendsList.clear();
                    friendAdapter.notifyDataSetChanged();
                    friendCount.setText("0 / 20 người bạn đã được bổ sung");
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
                Log.e("ListFriendActivity", "Friend List API call failed: " + t.getMessage());
                // Xử lý lỗi JSON parsing
                if (t.getMessage() != null && t.getMessage().contains("JSON document was not fully consumed")) {
                    Log.d("ListFriendActivity", "JSON parsing error - likely empty response, treating as success");
                    friendsList.clear();
                    friendAdapter.notifyDataSetChanged();
                    friendCount.setText("0 / 20 người bạn đã được bổ sung");
                } else {
                    Toast.makeText(ListFriendActivity.this, "Không thể tải danh sách bạn bè", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //      Tìm kiếm bạn bè realtime
    private void searchUsers(String query) {
        if (query.trim().isEmpty()) {
            // Load lại danh sách bạn bè ban đầu từ Firebase
            loadFriendList();
            return;
        }

        // TÌM KIẾM REALTIME
        SearchUserRequest request = new SearchUserRequest(query, currentUserId);
        Call<List<User>> call = apiService.searchUsers(request);
        call.enqueue(new Callback<List<User>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Cập nhật danh sách bạn bè với kết quả tìm kiếm
                    friendsList.clear();
                    friendsList.addAll(response.body());
                    friendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(ListFriendActivity.this, "Search failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //      Thêm bạn bè
    private void addFriend(User user) {
        // Gửi lời mời kết bạn
        FriendRequest request = new FriendRequest(currentUserId, user.getUserId(), null, null, System.currentTimeMillis());
        Call<String> call = apiService.sendFriendRequest(request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ListFriendActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();
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

    //xóa bạn
    private void removeFriend(User user) {
        Call<String> call = apiService.removeFriend(currentUserId, user.getUserId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    int position = friendsList.indexOf(user);
                    if (position != -1) {
                        friendsList.remove(position);
                        friendAdapter.notifyItemRemoved(position);
                    }
                    loadFriendList();
                } else {
                    Toast.makeText(ListFriendActivity.this, "Không thể xóa bạn bè", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ListFriendActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        TextView titleText = dialogView.findViewById(R.id.dialogTitle);
        if (titleText != null) {
            String userName = user.getFullName() != null ? user.getFullName() : user.getUserName();
            if (userName == null || userName.isEmpty()) {
                userName = "người bạn này";
            }
            titleText.setText("Xóa " + userName + " khỏi Modis của bạn?");
        }

        // Ánh xạ các nút
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnDelete = dialogView.findViewById(R.id.btnDelete);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            Call<String> call = apiService.removeFriend(currentUserId, user.getUserId());
            call.enqueue(new Callback<String>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        friendsList.remove(user);
                        friendAdapter.notifyDataSetChanged();

                        loadFriendList();
                    } else {
                        Toast.makeText(ListFriendActivity.this, "Không thể xóa bạn bè", Toast.LENGTH_SHORT).show();
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

    //share qua app khác
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

    //      Load danh sách lời mời kết bạn đang chờ
    private void loadPendingRequests() {
        Log.d("ListFriendActivity", "Loading pending requests for user ID: " + currentUserId);

        Call<List<FriendRequest>> call = apiService.getPendingRequests(currentUserId);
        call.enqueue(new Callback<List<FriendRequest>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                Log.d("ListFriendActivity", "Pending Requests API Response Code: " + response.code());
                Log.d("ListFriendActivity", "Pending Requests API Response Body: " + response.body());

                if (response.isSuccessful() && response.body() != null) {
                    List<FriendRequest> pendingRequests = response.body();
                    Log.d("ListFriendActivity", "Pending Requests Count: " + pendingRequests.size());

//                    pendingRequestsList.clear();
                    pendingRequestsList.addAll(pendingRequests);
                    pendingRequestAdapter.notifyDataSetChanged();

                    Log.d("ListFriendActivity", "Successfully updated pending requests list");
                } else {
                    Log.e("ListFriendActivity", "Failed to load pending requests - Response not successful or null");
                    pendingRequestsList.clear();
                    pendingRequestAdapter.notifyDataSetChanged();
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
                Log.e("ListFriendActivity", "Pending Requests API call failed: " + t.getMessage());

                // Xử lý lỗi JSON parsing
                if (t.getMessage() != null && t.getMessage().contains("JSON document was not fully consumed")) {
                    Log.d("ListFriendActivity", "JSON parsing error for pending requests - likely empty response, treating as success");
                    pendingRequestsList.clear();
                    pendingRequestAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ListFriendActivity.this, "Không thể tải lời mời kết bạn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //      Chấp nhận lời mời kết bạn
    private void acceptFriendRequest(FriendRequest request) {
        Log.d("ListFriendActivity", "Accepting friend request: " + request.getFriendRequestId());
        Call<String> call = apiService.acceptFriendRequest(request.getFriendRequestId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("ListFriendActivity", "Friend request accepted successfully");
                    loadPendingRequests(); // cập nhật lại danh sách lời mời
                    loadFriendList();      // cập nhật lại danh sách bạn bè
                } else {
                    Log.e("ListFriendActivity", "Failed to accept friend request - Response code: " + response.code());
                    Toast.makeText(ListFriendActivity.this, "Không thể chấp nhận lời mời", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ListFriendActivity", "Accept friend request failed: " + t.getMessage());

                // Xử lý lỗi JSON parsing
                if (t.getMessage() != null && t.getMessage().contains("JSON document was not fully consumed")) {
                    Log.d("ListFriendActivity", "JSON parsing error for accept - likely empty response, treating as success");
                    loadPendingRequests(); // cập nhật lại danh sách lời mời
                    loadFriendList();      // cập nhật lại danh sách bạn bè
                } else {
                    Toast.makeText(ListFriendActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //      Từ chối lời mời kết bạn
    private void rejectFriendRequest(FriendRequest request) {
        Log.d("ListFriendActivity", "Rejecting friend request: " + request.getFriendRequestId());
        Call<String> call = apiService.rejectFriendRequest(request.getFriendRequestId());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("ListFriendActivity", "Friend request rejected successfully");
                    loadPendingRequests(); // cập nhật lại danh sách lời mời
                    loadFriendList();      // cập nhật lại danh sách bạn bè
                } else {
                    Log.e("ListFriendActivity", "Failed to reject friend request - Response code: " + response.code());
                    Toast.makeText(ListFriendActivity.this, "Không thể từ chối lời mời", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ListFriendActivity", "Reject friend request failed: " + t.getMessage());

                // Xử lý lỗi JSON parsing
                if (t.getMessage() != null && t.getMessage().contains("JSON document was not fully consumed")) {
                    Log.d("ListFriendActivity", "JSON parsing error for reject - likely empty response, treating as success");
                    loadPendingRequests(); // cập nhật lại danh sách lời mời
                    loadFriendList();      // cập nhật lại danh sách bạn bè
                } else {
                    Toast.makeText(ListFriendActivity.this, "Lỗi kết nối, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}