package vn.edu.hcumuaf.locket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        String query = request.getURI().getQuery();
        String username = extractUsernameFromQuery(query);

        if (username != null && !username.trim().isEmpty()) {
            attributes.put("username", username);
            log.info("Setting username in session attributes: {}", username);
            return true;
        } else {
            log.warn("No username provided in WebSocket handshake");
            return true;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("WebSocket handshake failed", exception);
        } else {
            log.info("WebSocket handshake completed successfully");
        }
    }

    private String extractUsernameFromQuery(String query) {
        if (query == null || query.isEmpty()) {
            return null;
        }

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && "username".equals(keyValue[0])) {
                try {
                    return java.net.URLDecoder.decode(keyValue[1], "UTF-8");
                } catch (Exception e) {
                    log.error("Error decoding username", e);
                    return keyValue[1];
                }
            }
        }
        return null;
    }
}