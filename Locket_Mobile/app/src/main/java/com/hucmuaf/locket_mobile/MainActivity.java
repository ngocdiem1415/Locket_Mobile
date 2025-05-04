package com.hucmuaf.locket_mobile;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
        User user1 = new User("1", "tuvo", "Võ Thị Cẩm Tú", "tuvo@example.com", "0123456789", "https://linktoavatar.com","123");

        Friend friend1 = new Friend("1", "tuvo");

        Message message1 = new Message("text", "Hello!", "1");

        Activity activity1 = new Activity("1", "image1", "1", "activity_icon.png");

        Image image1 = new Image("image1", "https://linktoimage.com", "A beautiful image");

        // Đẩy lên Firebase Realtime Database
        myRef.child("users").child(user1.getId()).setValue(user1);

        myRef.child("friends").child(user1.getId()).child(friend1.getId()).setValue(friend1);

        myRef.child("messages").child("message1").setValue(message1);

        myRef.child("activities").child("activity1").setValue(activity1);

        myRef.child("images").child(image1.getId()).setValue(image1);
    }
}