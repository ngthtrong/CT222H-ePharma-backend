package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.service.RealTimeAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class AnalyticsWebSocketController {

    @Autowired
    private RealTimeAnalyticsService realTimeAnalyticsService;

    @MessageMapping("/analytics/connect")
    @SendTo("/topic/welcome")
    public String handleConnection(SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        realTimeAnalyticsService.addUserConnection(sessionId);
        return "Connected to real-time analytics";
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        // Handle WebSocket connection
        String sessionId = event.getMessage().getHeaders().get("simpSessionId", String.class);
        if (sessionId != null) {
            realTimeAnalyticsService.addUserConnection(sessionId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // Handle WebSocket disconnection
        String sessionId = event.getSessionId();
        if (sessionId != null) {
            realTimeAnalyticsService.removeUserConnection(sessionId);
        }
    }

    @MessageMapping("/analytics/ping")
    @SendTo("/topic/pong")
    public String handlePing() {
        return "pong";
    }
}
