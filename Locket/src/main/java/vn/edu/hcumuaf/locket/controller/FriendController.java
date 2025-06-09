package vn.edu.hcumuaf.locket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcumuaf.locket.model.User;
import vn.edu.hcumuaf.locket.service.FriendService;
import vn.edu.hcumuaf.locket.exception.ResourceNotFoundException;
import vn.edu.hcumuaf.locket.exception.FriendRequestException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friends")
@CrossOrigin(origins = "*")
public class FriendController {
    
    @Autowired
    private FriendService friendService;
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<User>> getFriendsList(@PathVariable Long userId) {
        try {
            List<User> friends = friendService.getFriendsList(userId);
            return ResponseEntity.ok(friends);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{userId}/pending")
    public ResponseEntity<List<User>> getPendingFriendRequests(@PathVariable Long userId) {
        try {
            List<User> pendingRequests = friendService.getPendingFriendRequests(userId);
            return ResponseEntity.ok(pendingRequests);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Long friendId = request.get("friendId");
            friendService.sendFriendRequest(userId, friendId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Long friendId = request.get("friendId");
            friendService.acceptFriendRequest(userId, friendId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Long friendId = request.get("friendId");
            friendService.rejectFriendRequest(userId, friendId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<?> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        try {
            friendService.removeFriend(userId, friendId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/block")
    public ResponseEntity<?> blockUser(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            Long friendId = request.get("friendId");
            friendService.blockUser(userId, friendId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (FriendRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 