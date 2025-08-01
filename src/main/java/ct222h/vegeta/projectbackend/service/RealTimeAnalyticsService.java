package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.response.DashboardResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@EnableAsync
public class RealTimeAnalyticsService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AdvancedAnalyticsService advancedAnalyticsService;

    // Track active WebSocket connections
    private final AtomicInteger activeConnections = new AtomicInteger(0);
    private final ConcurrentHashMap<String, Long> userSessions = new ConcurrentHashMap<>();

    // Broadcast real-time metrics every 30 seconds
    @Scheduled(fixedRate = 30000)
    public void broadcastRealTimeMetrics() {
        try {
            DashboardResponse.RealTimeMetrics metrics = advancedAnalyticsService.getRealTimeMetrics();
            
            // Update active users count with actual WebSocket connections
            metrics.setActiveUsersOnline(activeConnections.get());
            
            messagingTemplate.convertAndSend("/topic/realtime-metrics", metrics);
        } catch (Exception e) {
            // Log error but don't stop the scheduler
            System.err.println("Error broadcasting real-time metrics: " + e.getMessage());
        }
    }

    // Broadcast order updates
    @Async
    public void broadcastOrderUpdate(String eventType, Object orderData) {
        try {
            messagingTemplate.convertAndSend("/topic/order-updates", 
                new OrderUpdateEvent(eventType, orderData));
        } catch (Exception e) {
            System.err.println("Error broadcasting order update: " + e.getMessage());
        }
    }

    // Broadcast revenue updates
    @Async
    public void broadcastRevenueUpdate(Double newRevenue, String period) {
        try {
            messagingTemplate.convertAndSend("/topic/revenue-updates", 
                new RevenueUpdateEvent(newRevenue, period));
        } catch (Exception e) {
            System.err.println("Error broadcasting revenue update: " + e.getMessage());
        }
    }

    // Track user connections
    public void addUserConnection(String sessionId) {
        userSessions.put(sessionId, System.currentTimeMillis());
        activeConnections.incrementAndGet();
        
        // Broadcast updated connection count
        broadcastConnectionCount();
    }

    public void removeUserConnection(String sessionId) {
        userSessions.remove(sessionId);
        activeConnections.decrementAndGet();
        
        // Broadcast updated connection count
        broadcastConnectionCount();
    }

    private void broadcastConnectionCount() {
        try {
            messagingTemplate.convertAndSend("/topic/active-users", 
                new ActiveUsersEvent(activeConnections.get()));
        } catch (Exception e) {
            System.err.println("Error broadcasting connection count: " + e.getMessage());
        }
    }

    // Clean up stale connections every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void cleanupStaleConnections() {
        long currentTime = System.currentTimeMillis();
        long timeout = 600000; // 10 minutes timeout
        
        userSessions.entrySet().removeIf(entry -> {
            if (currentTime - entry.getValue() > timeout) {
                activeConnections.decrementAndGet();
                return true;
            }
            return false;
        });
    }

    public int getActiveConnectionsCount() {
        return activeConnections.get();
    }

    // Event classes for WebSocket messages
    public static class OrderUpdateEvent {
        private String eventType;
        private Object data;
        private long timestamp;

        public OrderUpdateEvent(String eventType, Object data) {
            this.eventType = eventType;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters and setters
        public String getEventType() { return eventType; }
        public void setEventType(String eventType) { this.eventType = eventType; }

        public Object getData() { return data; }
        public void setData(Object data) { this.data = data; }

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    public static class RevenueUpdateEvent {
        private Double revenue;
        private String period;
        private long timestamp;

        public RevenueUpdateEvent(Double revenue, String period) {
            this.revenue = revenue;
            this.period = period;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters and setters
        public Double getRevenue() { return revenue; }
        public void setRevenue(Double revenue) { this.revenue = revenue; }

        public String getPeriod() { return period; }
        public void setPeriod(String period) { this.period = period; }

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }

    public static class ActiveUsersEvent {
        private Integer count;
        private long timestamp;

        public ActiveUsersEvent(Integer count) {
            this.count = count;
            this.timestamp = System.currentTimeMillis();
        }

        // Getters and setters
        public Integer getCount() { return count; }
        public void setCount(Integer count) { this.count = count; }

        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
