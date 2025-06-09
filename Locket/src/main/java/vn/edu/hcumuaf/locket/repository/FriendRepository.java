package vn.edu.hcumuaf.locket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.edu.hcumuaf.locket.model.Friend;
import vn.edu.hcumuaf.locket.model.User;
import vn.edu.hcumuaf.locket.model.FriendStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findByUserAndStatus(User user, FriendStatus status);

    List<Friend> findByFriendAndStatus(User friend, FriendStatus status);

    @Query("SELECT f FROM Friend f WHERE (f.user = ?1 AND f.friend = ?2) OR (f.user = ?2 AND f.friend = ?1)")
    Optional<Friend> findFriendship(User user1, User user2);

    @Query("SELECT f FROM Friend f WHERE (f.user = ?1 OR f.friend = ?1) AND f.status = ?2")
    List<Friend> findAllFriendships(User user, FriendStatus status);

    boolean existsByUserAndFriendAndStatus(User user, User friend, FriendStatus status);
} 