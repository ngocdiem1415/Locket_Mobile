package com.hucmuaf.locket_mobile;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    
    // API service để gọi backend
    private FriendListApiService apiService;
    
    // User ID hiện tại - lấy động từ Firebase Auth thay vì hardcode
    private String currentUserId;
    
    // Firebase Auth để lấy thông tin user đang đăng nhập
    private FirebaseAuth mAuth;
    
    // UI Components
    private TextView friendCount;           // Hiển thị số lượng bạn bè (lấy từ Firebase)
    private EditText searchFriend;         // Ô tìm kiếm bạn bè
    private RecyclerView friendsRecyclerView;      // Danh sách bạn bè hiện có
    private RecyclerView suggestionsRecyclerView;  // Danh sách gợi ý thêm bạn
    private FriendAdapter friendAdapter;           // Adapter cho danh sách bạn bè
    private SuggestionAdapter suggestionAdapter;   // Adapter cho danh sách gợi ý
    
    // Data lists
    private List<User> friendsList = new ArrayList<>();        // Danh sách bạn bè từ Firebase
    private List<User> suggestionsList = new ArrayList<>();    // Danh sách gợi ý từ Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfriend);
        
        // ===== KHỞI TẠO FIREBASE AUTH =====
        // Lấy instance Firebase Auth để truy cập thông tin user đang đăng nhập
        mAuth = FirebaseAuth.getInstance();
        
        // ===== KHỞI TẠO API SERVICE =====
        // Service để gọi các API backend
        apiService = ApiClient.getFriendListApiService();
        
        // ===== LẤY USER ID TỪ EMAIL ĐĂNG NHẬP =====
        // Thay vì dùng Firebase Auth UID, lấy user ID từ email để đảm bảo đúng với database
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            // Lấy user ID từ email đăng nhập
            loadUserIdFromEmail(currentUser.getEmail());
        } else {
            // Fallback nếu user chưa đăng nhập (trường hợp test)
            currentUserId = "camt91990"; // User ID đã thêm vào Firebase
            Toast.makeText(this, "Using fallback User ID: " + currentUserId, Toast.LENGTH_SHORT).show();
            initializeAfterUserId();
        }
    }
    
    /**
     * Load user ID từ email đăng nhập
     */
    private void loadUserIdFromEmail(String email) {
        // Debug log
        Log.d("ListFriendActivity", "Loading user ID for email: " + email);
        
        Call<String> call = apiService.getUserIdByEmail(email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("ListFriendActivity", "API Response Code: " + response.code());
                Log.d("ListFriendActivity", "API Response Body: " + response.body());
                
                if (response.isSuccessful() && response.body() != null) {
                    currentUserId = response.body();
                    Log.d("ListFriendActivity", "Successfully got User ID: " + currentUserId);
                    Toast.makeText(ListFriendActivity.this, "User ID: " + currentUserId, Toast.LENGTH_SHORT).show();
                    initializeAfterUserId();
                } else {
                    // Fallback nếu không tìm thấy user
                    currentUserId = "camt91990";
                    Log.w("ListFriendActivity", "User not found, using fallback: " + currentUserId);
                    Toast.makeText(ListFriendActivity.this, "Using fallback User ID: " + currentUserId, Toast.LENGTH_SHORT).show();
                    initializeAfterUserId();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ListFriendActivity", "API call failed: " + t.getMessage());
                // Fallback nếu lỗi network
                currentUserId = "camt91990";
                Toast.makeText(ListFriendActivity.this, "Network error, using fallback User ID: " + currentUserId, Toast.LENGTH_SHORT).show();
                initializeAfterUserId();
            }
        });
    }
    
    /**
     * Khởi tạo UI sau khi đã có user ID
     */
    private void initializeAfterUserId() {
        // ===== TEST KẾT NỐI API TRƯỚC =====
        testApiConnection();
        
        // ===== KHỞI TẠO UI VÀ LOAD DỮ LIỆU =====
        initializeViews();        // Khởi tạo các view và setup search
        setupRecyclerViews();     // Setup RecyclerView cho danh sách bạn bè
        setupClickListeners();    // Setup click listeners cho các mũi tên share
        setupAppIconClickListeners(); // Setup click listeners cho các icon app
        loadFriendList();         // Load danh sách bạn bè từ Firebase
    }
    
    /**
     * Test kết nối API để kiểm tra backend có hoạt động không
     */
    private void testApiConnection() {
        Log.d("ListFriendActivity", "Testing API connection...");
        Call<String> call = apiService.testConnection();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("ListFriendActivity", "API Test Response Code: " + response.code());
                Log.d("ListFriendActivity", "API Test Response Body: " + response.body());
                if (response.isSuccessful()) {
                    Toast.makeText(ListFriendActivity.this, "API Connected: " + response.body(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListFriendActivity.this, "API Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ListFriendActivity", "API Test Failed: " + t.getMessage());
                Toast.makeText(ListFriendActivity.this, "API Connection Failed: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * Khởi tạo các UI components và setup tìm kiếm realtime
     */
    private void initializeViews() {
        // Ánh xạ các view từ layout
        friendCount = findViewById(R.id.friendCount);
        searchFriend = findViewById(R.id.searchFriend);
        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        suggestionsRecyclerView = findViewById(R.id.suggestionsRecyclerView);
        
        // ===== SETUP TÌM KIẾM REALTIME =====
        // TextWatcher để lắng nghe thay đổi text trong ô search
        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Khi user gõ text, gọi hàm tìm kiếm realtime
                searchUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    /**
     * Setup RecyclerView cho danh sách bạn bè và gợi ý
     */
    private void setupRecyclerViews() {
        // ===== SETUP RECYCLERVIEW CHO DANH SÁCH BẠN BÈ =====
        // Adapter cho danh sách bạn bè hiện có, callback removeFriend khi xóa bạn
        friendAdapter = new FriendAdapter(friendsList, this::removeFriend);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setAdapter(friendAdapter);
        
        // ===== SETUP RECYCLERVIEW CHO DANH SÁCH GỢI Ý =====
        // Adapter cho danh sách gợi ý thêm bạn, callback addFriend khi thêm bạn
        suggestionAdapter = new SuggestionAdapter(suggestionsList, this::addFriend);
        suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        suggestionsRecyclerView.setAdapter(suggestionAdapter);
    }
    
    /**
     * Setup click listeners cho các mũi tên share
     * THAY ĐỔI CHÍNH: Share trực tiếp không hiện dialog
     */
    private void setupClickListeners() {
        // ===== ÁNH XẠ CÁC MŨI TÊN SHARE =====
        ImageView facebookChevron = findViewById(R.id.facebook_chevron);
        ImageView messengerChevron = findViewById(R.id.messenger_chevron);
        ImageView instagramChevron = findViewById(R.id.instagram_chevron);
        ImageView shareChevron = findViewById(R.id.share_chevron);

        // ===== TẠO SHARE TEXT VỚI USER ID ĐỘNG =====
        // Dòng chữ share với userId thực của user đang đăng nhập
        String shareText = "Tôi muốn thêm bạn vào Màn hình chính của tôi qua Modis. Chạm vào liên kết để chấp nhận 💛 https://modis.app/invite/" + currentUserId;

        // ===== FACEBOOK SHARE TRỰC TIẾP =====
        // Khi bấm mũi tên Facebook → Share trực tiếp qua Facebook app
        facebookChevron.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.facebook.katana");  // Package Facebook app
            try {
                startActivity(shareIntent);  // Mở Facebook app trực tiếp
            } catch (Exception e) {
                Toast.makeText(this, "Facebook chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        // ===== MESSENGER SHARE TRỰC TIẾP =====
        // Khi bấm mũi tên Messenger → Share trực tiếp qua Messenger app
        messengerChevron.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.facebook.orca");  // Package Messenger app
            try {
                startActivity(shareIntent);  // Mở Messenger app trực tiếp
            } catch (Exception e) {
                Toast.makeText(this, "Messenger chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        // ===== INSTAGRAM SHARE TRỰC TIẾP =====
        // Khi bấm mũi tên Instagram → Share trực tiếp qua Instagram app
        instagramChevron.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.instagram.android");  // Package Instagram app
            try {
                startActivity(shareIntent);  // Mở Instagram app trực tiếp
            } catch (Exception e) {
                Toast.makeText(this, "Instagram chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        // ===== SHARE CHUNG =====
        // Khi bấm mũi tên Share → Mở menu share chung của Android
        shareChevron.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));  // Menu share chung
        });
    }
    
    /**
     * Setup click listeners cho các icon tìm bạn bè từ ứng dụng khác
     * THÊM MỚI: Các icon này sẽ chuyển hướng giống như các mũi tên share
     */
    private void setupAppIconClickListeners() {
        // ===== TẠO SHARE TEXT VỚI USER ID ĐỘNG =====
        // Dòng chữ share với userId thực của user đang đăng nhập
        String shareText = "Tôi muốn thêm bạn vào Màn hình chính của tôi qua Modis. Chạm vào liên kết để chấp nhận 💛 https://modis.app/invite/" + currentUserId;

        // ===== ÁNH XẠ CÁC ICON APP =====
        LinearLayout messengerLayout = findViewById(R.id.messenger_layout);
        LinearLayout facebookLayout = findViewById(R.id.facebook_layout);
        LinearLayout instagramLayout = findViewById(R.id.instagram_layout);
        LinearLayout shareLayout = findViewById(R.id.share_layout);

        // ===== MESSENGER ICON CLICK =====
        // Khi bấm icon Messenger → Share trực tiếp qua Messenger app
        messengerLayout.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.facebook.orca");  // Package Messenger app
            try {
                startActivity(shareIntent);  // Mở Messenger app trực tiếp
            } catch (Exception e) {
                Toast.makeText(this, "Messenger chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        // ===== FACEBOOK ICON CLICK =====
        // Khi bấm icon Facebook → Share trực tiếp qua Facebook app
        facebookLayout.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.facebook.katana");  // Package Facebook app
            try {
                startActivity(shareIntent);  // Mở Facebook app trực tiếp
            } catch (Exception e) {
                Toast.makeText(this, "Facebook chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        // ===== INSTAGRAM ICON CLICK =====
        // Khi bấm icon Instagram → Share trực tiếp qua Instagram app
        instagramLayout.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.setPackage("com.instagram.android");  // Package Instagram app
            try {
                startActivity(shareIntent);  // Mở Instagram app trực tiếp
            } catch (Exception e) {
                Toast.makeText(this, "Instagram chưa được cài đặt!", Toast.LENGTH_SHORT).show();
            }
        });

        // ===== SHARE ICON CLICK =====
        // Khi bấm icon Share → Mở menu share chung của Android
        shareLayout.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));  // Menu share chung
        });
    }
    
    /**
     * Load danh sách bạn bè từ Firebase
     * THAY ĐỔI CHÍNH: Số lượng và danh sách bạn bè lấy từ Firebase, không set cứng
     */
    private void loadFriendList() {
        Log.d("ListFriendActivity", "Loading friend list for user ID: " + currentUserId);
        
        // Gọi API để lấy danh sách bạn bè từ Firebase
        Call<FriendListResponse> call = apiService.getFriendList(currentUserId);
        call.enqueue(new Callback<FriendListResponse>() {
            @Override
            public void onResponse(Call<FriendListResponse> call, Response<FriendListResponse> response) {
                Log.d("ListFriendActivity", "Friend List API Response Code: " + response.code());
                Log.d("ListFriendActivity", "Friend List API Response Body: " + response.body());
                
                if (response.isSuccessful() && response.body() != null) {
                    FriendListResponse friendListResponse = response.body();
                    
                    Log.d("ListFriendActivity", "Total Friends: " + friendListResponse.getTotalFriends());
                    Log.d("ListFriendActivity", "Max Friends: " + friendListResponse.getMaxFriends());
                    Log.d("ListFriendActivity", "Friends List Size: " + friendListResponse.getFriends().size());
                    
                    // ===== CẬP NHẬT SỐ LƯỢNG BẠN BÈ TỪ FIREBASE =====
                    // Thay vì set cứng "10 / 20", giờ lấy số lượng thực từ Firebase
                    friendCount.setText(friendListResponse.getTotalFriends() + " / " + 
                                      friendListResponse.getMaxFriends() + " người bạn đã được bổ sung");
                    
                    // ===== CẬP NHẬT DANH SÁCH BẠN BÈ TỪ FIREBASE =====
                    // Hiển thị đúng danh sách bạn bè hiện có trên Firebase
                    friendsList.clear();
                    friendsList.addAll(friendListResponse.getFriends());
                    friendAdapter.notifyDataSetChanged();
                    
                    // ===== CẬP NHẬT DANH SÁCH GỢI Ý =====
                    // Danh sách gợi ý thêm bạn từ Firebase
                    suggestionsList.clear();
                    suggestionsList.addAll(friendListResponse.getSuggestions());
                    suggestionAdapter.notifyDataSetChanged();
                    
                    Log.d("ListFriendActivity", "Successfully updated UI with friend data");
                } else {
                    Log.e("ListFriendActivity", "Failed to load friend list - Response not successful");
                    Toast.makeText(ListFriendActivity.this, "Failed to load friend list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FriendListResponse> call, Throwable t) {
                Log.e("ListFriendActivity", "Friend List API call failed: " + t.getMessage());
                Toast.makeText(ListFriendActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Tìm kiếm bạn bè realtime
     * THAY ĐỔI CHÍNH: Khi search trống sẽ load lại danh sách gợi ý ban đầu
     */
    private void searchUsers(String query) {
        if (query.trim().isEmpty()) {
            // ===== KHI SEARCH TRỐNG =====
            // Load lại danh sách gợi ý ban đầu từ Firebase
            loadFriendList();
            return;
        }
        
        // ===== TÌM KIẾM REALTIME =====
        // Gọi API tìm kiếm user theo query
        SearchUserRequest request = new SearchUserRequest(query, currentUserId);
        Call<List<User>> call = apiService.searchUsers(request);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Cập nhật danh sách gợi ý với kết quả tìm kiếm
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
    
    /**
     * Thêm bạn bè
     */
    private void addFriend(User user) {
        // Gửi lời mời kết bạn
        FriendRequestDto request = new FriendRequestDto(currentUserId, user.getUserId());
        Call<String> call = apiService.sendFriendRequest(request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ListFriendActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();
                    // Xóa khỏi danh sách gợi ý sau khi gửi lời mời
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
    
    /**
     * Xóa bạn bè - hiển thị dialog xác nhận
     */
    private void removeFriend(User user) {
        showConfirmationDialog(user);
    }
    
    /**
     * Hiển thị dialog xác nhận xóa bạn bè
     */
    private void showConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.confirmation_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        // ===== CẬP NHẬT TÊN USER ĐỘNG =====
        // Thay vì hardcode "Đăng Trần", giờ lấy tên thực từ user object
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
            // Gọi API để xóa bạn bè
            Call<String> call = apiService.removeFriend(currentUserId, user.getUserId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ListFriendActivity.this, "Đã xóa bạn!", Toast.LENGTH_SHORT).show();
                        // Xóa khỏi danh sách bạn bè
                        friendsList.remove(user);
                        friendAdapter.notifyDataSetChanged();
                        // Load lại danh sách để cập nhật số lượng
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
    
    /**
     * Share qua social media (hàm cũ, không dùng nữa)
     * Thay thế bằng share trực tiếp trong setupClickListeners()
     */
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