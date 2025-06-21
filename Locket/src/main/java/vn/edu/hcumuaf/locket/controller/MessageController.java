package vn.edu.hcumuaf.locket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcumuaf.locket.model.Message;
import vn.edu.hcumuaf.locket.service.MessageService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/messages")
public class MessageController {
    @Autowired
    private MessageService messageService;

    @GetMapping("/{userId}")
    public CompletableFuture<List<Message>> getMessagesByUserId(@PathVariable String userId) {
        return messageService.getMessagesByUserId(userId);
    }
    //CompletableFuture dùng để xử lí bất đồng bộ và lập trình phản ứng một cách dễ dàng và mạnh mẽ
}
