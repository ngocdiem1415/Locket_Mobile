//package vn.edu.hcumuaf.locket.config;
//
//import com.google.firebase.database.FirebaseDatabase;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.messaging.SessionConnectedEvent;
//import org.springframework.web.socket.messaging.SessionDisconnectEvent;
//import vn.edu.hcumuaf.locket.model.Message;
//
//import java.util.Map;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class WebSocketEventListener {
//    public SimpMessageSendingOperations messagingTemplate;
//    private FirebaseDatabase database;
//
//
//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
////        String username = database.getReference("users").child(accessor.getSessionId()).toString();
//        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
//        if (sessionAttributes == null) {
//            System.out.println("Can not find session attributes");
//        }
//        String username = (String) sessionAttributes.get("username");
//        if (username == null) {
//            log.warn("Username not set for sessionId: {}", accessor.getSessionId());
//            return;
//        }
//        System.out.println("username: " + username);
//        System.out.println("WebSocket connected: " + username);
//        System.out.println("User connected:" + username);
//        if (username != null) {
//            Message chatMessage = new Message();
//            chatMessage.setType(Message.MessageType.JOIN);
//            chatMessage.setSenderId(username);
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
//        }
//    }
//
//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) accessor.getSessionAttributes().get("username");
//        if (username != null) {
//            log.info("User disconnected: {}", username);
//            var chatMessage = new Message();
//            chatMessage.setType(Message.MessageType.LEAVE);
//            chatMessage.setSenderId(username);
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
//        }
//    }
//}


package vn.edu.hcumuaf.locket.config;

import com.google.firebase.database.FirebaseDatabase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import vn.edu.hcumuaf.locket.model.Message;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messagingTemplate;
    private FirebaseDatabase database;

//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
//        StompHeaderAccessor connectAccessor = StompHeaderAccessor.wrap(accessor.getMessageHeaders().get("simpConnectMessage", org.springframework.messaging.Message.class));
//        Map<String, Object> sessionAttributes = null;
//        String username = null;
//        if (connectAccessor != null && connectAccessor.getSessionAttributes() != null) {
//            sessionAttributes = connectAccessor.getSessionAttributes();
//            username = (String) sessionAttributes.get("username");
//        }
//
//        if (username == null && accessor.getSessionAttributes() != null) {
//            sessionAttributes = accessor.getSessionAttributes();
//            username = (String) sessionAttributes.get("username");
//        }
//
//        if (username == null || username.trim().isEmpty()) {
//            log.warn("Username not found for sessionId: {}. Session attributes: {}",
//                    accessor.getSessionId(), sessionAttributes);
//            return;
//        }
//
//        log.info("WebSocket connected - Username: {}, SessionId: {}", username, accessor.getSessionId());
//
//        Message chatMessage = new Message();
//        chatMessage.setType(Message.MessageType.JOIN);
//        chatMessage.setSenderId(username);
//        messagingTemplate.convertAndSend("/topic/public", chatMessage);
//    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        if (sessionAttributes == null) {
            log.warn("Session attributes are null during disconnect for sessionId: {}", accessor.getSessionId());
            return;
        }

        String username = (String) sessionAttributes.get("username");
        if (username != null && !username.trim().isEmpty()) {
            log.info("User disconnected: {}", username);
            Message chatMessage = new Message();
            chatMessage.setType(Message.MessageType.LEAVE);
            chatMessage.setSenderId(username);
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        } else {
            log.warn("Username not found during disconnect for sessionId: {}", accessor.getSessionId());
        }
    }
}