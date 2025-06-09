package vn.edu.hcumuaf.locket.responsitory;

import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import vn.edu.hcumuaf.locket.model.entity.User;

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

}
