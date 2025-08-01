package ct222h.vegeta.projectbackend.websocket;

import ct222h.vegeta.projectbackend.security.JwtUtil;
import ct222h.vegeta.projectbackend.service.DashboardService;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class DashboardWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(DashboardWebSocketHandler.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private DashboardService dashboardService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        try {
            logger.info("WebSocket connection attempt from: {}", session.getUri());
            
            // Extract and validate JWT token from query parameters
            String token = extractTokenFromQuery(session.getUri());
            logger.info("Extracted token: {}", token != null ? "present" : "missing");
            
            if (token == null) {
                logger.warn("No JWT token provided for WebSocket connection");
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing authentication token"));
                return;
            }
            
            if (!isValidToken(token)) {
                logger.warn("Invalid JWT token for WebSocket connection");
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid authentication"));
                return;
            }

            String userEmail = jwtUtil.extractEmail(token);
            sessions.put(session.getId(), session);
            
            logger.info("Dashboard WebSocket connection established for user: {}", userEmail);
            
            // Send initial dashboard data
            sendDashboardUpdate(session);
            
            // Start periodic updates for this session
            startPeriodicUpdates(session);
            
        } catch (Exception e) {
            logger.error("Error establishing WebSocket connection: {}", e.getMessage(), e);
            session.close(CloseStatus.SERVER_ERROR.withReason("Connection error"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        logger.info("Dashboard WebSocket connection closed: {}", status.getReason());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("WebSocket transport error: {}", exception.getMessage(), exception);
        sessions.remove(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            logger.debug("Received WebSocket message: {}", payload);
            
            // Handle different message types
            Map<String, Object> messageData = objectMapper.readValue(payload, Map.class);
            String type = (String) messageData.get("type");
            
            switch (type) {
                case "REQUEST_UPDATE":
                    sendDashboardUpdate(session);
                    break;
                case "PING":
                    session.sendMessage(new TextMessage("{\"type\":\"PONG\",\"timestamp\":" + System.currentTimeMillis() + "}"));
                    break;
                default:
                    logger.warn("Unknown message type: {}", type);
            }
            
        } catch (Exception e) {
            logger.error("Error handling WebSocket message: {}", e.getMessage(), e);
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
            logger.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    private void sendDashboardUpdate(WebSocketSession session) {
        try {
            var stats = dashboardService.getDashboardStats();
            Map<String, Object> update = Map.of(
                "type", "DASHBOARD_UPDATE",
                "timestamp", System.currentTimeMillis(),
                "data", stats
            );
            
            String jsonMessage = objectMapper.writeValueAsString(update);
            session.sendMessage(new TextMessage(jsonMessage));
            
        } catch (Exception e) {
            logger.error("Error sending dashboard update: {}", e.getMessage(), e);
        }
    }

    private void startPeriodicUpdates(WebSocketSession session) {
        scheduler.scheduleAtFixedRate(() -> {
            if (session.isOpen()) {
                sendDashboardUpdate(session);
            }
        }, 30, 30, TimeUnit.SECONDS); // Update every 30 seconds
    }

    public void broadcastUpdate() {
        sessions.values().forEach(session -> {
            if (session.isOpen()) {
                sendDashboardUpdate(session);
            }
        });
    }
}
