package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.response.DashboardResponse;
import ct222h.vegeta.projectbackend.model.Order;
import ct222h.vegeta.projectbackend.repository.OrderRepository;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdvancedAnalyticsService {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProductRepository productRepository;

    public DashboardResponse.AdvancedDashboardMetrics getAdvancedDashboardMetrics(Date startDate, Date endDate) {
        // Get orders in date range
        List<Order> orders = orderRepository.findOrdersByDateRange(startDate, endDate);
        List<Order> completedOrders = orders.stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()))
                .collect(Collectors.toList());

        // Calculate revenue metrics
        Double totalRevenue = completedOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        Double averageOrderValue = completedOrders.isEmpty() ? 0.0 : 
                totalRevenue / completedOrders.size();

        // Calculate growth rates
        Date previousPeriodStart = getPreviousPeriodStart(startDate, endDate);
        Date previousPeriodEnd = startDate;
        
        List<Order> previousOrders = orderRepository.findOrdersByDateRange(previousPeriodStart, previousPeriodEnd);
        List<Order> previousCompletedOrders = previousOrders.stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()))
                .collect(Collectors.toList());

        Double previousRevenue = previousCompletedOrders.stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();

        Double revenueGrowthRate = calculateGrowthRate(previousRevenue, totalRevenue);

        // Calculate conversion rate
        Integer totalUsers = userRepository.findAll().size();
        Set<String> activeCustomers = completedOrders.stream()
                .map(Order::getUserId)
                .collect(Collectors.toSet());
        
        Double conversionRate = totalUsers > 0 ? (activeCustomers.size() * 100.0) / totalUsers : 0.0;

        // Calculate top categories performance - using mock category since OrderItem doesn't have category
        Map<String, CategoryPerformance> categoryPerformanceMap = new HashMap<>();
        
        for (Order order : completedOrders) {
            for (Order.OrderItem item : order.getItems()) {
                // Since OrderItem doesn't have category, we'll use product name prefix as mock category
                String category = getCategoryFromProductName(item.getProductName());
                CategoryPerformance performance = categoryPerformanceMap.getOrDefault(category,
                        new CategoryPerformance(category, 0, 0.0));
                
                performance.totalSold += item.getQuantity();
                performance.revenue += item.getItemTotal();
                
                categoryPerformanceMap.put(category, performance);
            }
        }

        List<DashboardResponse.CategoryPerformanceResponse> topCategories = categoryPerformanceMap.values().stream()
                .sorted((a, b) -> Double.compare(b.revenue, a.revenue))
                .limit(5)
                .map(data -> new DashboardResponse.CategoryPerformanceResponse(
                        data.categoryName, data.totalSold, data.revenue))
                .collect(Collectors.toList());

        // Calculate daily revenue trend
        List<DashboardResponse.DailyRevenueResponse> dailyRevenue = calculateDailyRevenue(completedOrders, startDate, endDate);

        // Calculate customer segments
        DashboardResponse.CustomerSegmentResponse customerSegments = calculateCustomerSegments(completedOrders);

        // Calculate inventory turnover (mock data for now)
        Double inventoryTurnover = calculateInventoryTurnover(completedOrders);

        return new DashboardResponse.AdvancedDashboardMetrics(
                totalRevenue,
                completedOrders.size(),
                averageOrderValue,
                revenueGrowthRate,
                conversionRate,
                activeCustomers.size(),
                topCategories,
                dailyRevenue,
                customerSegments,
                inventoryTurnover,
                new Date()
        );
    }

    public DashboardResponse.RealTimeMetrics getRealTimeMetrics() {
        // Get data for last 24 hours
        Date now = new Date();
        Date yesterday = new Date(now.getTime() - 24 * 60 * 60 * 1000);
        
        List<Order> recentOrders = orderRepository.findOrdersByDateRange(yesterday, now);
        
        Integer ordersLast24h = recentOrders.size();
        Double revenueLast24h = recentOrders.stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()))
                .mapToDouble(Order::getTotalAmount)
                .sum();

        // Get hourly order distribution for last 24 hours
        List<DashboardResponse.HourlyOrderResponse> hourlyOrders = calculateHourlyOrders(recentOrders);

        // Calculate peak hour
        String peakHour = hourlyOrders.stream()
                .max(Comparator.comparing(DashboardResponse.HourlyOrderResponse::getOrderCount))
                .map(DashboardResponse.HourlyOrderResponse::getHour)
                .orElse("00:00");

        // Active users online (mock data - would need WebSocket tracking)
        Integer activeUsersOnline = calculateActiveUsersOnline();

        return new DashboardResponse.RealTimeMetrics(
                ordersLast24h,
                revenueLast24h,
                hourlyOrders,
                peakHour,
                activeUsersOnline,
                new Date()
        );
    }

    private Date getPreviousPeriodStart(Date startDate, Date endDate) {
        long periodLength = endDate.getTime() - startDate.getTime();
        return new Date(startDate.getTime() - periodLength);
    }

    private Double calculateGrowthRate(Double previousValue, Double currentValue) {
        if (previousValue == 0) return currentValue > 0 ? 100.0 : 0.0;
        return ((currentValue - previousValue) / previousValue) * 100.0;
    }

    private List<DashboardResponse.DailyRevenueResponse> calculateDailyRevenue(List<Order> orders, Date startDate, Date endDate) {
        Map<String, Double> dailyRevenueMap = new HashMap<>();
        
        // Initialize all days with 0 revenue
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        
        while (!cal.getTime().after(endDate)) {
            String dateKey = String.format("%04d-%02d-%02d", 
                    cal.get(Calendar.YEAR), 
                    cal.get(Calendar.MONTH) + 1, 
                    cal.get(Calendar.DAY_OF_MONTH));
            dailyRevenueMap.put(dateKey, 0.0);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Calculate actual revenue for each day
        for (Order order : orders) {
            if ("COMPLETED".equals(order.getStatus()) && order.getCreatedAt() != null) {
                Calendar orderCal = Calendar.getInstance();
                orderCal.setTime(Date.from(order.getCreatedAt()));
                
                String dateKey = String.format("%04d-%02d-%02d", 
                        orderCal.get(Calendar.YEAR), 
                        orderCal.get(Calendar.MONTH) + 1, 
                        orderCal.get(Calendar.DAY_OF_MONTH));
                
                dailyRevenueMap.put(dateKey, 
                        dailyRevenueMap.getOrDefault(dateKey, 0.0) + order.getTotalAmount());
            }
        }

        return dailyRevenueMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new DashboardResponse.DailyRevenueResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private DashboardResponse.CustomerSegmentResponse calculateCustomerSegments(List<Order> orders) {
        Map<String, Double> customerSpending = new HashMap<>();
        
        for (Order order : orders) {
            String userId = order.getUserId();
            customerSpending.put(userId, 
                    customerSpending.getOrDefault(userId, 0.0) + order.getTotalAmount());
        }

        List<Double> spendingValues = new ArrayList<>(customerSpending.values());
        spendingValues.sort(Collections.reverseOrder());

        int totalCustomers = spendingValues.size();
        int highValueCustomers = (int) (totalCustomers * 0.2); // Top 20%
        int mediumValueCustomers = (int) (totalCustomers * 0.3); // Next 30%
        int lowValueCustomers = totalCustomers - highValueCustomers - mediumValueCustomers; // Remaining 50%

        return new DashboardResponse.CustomerSegmentResponse(
                highValueCustomers,
                mediumValueCustomers,
                lowValueCustomers
        );
    }

    private Double calculateInventoryTurnover(List<Order> orders) {
        // Mock calculation - in real scenario, would calculate based on inventory data
        int totalProductsSold = orders.stream()
                .mapToInt(order -> order.getItems().stream()
                        .mapToInt(Order.OrderItem::getQuantity)
                        .sum())
                .sum();
        
        // Assuming average inventory of 1000 items
        return totalProductsSold / 1000.0;
    }

    private List<DashboardResponse.HourlyOrderResponse> calculateHourlyOrders(List<Order> orders) {
        Map<Integer, Integer> hourlyOrderCount = new HashMap<>();
        
        // Initialize all hours with 0
        for (int i = 0; i < 24; i++) {
            hourlyOrderCount.put(i, 0);
        }

        for (Order order : orders) {
            if (order.getCreatedAt() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(Date.from(order.getCreatedAt()));
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                hourlyOrderCount.put(hour, hourlyOrderCount.get(hour) + 1);
            }
        }

        return hourlyOrderCount.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new DashboardResponse.HourlyOrderResponse(
                        String.format("%02d:00", entry.getKey()), 
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    private Integer calculateActiveUsersOnline() {
        // Mock implementation - in real scenario, would track WebSocket connections
        return new Random().nextInt(50) + 10; // Random between 10-60
    }

    private String getCategoryFromProductName(String productName) {
        // Mock category extraction from product name - in real scenario would use Product entity
        if (productName == null) return "Uncategorized";
        
        String lowerName = productName.toLowerCase();
        if (lowerName.contains("laptop") || lowerName.contains("computer") || lowerName.contains("desktop")) {
            return "Electronics";
        } else if (lowerName.contains("phone") || lowerName.contains("mobile") || lowerName.contains("smartphone")) {
            return "Mobile Devices";
        } else if (lowerName.contains("shirt") || lowerName.contains("dress") || lowerName.contains("clothes")) {
            return "Clothing";
        } else if (lowerName.contains("book") || lowerName.contains("novel") || lowerName.contains("magazine")) {
            return "Books";
        } else if (lowerName.contains("food") || lowerName.contains("snack") || lowerName.contains("drink")) {
            return "Food & Beverages";
        } else {
            return "General";
        }
    }

    // Helper class for category performance
    private static class CategoryPerformance {
        String categoryName;
        Integer totalSold;
        Double revenue;

        CategoryPerformance(String categoryName, Integer totalSold, Double revenue) {
            this.categoryName = categoryName;
            this.totalSold = totalSold;
            this.revenue = revenue;
        }
    }
}
