package ct222h.vegeta.projectbackend.config;

import ct222h.vegeta.projectbackend.websocket.DashboardWebSocketHandler;
import ct222h.vegeta.projectbackend.websocket.NotificationWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket  
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private DashboardWebSocketHandler dashboardHandler;

    @Autowired
    private NotificationWebSocketHandler notificationHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Simple WebSocket endpoints without SockJS first
        registry.addHandler(dashboardHandler, "/ws/analytics")
                .setAllowedOrigins("*")  // Use setAllowedOrigins instead of patterns for simplicity
                .setHandshakeHandler(new org.springframework.web.socket.server.support.DefaultHandshakeHandler());

        registry.addHandler(notificationHandler, "/ws/notifications") 
                .setAllowedOrigins("*")
                .setHandshakeHandler(new org.springframework.web.socket.server.support.DefaultHandshakeHandler());
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
