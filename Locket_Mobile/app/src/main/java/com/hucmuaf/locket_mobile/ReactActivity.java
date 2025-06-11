package com.hucmuaf.locket_mobile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.hucmuaf.locket_mobile.model.Image;
import com.hucmuaf.locket_mobile.model.ItemFriend;
import com.hucmuaf.locket_mobile.model.ItemFriendAdapter;

import java.util.Arrays;
import java.util.List;

public class ReactActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ViewPager2 imageView;
    private ItemFriendAdapter itemAdapter;
    GestureDetector gestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.react_emoji_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        View main = findViewById(R.id.main);
        gestureDetector  = new GestureDetector(this, new SwipeGestureListenerDown(this));
        main.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        recyclerView = findViewById(R.id.list_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ItemFriend> list = Arrays.asList(
          new ItemFriend("account_icon", "Tất cả bạn bè"),
          new ItemFriend("account_icon", "Khanh Duy"),
          new ItemFriend("account_icon", "Cẩm Tú"),
          new ItemFriend("account_icon", "Tấn Lực"),
          new ItemFriend("account_icon", "Thanh Diệu"),
          new ItemFriend("account_icon", "Ngọc Diễm"),
          new ItemFriend("account_icon", "Ngọc Diễm"),
          new ItemFriend("account_icon", "Ngọc Diễm")
        );

        itemAdapter = new ItemFriendAdapter(this, list);

        recyclerView.setAdapter(itemAdapter);

        View maskView = findViewById(R.id.mask);
        LinearLayout layout = findViewById(R.id.friends_board);
        LinearLayout down_toggle = findViewById(R.id.all_friends);

        down_toggle.setOnClickListener(v ->{
            maskView.setVisibility(View.VISIBLE);
            layout.setVisibility(View.VISIBLE);
        });

        maskView.setOnClickListener(e ->{
            maskView.setVisibility(View.GONE);
            layout.setVisibility(View.GONE);
        });

        imageView = findViewById(R.id.list_image_react);
        List<Image> pages = Arrays.asList(
//                new Image("1", "", "Check-in metro", 1, "5", Arrays.asList("1","3")),
//                new Image("2", "", "Dạo một vòng quanh metro", 1, "5", Arrays.asList("1","3")),
//                new Image("3", "", "Metro buổi tối", 1, "5", Arrays.asList("1","3"))
        );
        PhotoAdapter adapter = new PhotoAdapter(this, pages);

        imageView.setAdapter(adapter);

        View take = findViewById(R.id.take);
        take.setOnClickListener(v ->{
            startActivityWithAnimation(this, TakeActivity.class, R.anim.slide_down);
        });
    }

    private void startActivityWithAnimation(Context context, Class<?> cls, int animEnter) {
        if (context == null) return;

        Intent intent = new Intent(context, cls);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(animEnter, R.anim.no_animation);
        }
    }
}
