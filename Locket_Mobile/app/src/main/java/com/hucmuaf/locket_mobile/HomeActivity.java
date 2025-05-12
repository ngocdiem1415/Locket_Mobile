package com.hucmuaf.locket_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home); // Sử dụng layout home.xml

        // Tìm LinearLayout với id="friends" và thêm sự kiện onClick
        LinearLayout friendsLayout = findViewById(R.id.friends);
        if (friendsLayout != null) {
            friendsLayout.setOnClickListener(v -> {
                Intent intent = new Intent(HomeActivity.this, ListFriendActivity.class);
                startActivity(intent);
            });
        } else {
            Toast.makeText(this, "Không tìm thấy LinearLayout friends", Toast.LENGTH_SHORT).show();
        }
    }
}