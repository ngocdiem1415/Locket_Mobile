package vn.edu.hcumuaf.locket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.HashMap;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketHandshakeInterceptor handshakeInterceptor;

    public WebSocketConfig(WebSocketHandshakeInterceptor handshakeInterceptor) {
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .addInterceptors(handshakeInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    if (accessor.getSessionAttributes() == null) {
                        accessor.setSessionAttributes(new HashMap<>());
                    }

                    String username = extractUsernameFromHeaders(accessor);
                    if (username != null && !username.trim().isEmpty()) {
                        accessor.getSessionAttributes().put("username", username);
                        System.out.println("Username set in session attributes: " + username);
                    }

                    System.out.println("WebSocket client connected: " + accessor.getSessionId());
                }
                return message;
            }

            private String extractUsernameFromHeaders(StompHeaderAccessor accessor) {
                List<String> usernameHeaders = accessor.getNativeHeader("username");
                if (usernameHeaders != null && !usernameHeaders.isEmpty()) {
                    return usernameHeaders.get(0);
                }

                List<String> loginHeaders = accessor.getNativeHeader("login");
                if (loginHeaders != null && !loginHeaders.isEmpty()) {
                    return loginHeaders.get(0);
                }

                return null;
            }
        });
    }
}