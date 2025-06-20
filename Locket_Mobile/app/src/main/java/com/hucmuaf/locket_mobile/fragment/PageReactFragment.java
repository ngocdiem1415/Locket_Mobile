package com.hucmuaf.locket_mobile.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.hucmuaf.locket_mobile.R;
import com.hucmuaf.locket_mobile.adapter.ItemFriendAdapter;
import com.hucmuaf.locket_mobile.adapter.PhotoAdapter;
import com.hucmuaf.locket_mobile.listener.SwipeGestureListenerDown;
import com.hucmuaf.locket_mobile.modedb.Image;
import com.hucmuaf.locket_mobile.modedb.User;
import com.hucmuaf.locket_mobile.service.ApiClient;
import com.hucmuaf.locket_mobile.service.FriendRequestService;
import com.hucmuaf.locket_mobile.service.ImageService;
import com.hucmuaf.locket_mobile.service.OnFriendLoadedListener;
import com.hucmuaf.locket_mobile.service.OnImagesLoadedListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageReactFragment extends Fragment {
    private RecyclerView recyclerView;
    private ViewPager2 imageView;
    private ItemFriendAdapter itemAdapter;
    GestureDetector gestureDetector;
    private List<Image> pages;
    private List<User> listFriend;
    private String userId = null;
    private Context context;
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.react_emoji_comment, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = requireContext(); // hoặc getContext()
        activity = requireActivity(); // cần thiết nếu gọi hàm yêu cầu Activity

        assert getArguments() != null;
        userId = getArguments().getString("userId");
        Log.e("React Activity", String.valueOf(userId));

        View maskView = view.findViewById(R.id.mask);
        LinearLayout layout = view.findViewById(R.id.friends_board);
        LinearLayout down_toggle = view.findViewById(R.id.all_friends);

        down_toggle.setOnClickListener(v -> {
            maskView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
        });

        maskView.setOnClickListener(e -> {
            maskView.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        });

        TextView titleFriend = view.findViewById(R.id.title);

        LinearLayout imageAllFriend = view.findViewById(R.id.image_all_friend);
        TextView tvName = view.findViewById(R.id.tvName);
        // CHẶN SỰ KIỆN TỪ decor_caption TRUYỀN XUỐNG mask
        imageAllFriend.setOnClickListener(v -> {
            Log.e("Click", "Click rồi này");
            getAllImagePages(userId, new OnImagesLoadedListener() {
                @Override
                public void onSuccess(List<Image> images) {
                    pages.clear();
                    // Xử lý danh sách ảnh ở đây
                    pages = images;
                    titleFriend.setText(tvName.getText());
                    Log.e("React Activity", pages.toString());
                    PhotoAdapter adapter = new PhotoAdapter(context, pages);

                    imageView.setAdapter(adapter);
                    Log.e("API", images.toString());
                    maskView.setVisibility(View.GONE);
                    layout.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(String error) {
                    Log.e("API", "Error: " + error);
                }
            });
        });


        recyclerView = view.findViewById(R.id.list_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        listFriend = new ArrayList<>();
        getFriends(userId, new OnFriendLoadedListener() {
            @Override
            public void onSuccess(List<User> users) {
                listFriend = users;
                Log.e("React Activity", listFriend.toString());

                itemAdapter = new ItemFriendAdapter(context, listFriend, new ItemFriendAdapter.OnFriendClickListener() {
                    @Override
                    public void onFriendClick(User user) {
                        getImageOfUser(user.getUserId(), new OnImagesLoadedListener() {
                            @Override
                            public void onSuccess(List<Image> images) {
                                // Xử lý danh sách ảnh ở đây
                                pages = images;
                                titleFriend.setText(user.getUserName());
                                Log.e("React Activity", pages.toString());
                                PhotoAdapter adapter = new PhotoAdapter(context, pages);
                                imageView.setAdapter(adapter);
                                Log.e("API", images.toString());
                                maskView.setVisibility(View.GONE);
                                layout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(String error) {
                                Log.e("API", "Error: " + error);
                            }
                        });
                    }
                });
                recyclerView.setAdapter(itemAdapter);

            }

            @Override
            public void onFailure(String error) {
            }
        });

        imageView = view.findViewById(R.id.list_image_react);
        RecyclerView recyclerView1 = (RecyclerView) imageView.getChildAt(0);
        recyclerView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startX = 0;
                float startY = 0;
                final int threshold = 30; // Độ nhạy nhận biết vuốt
                int currentPage = imageView.getCurrentItem();
                Log.e("Page React Fragment", "Trang hiện tại " + currentPage);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        // Không quyết định gì tại đây cả
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaX = event.getX() - startX;
                        float deltaY = event.getY() - startY;
                        if (Math.abs(deltaY) > Math.abs(deltaX)) {
                            if (currentPage == 0) {
                                Log.d("Touch", deltaY + "+" + deltaX);
                                if (deltaY > threshold) {
                                    // Vuốt xuống
                                    Log.d("Touch", "Vuốt lên");
                                    v.getParent().requestDisallowInterceptTouchEvent(true);
                                } else if (deltaY < -threshold) {
                                    // Vuốt lên
                                    Log.d("Touch", "Vuốt xuống");
                                    v.getParent().requestDisallowInterceptTouchEvent(false);
                                }
                            } else if (currentPage > 0) {
                                Log.e("Page React Fragment", "Vuốt được con rồi này");
                                v.getParent().requestDisallowInterceptTouchEvent(true);
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                }
                return false;
            }
        });
        pages = new ArrayList<>();
        getAllImagePages(userId, new OnImagesLoadedListener() {
            @Override
            public void onSuccess(List<Image> images) {
                pages.clear();
                // Xử lý danh sách ảnh ở đây
                pages = images;

                Log.e("React Activity", pages.toString());
                PhotoAdapter adapter = new PhotoAdapter(context, pages);

                imageView.setAdapter(adapter);
                Log.e("API", images.toString());
            }

            @Override
            public void onFailure(String error) {
                Log.e("API", "Error: " + error);
            }
        });

        View take = view.findViewById(R.id.take);
        take.setOnClickListener(v -> {
            ViewPager2 viewPager = requireActivity().findViewById(R.id.main_viewpager);
            viewPager.setCurrentItem(0, true); // trở lại trang đầu tiên.
        });
    }

    public void getAllImagePages(String userId, OnImagesLoadedListener listener) {
        ImageService imageService = ApiClient.getClient().create(ImageService.class);
        Call<List<Image>> call = imageService.getAllImages(userId);
        Log.e("PAGES", call.toString());

        call.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(@NonNull Call<List<Image>> call, @NonNull Response<List<Image>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure("Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Image>> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void getFriends(String userId, OnFriendLoadedListener listener) {
        FriendRequestService friendRequestService = ApiClient.getClient().create(FriendRequestService.class);
        Call<List<User>> call = friendRequestService.getListFriendByUserId(userId);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure("Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<User>> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void getImageOfUser(String userId, OnImagesLoadedListener listener) {
        ImageService imageService = ApiClient.getClient().create(ImageService.class);
        Call<List<Image>> call = imageService.getImageOfUser(userId);

        call.enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(@NonNull Call<List<Image>> call, @NonNull Response<List<Image>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure("Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Image>> call, @NonNull Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }
}
