package com.hucmuaf.locket_mobile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Collections;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
        // Firebase reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
// Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                // xử lý dữ liệu
                Toast.makeText(getBaseContext(), "Value is", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(getBaseContext(), "Failed to read value", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Tạo dữ liệu mẫu
//        User user1 = new User("1", "tuvo", "Võ Thị Cẩm Tú", "tuvo@example.com", "0123456789", "https://linktoavatar.com","123");
//
//        Friend friend1 = new Friend("1", "tuvo");
//
//        Message message1 = new Message("text", "Hello!", "1");
//
//        Reaction activity1 = new Reaction("1", "image1", "1", "activity_icon.png");
//
//        Image image1 = new Image("image1", "https://linktoimage.com", "A beautiful image");
//
//        // Đẩy lên Firebase Realtime Database
//        myRef.child("users").child(user1.getId()).setValue(user1);
//
//        myRef.child("friends").child(user1.getId()).child(friend1.getId()).setValue(friend1);
//
//        myRef.child("messages").child("message1").setValue(message1);
//
//        myRef.child("activities").child("activity1").setValue(activity1);
//
//        myRef.child("images").child(image1.getId()).setValue(image1);
        DatabaseReference usersRef = myRef.child("users");
        User user1 = new User("u1", "namnguyen", "Nguyễn Nam", "nam@gmail.com", "0987654321", "https://example.com/avatars/user123.jpg", "123");
        User user2 = new User("u2", "linhphan", "Phan Linh", "linh@gmail.com", "0909123456", "https://example.com/avatars/user456.jpg", "123");
        usersRef.child(user1.getUserId()).setValue(user1);
        usersRef.child(user2.getUserId()).setValue(user2);

        DatabaseReference friendsRef = myRef.child("friends");
        long timestamp = System.currentTimeMillis();
        Friend f1 = new Friend("u1", "u2", timestamp);
        Friend f2 = new Friend("u2", "u1", timestamp);
        friendsRef.child("u1").child("u2").setValue(f1);
        friendsRef.child("u2").child("u1").setValue(f2);

        DatabaseReference requestsRef = myRef.child("friendRequests");
        String requestId = requestsRef.push().getKey();
        FriendRequest req = new FriendRequest("u1", "u2", "pending", System.currentTimeMillis());
        requestsRef.child(requestId).setValue(req);

        DatabaseReference imagesRef = myRef.child("images");
        String imageId = imagesRef.push().getKey();
        List<String> receivers = Arrays.asList("u1", "u2");  // danh sách id bạn nhận ảnh
        Image image = new Image(imageId, "https://example.com/photo1.jpg", "Buổi chiều 🌇", System.currentTimeMillis(), "u1", receivers);
        imagesRef.child(imageId).setValue(image);

        DatabaseReference messagesRef = myRef.child("messages").child("u1_u2");
        String msgId = messagesRef.push().getKey();
        Message message = new Message(msgId, "u1", "u2", "Hello 😄", "mixed", System.currentTimeMillis());
        messagesRef.child(msgId).setValue(message);

        DatabaseReference reactionsRef = myRef.child("reactions").child(imageId);
        Reaction reaction = new Reaction("u2", imageId, "❤️", System.currentTimeMillis());
        reactionsRef.child("u2").setValue(reaction);

    }

}