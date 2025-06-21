package vn.edu.hcumuaf.locket.controller;

import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.hcumuaf.locket.model.Message;
import vn.edu.hcumuaf.locket.service.MessageService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequestMapping("/api/messages")
public class ChatController {
    private final MessageService service;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(MessageService service, SimpMessagingTemplate messagingTemplate) {
        this.service = service;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.register")
    public void register(@Payload Message chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String senderId = chatMessage.getSenderId();
        log.info("Registering user with senderId: {}, sessionId: {}", senderId, headerAccessor.getSessionId());

        if (senderId != null && !senderId.trim().isEmpty()) {
            headerAccessor.getSessionAttributes().put("username", senderId);
            log.info("Session attributes set: {}", headerAccessor.getSessionAttributes());

            Message joinMessage = new Message();
            joinMessage.setType(Message.MessageType.JOIN);
            joinMessage.setSenderId(senderId);
            messagingTemplate.convertAndSend("/topic/public", joinMessage);

            messagingTemplate.convertAndSendToUser(
                    senderId,
                    "/topic/messages",
                    chatMessage
            );
        } else {
            log.warn("Invalid senderId received in /chat.register: {}", senderId);
        }
    }

    //    @SendTo("/topic/public")
//    @SendToUser("/topic/{receiverId}")
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Message chatMessage) {
        try {
            service.saveMessage(chatMessage);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getReceiverId(),
                    "/topic/messages",
                    chatMessage
            );
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getSenderId(),
                    "/topic/messages",
                    chatMessage
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
    }

    @GetMapping("topic/{senderId}")
    public CompletableFuture<List<Message>> getMessage(@PathVariable("senderId") String senderId) {
        return service.getMessagesByUserId(senderId);
    }
}