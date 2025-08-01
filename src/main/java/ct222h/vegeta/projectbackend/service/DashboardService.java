package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.response.DashboardResponse;
import ct222h.vegeta.projectbackend.model.Order;
import ct222h.vegeta.projectbackend.repository.OrderRepository;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import ct222h.vegeta.projectbackend.constants.OrderConstants;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private volatile Date lastUpdated = new Date();

    public DashboardResponse.DashboardStatsResponse getDashboardStats() {
        log.info("Fetching dashboard statistics");
        
        try {
            // Tính toán thời gian
            Instant now = Instant.now();
            Instant thirtyDaysAgo = now.minus(30, ChronoUnit.DAYS);
            Instant previousPeriodStart = thirtyDaysAgo.minus(30, ChronoUnit.DAYS);

            // Lấy orders cho 30 ngày gần nhất và 30 ngày trước đó
            List<Order> currentPeriodOrders = orderRepository.findOrdersByCreatedAtBetween(thirtyDaysAgo, now);
            List<Order> previousPeriodOrders = orderRepository.findOrdersByCreatedAtBetween(previousPeriodStart, thirtyDaysAgo);

            // Tính toán tổng doanh thu
            double totalRevenue = calculateTotalRevenue(currentPeriodOrders);
            double previousRevenue = calculateTotalRevenue(previousPeriodOrders);
            double revenueGrowth = calculateGrowthPercentage(totalRevenue, previousRevenue);

            // Tính toán tổng đơn hàng
            int totalOrders = currentPeriodOrders.size();
            int previousOrderCount = previousPeriodOrders.size();

            // Tính toán giá trị trung bình mỗi đơn
            double avgOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0;

            // Tính toán tỷ lệ chuyển đổi (conversion rate)
            long totalUsers = userRepository.count();
            double conversionRate = totalUsers > 0 ? (double) totalOrders / totalUsers * 100 : 0;

            // Tính toán xu hướng doanh thu 7 ngày
            List<DashboardResponse.DailyRevenueResponse> revenueChart = calculateRevenueChart(now);

            // Performance theo category
            List<DashboardResponse.CategoryPerformanceResponse> categoryPerformance = 
                calculateCategoryPerformance(currentPeriodOrders);

            // Customer segments
            DashboardResponse.CustomerSegmentResponse customerSegments = 
                calculateCustomerSegments();

            // Top products
            List<DashboardResponse.TopProductResponse> topProducts = 
                calculateTopProducts(currentPeriodOrders);

            // Recent orders
            List<DashboardResponse.RecentOrderResponse> recentOrders = 
                getRecentOrders();

            // Cập nhật thời gian
            lastUpdated = new Date();

            return new DashboardResponse.DashboardStatsResponse(
                totalRevenue,
                revenueGrowth,
                totalOrders,
                avgOrderValue,
                conversionRate,
                revenueChart,
                categoryPerformance,
                customerSegments,
                topProducts,
                recentOrders,
                lastUpdated
            );

        } catch (Exception e) {
            log.error("Error calculating dashboard stats", e);
            throw new RuntimeException("Failed to calculate dashboard statistics", e);
        }
    }

    private double calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
            .filter(order ->OrderConstants.STATUS_COMPLETED.equals(order.getStatus()))
            .mapToDouble(Order::getTotalAmount)
            .sum();
    }

    private double calculateGrowthPercentage(double current, double previous) {
        if (previous == 0) {
            return current > 0 ? 100 : 0;
        }
        return ((current - previous) / previous) * 100;
    }

    private List<DashboardResponse.DailyRevenueResponse> calculateRevenueChart(Instant now) {
        List<DashboardResponse.DailyRevenueResponse> chart = new ArrayList<>();
        
        for (int i = 6; i >= 0; i--) {
            Instant dayStart = now.minus(i, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS);
            Instant dayEnd = dayStart.plus(1, ChronoUnit.DAYS);
            
            List<Order> dayOrders = orderRepository.findOrdersByCreatedAtBetween(dayStart, dayEnd);
            double revenue = calculateTotalRevenue(dayOrders);
            
            chart.add(new DashboardResponse.DailyRevenueResponse(
                dayStart.toString().substring(0, 10), // YYYY-MM-DD format
                revenue
            ));
        }
        
        return chart;
    }

    private List<DashboardResponse.CategoryPerformanceResponse> calculateCategoryPerformance(List<Order> orders) {
        Map<String, Double> categoryRevenue = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();

        orders.stream()
            .filter(order ->OrderConstants.STATUS_COMPLETED.equals(order.getStatus()))
            .forEach(order -> {
                order.getItems().forEach(item -> {
                    try {
                        String categoryName = "Unknown Category"; // Simplified for now
                        
                        double itemRevenue = item.getPriceAtPurchase() * item.getQuantity();
                        categoryRevenue.merge(categoryName, itemRevenue, Double::sum);
                        categoryCount.merge(categoryName, item.getQuantity(), Integer::sum);
                    } catch (Exception e) {
                        log.warn("Error processing category performance for item: {}", item.getProductId(), e);
                    }
                });
            });

        return categoryRevenue.entrySet().stream()
            .map(entry -> new DashboardResponse.CategoryPerformanceResponse(
                entry.getKey(),
                categoryCount.getOrDefault(entry.getKey(), 0),
                entry.getValue()
            ))
            .sorted((a, b) -> Double.compare(b.getRevenue(), a.getRevenue()))
            .limit(10)
            .collect(Collectors.toList());
    }

    private DashboardResponse.CustomerSegmentResponse calculateCustomerSegments() {
        try {
            List<Order> allOrders = orderRepository.findAll();
            Map<String, List<Order>> userOrders = allOrders.stream()
                .collect(Collectors.groupingBy(Order::getUserId));

            int highValue = 0;
            int mediumValue = 0;
            int lowValue = 0;

            for (List<Order> orders : userOrders.values()) {
                int orderCount = orders.size();
                if (orderCount >= 10) {
                    highValue++;
                } else if (orderCount >= 5) {
                    mediumValue++;
                } else {
                    lowValue++;
                }
            }
            
            return new DashboardResponse.CustomerSegmentResponse(highValue, mediumValue, lowValue);
        } catch (Exception e) {
            log.warn("Error calculating customer segments", e);
            return new DashboardResponse.CustomerSegmentResponse(0, 0, 0);
        }
    }

    private List<DashboardResponse.TopProductResponse> calculateTopProducts(List<Order> orders) {
        Map<String, Integer> productSales = new HashMap<>();
        Map<String, Double> productRevenue = new HashMap<>();
        Map<String, String> productNames = new HashMap<>();

        orders.stream()
            .filter(order ->OrderConstants.STATUS_COMPLETED.equals(order.getStatus()))
            .forEach(order -> {
                order.getItems().forEach(item -> {
                    String productId = item.getProductId();
                    productSales.merge(productId, item.getQuantity(), Integer::sum);
                    productRevenue.merge(productId, item.getPriceAtPurchase() * item.getQuantity(), Double::sum);
                    productNames.put(productId, item.getProductName());
                });
            });

        return productSales.entrySet().stream()
            .map(entry -> {
                String productId = entry.getKey();
                return new DashboardResponse.TopProductResponse(
                    productId,
                    productNames.getOrDefault(productId, "Unknown Product"),
                    null, // productImage - simplified for now
                    entry.getValue(),
                    productRevenue.getOrDefault(productId, 0.0),
                    null // categoryName - simplified for now
                );
            })
            .sorted((a, b) -> Integer.compare(b.getQuantitySold(), a.getQuantitySold()))
            .limit(10)
            .collect(Collectors.toList());
    }

    public List<DashboardResponse.RecentOrderResponse> getRecentOrders() {
        try {
            return orderRepository.findTop10ByOrderByCreatedAtDesc()
                .stream()
                .map(order -> {
                    try {
                       String customerName = userRepository.findById(order.getUserId())
                            .map(user -> user.getFullName())
                            .orElse("Unknown Customer"); // Simplified for now

                        return new DashboardResponse.RecentOrderResponse(
                            order.getId(),
                            order.getOrderCode(),
                            customerName,
                            order.getTotalAmount(),
                            order.getStatus(),
                            Date.from(order.getCreatedAt())
                        );
                    } catch (Exception e) {
                        log.warn("Error processing recent order: {}", order.getId(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        } catch (Exception e) {
            log.warn("Error fetching recent orders", e);
            return new ArrayList<>();
        }
    }

    public void refreshDashboard() {
        lastUpdated = new Date();
        log.info("Dashboard manually refreshed at {}", lastUpdated);
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }
}