package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.response.ReportResponse;
import ct222h.vegeta.projectbackend.model.Order;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.OrderRepository;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    
    public ReportService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }
    
    public ReportResponse.RevenueReportResponse getRevenueReport(Date startDate, Date endDate, String reportType) {
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        List<Order> completedOrders = orders.stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()))
                .collect(Collectors.toList());
        
        Double totalRevenue = completedOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
        
        Integer totalOrders = completedOrders.size();
        
        // Calculate top products performance
        Map<String, ProductPerformanceData> productPerformanceMap = new HashMap<>();
        
        for (Order order : completedOrders) {
            for (Order.OrderItem item : order.getItems()) {
                String productId = item.getProductId();
                ProductPerformanceData data = productPerformanceMap.getOrDefault(productId, 
                    new ProductPerformanceData(productId, item.getProductName(), 0, 0.0));
                
                data.quantitySold += item.getQuantity();
                data.revenue += item.getItemTotal();
                
                productPerformanceMap.put(productId, data);
            }
        }
        
        List<ReportResponse.ProductPerformanceResponse> topProducts = productPerformanceMap.values().stream()
                .sorted((a, b) -> Double.compare(b.revenue, a.revenue))
                .limit(10)
                .map(data -> new ReportResponse.ProductPerformanceResponse(
                    data.productId, data.productName, data.quantitySold, data.revenue))
                .collect(Collectors.toList());
        
        return new ReportResponse.RevenueReportResponse(
            reportType, startDate, endDate, totalRevenue, totalOrders, topProducts, new Date()
        );
    }
    
    public List<ReportResponse.ProductPerformanceResponse> getProductPerformanceReport(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        List<Order> completedOrders = orders.stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()))
                .collect(Collectors.toList());
        
        Map<String, ProductPerformanceData> productPerformanceMap = new HashMap<>();
        
        for (Order order : completedOrders) {
            for (Order.OrderItem item : order.getItems()) {
                String productId = item.getProductId();
                ProductPerformanceData data = productPerformanceMap.getOrDefault(productId, 
                    new ProductPerformanceData(productId, item.getProductName(), 0, 0.0));
                
                data.quantitySold += item.getQuantity();
                data.revenue += item.getItemTotal();
                
                productPerformanceMap.put(productId, data);
            }
        }
        
        return productPerformanceMap.values().stream()
                .sorted((a, b) -> Integer.compare(b.quantitySold, a.quantitySold))
                .map(data -> new ReportResponse.ProductPerformanceResponse(
                    data.productId, data.productName, data.quantitySold, data.revenue))
                .collect(Collectors.toList());
    }
    
    public ReportResponse.OrderStatisticsResponse getOrderStatistics(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        
        Integer totalOrders = orders.size();
        Integer completedOrders = (int) orders.stream().filter(o -> "COMPLETED".equals(o.getStatus())).count();
        Integer cancelledOrders = (int) orders.stream().filter(o -> "CANCELLED".equals(o.getStatus())).count();
        Integer pendingOrders = (int) orders.stream().filter(o -> "PENDING".equals(o.getStatus())).count();
        
        Double averageOrderValue = orders.stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalAmount)
                .average()
                .orElse(0.0);
        
        return new ReportResponse.OrderStatisticsResponse(
            startDate, endDate, totalOrders, completedOrders, cancelledOrders, 
            pendingOrders, averageOrderValue, new Date()
        );
    }
    
    public ReportResponse.UserAnalyticsResponse getUserAnalytics(Date startDate, Date endDate) {
        List<User> allUsers = userRepository.findAll();
        Integer totalUsers = allUsers.size();
        
        // Count new users in the period
        Integer newUsers = (int) allUsers.stream()
                .filter(user -> user.getCreatedAt() != null && 
                    !user.getCreatedAt().before(startDate) && 
                    !user.getCreatedAt().after(endDate))
                .count();
        
        // Count active users (users who placed orders in the period)
        List<Order> ordersInPeriod = orderRepository.findOrdersByDateRange(startDate, endDate);
        Set<String> activeUserIds = ordersInPeriod.stream()
                .map(Order::getUserId)
                .collect(Collectors.toSet());
        Integer activeUsers = activeUserIds.size();
        
        return new ReportResponse.UserAnalyticsResponse(
            startDate, endDate, totalUsers, newUsers, activeUsers, new Date()
        );
    }
    
    // Helper class for product performance calculation
    private static class ProductPerformanceData {
        String productId;
        String productName;
        Integer quantitySold;
        Double revenue;
        
        ProductPerformanceData(String productId, String productName, Integer quantitySold, Double revenue) {
            this.productId = productId;
            this.productName = productName;
            this.quantitySold = quantitySold;
            this.revenue = revenue;
        }
    }
}
