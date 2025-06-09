package vn.edu.hcumuaf.locket.responsitory;

import com.google.api.core.ApiFuture;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import vn.edu.hcumuaf.locket.model.entity.FriendRequest;
import vn.edu.hcumuaf.locket.model.FriendRequestStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

@Repository
public class FriendRequestDao {
    private final DatabaseReference dbRef;

    @Autowired
    public FriendRequestDao(FirebaseDatabase firebaseDatabase) {
        this.dbRef = firebaseDatabase.getReference("friendRequests");
    }

    //Tìm danh sách bạn bè theo user id
    public CompletableFuture<Set<String>> findListFriendByUserID(String userID) {
        CompletableFuture<Set<String>> senderFuture = new CompletableFuture<>();
        CompletableFuture<Set<String>> receiverFuture = new CompletableFuture<>();


        dbRef.orderByChild("senderId").equalTo(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Set<String> senderSet  = ConcurrentHashMap.newKeySet();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FriendRequest fq = parseFriendRequest(snap);
                            if (fq != null && fq.getStatus().equals(FriendRequestStatus.ACCEPTED)) {
                                senderSet.add(fq.getReceiverId());
                            }
                        }
                        System.out.println("Sender query done");
                        senderFuture.complete(senderSet);

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println("Sender query failed: " + error.getMessage());
                        senderFuture.completeExceptionally(new RuntimeException(error.getMessage()));

                    }
                });
        dbRef.orderByChild("receiverId").equalTo(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Set<String> receiverSet  = ConcurrentHashMap.newKeySet();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FriendRequest fq = parseFriendRequest(snap);
                            if (fq != null && fq.getStatus().equals(FriendRequestStatus.ACCEPTED)) {
                                receiverSet .add(fq.getSenderId());
                            }
                        }
                        System.out.println("Receiver query done");
                        receiverFuture.complete(receiverSet );

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println("Receiver query failed: " + error.getMessage());
                        receiverFuture.completeExceptionally(new RuntimeException(error.getMessage()));

                    }
                });
        return senderFuture.thenCombine(receiverFuture, (senderSet, receiverSet) -> {
            senderSet.addAll(receiverSet);
            return senderSet;
        });
    }

    // Lưu friend request mới
    public CompletableFuture<Boolean> saveFriendRequest(FriendRequest friendRequest) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        dbRef.child(friendRequest.getFriendRequestId()).setValue(friendRequest, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    System.err.println("Error saving friend request: " + error.getMessage());
                    future.complete(false);
                } else {
                    future.complete(true);
                }
            }
        });
        
        return future;
    }

    // Tìm friend request theo ID
    public CompletableFuture<FriendRequest> findFriendRequestById(String requestId) {
        CompletableFuture<FriendRequest> future = new CompletableFuture<>();
        
        dbRef.child(requestId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    FriendRequest request = parseFriendRequest(snapshot);
                    future.complete(request);
                } else {
                    future.complete(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error finding friend request: " + error.getMessage());
                future.completeExceptionally(new RuntimeException(error.getMessage()));
            }
        });
        
        return future;
    }

    // Cập nhật friend request
    public CompletableFuture<Boolean> updateFriendRequest(FriendRequest friendRequest) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        dbRef.child(friendRequest.getFriendRequestId()).setValue(friendRequest, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    System.err.println("Error updating friend request: " + error.getMessage());
                    future.complete(false);
                } else {
                    future.complete(true);
                }
            }
        });
        
        return future;
    }

    // Xóa friend request
    public CompletableFuture<Boolean> deleteFriendRequest(String requestId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        
        dbRef.child(requestId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    System.err.println("Error deleting friend request: " + error.getMessage());
                    future.complete(false);
                } else {
                    future.complete(true);
                }
            }
        });
        
        return future;
    }

    // Tìm friend requests giữa hai user
    public CompletableFuture<List<FriendRequest>> findFriendRequestsBetweenUsers(String userId1, String userId2) {
        CompletableFuture<List<FriendRequest>> future = new CompletableFuture<>();
        List<FriendRequest> requests = new ArrayList<>();
        
        // Tìm requests từ user1 đến user2
        dbRef.orderByChild("senderId").equalTo(userId1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FriendRequest request = parseFriendRequest(snap);
                            if (request != null && request.getReceiverId().equals(userId2)) {
                                requests.add(request);
                            }
                        }
                        
                        // Tìm requests từ user2 đến user1
                        dbRef.orderByChild("senderId").equalTo(userId2)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot2) {
                                        for (DataSnapshot snap : snapshot2.getChildren()) {
                                            FriendRequest request = parseFriendRequest(snap);
                                            if (request != null && request.getReceiverId().equals(userId1)) {
                                                requests.add(request);
                                            }
                                        }
                                        future.complete(requests);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        System.err.println("Error finding friend requests: " + error.getMessage());
                                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Error finding friend requests: " + error.getMessage());
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });
        
        return future;
    }

    // Tìm pending requests theo receiver ID
    public CompletableFuture<List<FriendRequest>> findPendingRequestsByReceiverId(String receiverId) {
        CompletableFuture<List<FriendRequest>> future = new CompletableFuture<>();
        List<FriendRequest> requests = new ArrayList<>();
        
        dbRef.orderByChild("receiverId").equalTo(receiverId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            FriendRequest request = parseFriendRequest(snap);
                            if (request != null && request.getStatus() == FriendRequestStatus.PENDING) {
                                requests.add(request);
                            }
                        }
                        future.complete(requests);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Error finding pending requests: " + error.getMessage());
                        future.completeExceptionally(new RuntimeException(error.getMessage()));
                    }
                });
        
        return future;
    }

    //Chuyển enum ở bean sang String khi parse từ DataSnapshot
    private FriendRequest parseFriendRequest(DataSnapshot snap) {
        try {
            String statusStr = snap.child("status").getValue(String.class);
            if (statusStr == null) return null;

            FriendRequestStatus status = FriendRequestStatus.valueOf(statusStr.toUpperCase());
            String friendRequestId = snap.child("friendRequestId").getValue(String.class);
            String senderId = snap.child("senderId").getValue(String.class);
            String receiverId = snap.child("receiverId").getValue(String.class);
            Long timestamp = snap.child("timestamp").getValue(Long.class);

            if (senderId == null || receiverId == null || timestamp == null) return null;

            return new FriendRequest(friendRequestId, senderId, receiverId, status, timestamp);
        } catch (Exception e) {
            System.err.println("Error parsing friend request: " + e.getMessage());
            return null;
        }
    }

}
