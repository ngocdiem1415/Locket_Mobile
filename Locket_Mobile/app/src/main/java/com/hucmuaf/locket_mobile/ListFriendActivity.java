package com.hucmuaf.locket_mobile;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.hucmuaf.locket_mobile.ShareDialogFragment;
public class ListFriendActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfriend);
        // Tìm các ImageView mũi tên
        ImageView facebookChevron = findViewById(R.id.facebook_chevron);
        ImageView messengerChevron = findViewById(R.id.messenger_chevron);
        ImageView instagramChevron = findViewById(R.id.instagram_chevron);
        ImageView shareChevron = findViewById(R.id.share_chevron);
        ImageView btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(v -> showConfirmationDialog());

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
    private void showConfirmationDialog() {
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
            Toast.makeText(this, "Đã xóa bạn!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

}