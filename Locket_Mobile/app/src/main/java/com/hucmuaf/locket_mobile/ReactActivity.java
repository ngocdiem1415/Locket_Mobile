package com.hucmuaf.locket_mobile;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hucmuaf.locket_mobile.model.ItemFriend;
import com.hucmuaf.locket_mobile.model.ItemFriendAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReactActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemFriendAdapter itemAdapter;

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

        recyclerView = findViewById(R.id.list_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<ItemFriend> list = Arrays.asList(
          new ItemFriend("account_icon", "Khanh Duy"),
          new ItemFriend("account_icon", "Bích Loan"),
          new ItemFriend("account_icon", "Tấn Lực"),
          new ItemFriend("account_icon", "Thanh Diệu")
        );

        itemAdapter = new ItemFriendAdapter(this, list);

        recyclerView.setAdapter(itemAdapter);

    }
}
