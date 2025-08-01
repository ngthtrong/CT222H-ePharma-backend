package ct222h.vegeta.projectbackend.websocket;

import ct222h.vegeta.projectbackend.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationWebSocketHandler.class);

    @Autowired
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            // Extract and validate JWT token from query parameters
            String token = extractTokenFromQuery(session.getUri());
            if (token == null || !isValidToken(token)) {
                logger.warn("Invalid or missing JWT token for notification WebSocket connection");
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid authentication"));
                return;
            }

            String userEmail = jwtUtil.extractEmail(token);
            sessions.put(session.getId(), session);
            
            logger.info("Notification WebSocket connection established for user: {}", userEmail);
            
            // Send welcome message
            Map<String, Object> welcome = Map.of(
                "type", "WELCOME",
                "message", "Connected to notifications",
                "timestamp", System.currentTimeMillis()
            );
            
            String jsonMessage = objectMapper.writeValueAsString(welcome);
            session.sendMessage(new TextMessage(jsonMessage));
            
        } catch (Exception e) {
            logger.error("Error establishing notification WebSocket connection: {}", e.getMessage(), e);
            session.close(CloseStatus.SERVER_ERROR.withReason("Connection error"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        logger.info("Notification WebSocket connection closed: {}", status.getReason());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("Notification WebSocket transport error: {}", exception.getMessage(), exception);
        sessions.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            logger.debug("Received notification WebSocket message: {}", payload);
            
            // Echo back for now
            session.sendMessage(new TextMessage("{\"type\":\"ACK\",\"timestamp\":" + System.currentTimeMillis() + "}"));
            
        } catch (Exception e) {
            logger.error("Error handling notification WebSocket message: {}", e.getMessage(), e);
        }
    }

    private String extractTokenFromQuery(URI uri) {
        if (uri == null || uri.getQuery() == null) {
            return null;
        }
        
        String query = uri.getQuery();
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair.length == 2 && "token".equals(pair[0])) {
                return pair[1];
            }
        }
        return null;
    }

    private boolean isValidToken(String token) {
        try {
            String email = jwtUtil.extractEmail(token);
            return email != null && !email.trim().isEmpty();
        } catch (Exception e) {
            logger.warn("Invalid JWT token for notifications: {}", e.getMessage());
            return false;
        }
    }

    public void broadcastNotification(Map<String, Object> notification) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(notification);
            sessions.values().forEach(session -> {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(new TextMessage(jsonMessage));
                    } catch (Exception e) {
                        logger.error("Error sending notification to session: {}", e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Error broadcasting notification: {}", e.getMessage(), e);
        }
    }
}
