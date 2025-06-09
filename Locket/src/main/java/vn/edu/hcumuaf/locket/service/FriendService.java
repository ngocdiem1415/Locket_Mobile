package vn.edu.hcumuaf.locket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcumuaf.locket.model.Friend;
import vn.edu.hcumuaf.locket.model.User;
import vn.edu.hcumuaf.locket.model.FriendStatus;
import vn.edu.hcumuaf.locket.repository.FriendRepository;
import vn.edu.hcumuaf.locket.repository.UserRepository;
import vn.edu.hcumuaf.locket.exception.ResourceNotFoundException;
import vn.edu.hcumuaf.locket.exception.FriendRequestException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {
    
    @Autowired
    private FriendRepository friendRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> getFriendsList(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
        List<Friend> friendships = friendRepository.findAllFriendships(user, FriendStatus.ACCEPTED);
        return friendships.stream()
            .map(f -> f.getUser().equals(user) ? f.getFriend() : f.getUser())
            .collect(Collectors.toList());
    }
    
    public List<User> getPendingFriendRequests(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
        List<Friend> pendingRequests = friendRepository.findByFriendAndStatus(user, FriendStatus.PENDING);
        return pendingRequests.stream()
            .map(Friend::getUser)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void sendFriendRequest(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User friend = userRepository.findById(friendId)
            .orElseThrow(() -> new ResourceNotFoundException("Friend not found"));
            
        if (friendRepository.existsByUserAndFriendAndStatus(user, friend, FriendStatus.PENDING) ||
            friendRepository.existsByUserAndFriendAndStatus(friend, user, FriendStatus.PENDING)) {
            throw new FriendRequestException("Friend request already exists");
        }
        
        if (friendRepository.existsByUserAndFriendAndStatus(user, friend, FriendStatus.ACCEPTED) ||
            friendRepository.existsByUserAndFriendAndStatus(friend, user, FriendStatus.ACCEPTED)) {
            throw new FriendRequestException("Users are already friends");
        }
        
        Friend friendRequest = new Friend();
        friendRequest.setUser(user);
        friendRequest.setFriend(friend);
        friendRequest.setStatus(FriendStatus.PENDING);
        friendRepository.save(friendRequest);
    }
    
    @Transactional
    public void acceptFriendRequest(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User friend = userRepository.findById(friendId)
            .orElseThrow(() -> new ResourceNotFoundException("Friend not found"));
            
        Friend friendRequest = friendRepository.findByUserAndFriendAndStatus(friend, user, FriendStatus.PENDING)
            .orElseThrow(() -> new FriendRequestException("Friend request not found"));
            
        friendRequest.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(friendRequest);
    }
    
    @Transactional
    public void rejectFriendRequest(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User friend = userRepository.findById(friendId)
            .orElseThrow(() -> new ResourceNotFoundException("Friend not found"));
            
        Friend friendRequest = friendRepository.findByUserAndFriendAndStatus(friend, user, FriendStatus.PENDING)
            .orElseThrow(() -> new FriendRequestException("Friend request not found"));
            
        friendRequest.setStatus(FriendStatus.REJECTED);
        friendRepository.save(friendRequest);
    }
    
    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User friend = userRepository.findById(friendId)
            .orElseThrow(() -> new ResourceNotFoundException("Friend not found"));
            
        Friend friendship = friendRepository.findFriendship(user, friend)
            .orElseThrow(() -> new FriendRequestException("Friendship not found"));
            
        friendRepository.delete(friendship);
    }
    
    @Transactional
    public void blockUser(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User friend = userRepository.findById(friendId)
            .orElseThrow(() -> new ResourceNotFoundException("Friend not found"));
            
        Friend friendship = friendRepository.findFriendship(user, friend)
            .orElseThrow(() -> new FriendRequestException("Friendship not found"));
            
        friendship.setStatus(FriendStatus.BLOCKED);
        friendRepository.save(friendship);
    }
} 