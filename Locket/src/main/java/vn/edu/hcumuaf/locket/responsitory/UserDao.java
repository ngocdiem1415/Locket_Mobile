package vn.edu.hcumuaf.locket.responsitory;

import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import vn.edu.hcumuaf.locket.model.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Repository
public class UserDao {
    private final DatabaseReference dbRef;

    @Qualifier("taskExecutor")
    @Autowired
    private Executor taskExecutor;

    @Autowired
    public UserDao(FirebaseDatabase firebaseDatabase) {
        this.dbRef = firebaseDatabase.getReference("users");
    }

    //Tìm user bằng id
    public CompletableFuture<User> findUserById(String userId) {
        CompletableFuture<User> future = new CompletableFuture<>();

        dbRef.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            User user = snap.getValue(User.class);
                            future.complete(user);
                            return;
                        }
                        future.complete(null); // Không tìm thấy user
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        return future;
    }

    // Tìm kiếm users theo tên hoặc username
    public CompletableFuture<List<User>> searchUsers(String query) {
        CompletableFuture<List<User>> future = new CompletableFuture<>();
        List<User> users = new ArrayList<>();

        if (query == null || query.trim().isEmpty()) {
            future.complete(users);
            return future;
        }

        String searchQuery = query.toLowerCase().trim();

        // Tìm kiếm theo fullName
        dbRef.orderByChild("fullName")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            User user = snap.getValue(User.class);
                            if (user != null && user.getFullName() != null && 
                                user.getFullName().toLowerCase().contains(searchQuery)) {
                                users.add(user);
                            }
                        }

                        // Tìm kiếm theo userName
                        dbRef.orderByChild("userName")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot2) {
                                        for (DataSnapshot snap : snapshot2.getChildren()) {
                                            User user = snap.getValue(User.class);
                                            if (user != null && user.getUserName() != null && 
                                                user.getUserName().toLowerCase().contains(searchQuery)) {
                                                // Kiểm tra xem user đã có trong danh sách chưa
                                                boolean exists = users.stream()
                                                        .anyMatch(u -> u.getUserId().equals(user.getUserId()));
                                                if (!exists) {
                                                    users.add(user);
                                                }
                                            }
                                        }
                                        future.complete(users);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });

        return future;
    }

}
