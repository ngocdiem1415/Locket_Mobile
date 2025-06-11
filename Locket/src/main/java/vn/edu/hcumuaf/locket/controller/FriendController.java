package vn.edu.hcumuaf.locket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcumuaf.locket.model.entity.User;
import vn.edu.hcumuaf.locket.service.FriendService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping("/list/user/{userId}")
    public CompletableFuture<ResponseEntity<List<User>>> getListFriendByUserId(@PathVariable String userId) {
        return friendService.getListFriendByUserId(userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
                });
    }

    @GetMapping("/listFriendId/{userId}")
    public CompletableFuture<ResponseEntity<Set<String>>> getFriendIdsByUserId(@PathVariable String userId) {
        return friendService.getFriendIdsByUserId(userId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
}
