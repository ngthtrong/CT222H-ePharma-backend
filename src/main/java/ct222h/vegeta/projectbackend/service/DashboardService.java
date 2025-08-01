package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.response.DashboardResponse;
import ct222h.vegeta.projectbackend.model.Order;
import ct222h.vegeta.projectbackend.model.Product;
import ct222h.vegeta.projectbackend.repository.OrderRepository;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderService orderService;
    private final CategoryService categoryService;
    
    public DashboardService(OrderRepository orderRepository, ProductRepository productRepository,
                           UserRepository userRepository, OrderService orderService, CategoryService categoryService) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.categoryService = categoryService;
    }
    
    public DashboardResponse.DashboardStatsResponse getDashboardStats() {
        // Get all orders
        List<Order> allOrders = orderRepository.findAll();
        Integer totalOrders = allOrders.size();
        Integer pendingOrders = (int) allOrders.stream().filter(o -> "PENDING".equals(o.getStatus())).count();
        
        // Get total products and users
        Integer totalProducts = (int) productRepository.count();
        Integer totalUsers = (int) userRepository.count();
        
        // Calculate total revenue (completed orders only)
        Double totalRevenue = allOrders.stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalAmount)
                .sum();
        
        // Calculate today's revenue
        LocalDate today = LocalDate.now();
        Date todayStart = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date todayEnd = Date.from(today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Double todayRevenue = allOrders.stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .filter(o -> o.getCreatedAt() != null)
                .filter(o -> {
                    Date orderDate = Date.from(o.getCreatedAt());
                    return !orderDate.before(todayStart) && orderDate.before(todayEnd);
                })
                .mapToDouble(Order::getTotalAmount)
                .sum();
        
        // Calculate monthly revenue
        LocalDate firstDayOfMonth = today.withDayOfMonth(1);
        Date monthStart = Date.from(firstDayOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        Double monthlyRevenue = allOrders.stream()
                .filter(o -> "COMPLETED".equals(o.getStatus()))
                .filter(o -> o.getCreatedAt() != null)
                .filter(o -> {
                    Date orderDate = Date.from(o.getCreatedAt());
                    return !orderDate.before(monthStart);
                })
                .mapToDouble(Order::getTotalAmount)
                .sum();
        
        return new DashboardResponse.DashboardStatsResponse(
            totalOrders, pendingOrders, totalProducts, totalUsers,
            totalRevenue, todayRevenue, monthlyRevenue, new Date()
        );
    }
    
    public List<DashboardResponse.RecentOrderResponse> getRecentOrders(int limit) {
        List<Order> allOrders = orderRepository.findAll();
        
        return allOrders.stream()
                .sorted((a, b) -> {
                    if (a.getCreatedAt() == null && b.getCreatedAt() == null) return 0;
                    if (a.getCreatedAt() == null) return 1;
                    if (b.getCreatedAt() == null) return -1;
                    return b.getCreatedAt().compareTo(a.getCreatedAt());
                })
                .limit(limit)
                .map(order -> {
                    String customerName = orderService.getUserNameById(order.getUserId());
                    Date createdAt = order.getCreatedAt() != null ? Date.from(order.getCreatedAt()) : new Date();
                    
                    return new DashboardResponse.RecentOrderResponse(
                        order.getId(),
                        order.getOrderCode(),
                        customerName,
                        order.getTotalAmount(),
                        order.getStatus(),
                        createdAt
                    );
                })
                .collect(Collectors.toList());
    }
    
    public List<DashboardResponse.TopProductResponse> getTopSellingProducts(int limit) {
        // Get all completed orders from last 30 days
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        Date startDate = Date.from(thirtyDaysAgo.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = new Date();
        
        List<Order> recentOrders = orderRepository.findOrdersByDateRange(startDate, endDate);
        List<Order> completedOrders = recentOrders.stream()
                .filter(order -> "COMPLETED".equals(order.getStatus()))
                .collect(Collectors.toList());
        
        // Calculate product performance
        Map<String, ProductSalesData> productSalesMap = new HashMap<>();
        
        for (Order order : completedOrders) {
            for (Order.OrderItem item : order.getItems()) {
                String productId = item.getProductId();
                ProductSalesData data = productSalesMap.getOrDefault(productId, 
                    new ProductSalesData(productId, item.getProductName(), 0, 0.0));
                
                data.quantitySold += item.getQuantity();
                data.revenue += item.getItemTotal();
                
                productSalesMap.put(productId, data);
            }
        }
        
        // Get product details and convert to response
        return productSalesMap.values().stream()
                .sorted((a, b) -> Integer.compare(b.quantitySold, a.quantitySold))
                .limit(limit)
                .map(data -> {
                    Optional<Product> productOpt = productRepository.findById(data.productId);
                    Product product = productOpt.orElse(null);
                    
                    String productImage = product != null && product.getImages() != null && !product.getImages().isEmpty() 
                        ? product.getImages().get(0) : "";
                    String categoryName = product != null && product.getCategoryId() != null 
                        ? categoryService.getCategoryById(product.getCategoryId())
                            .map(category -> category.getName())
                            .orElse("Không xác định")
                        : "Không xác định";
                    
                    return new DashboardResponse.TopProductResponse(
                        data.productId,
                        data.productName,
                        productImage,
                        data.quantitySold,
                        data.revenue,
                        categoryName
                    );
                })
                .collect(Collectors.toList());
    }
    
    // Helper class for product sales calculation
    private static class ProductSalesData {
        String productId;
        String productName;
        Integer quantitySold;
        Double revenue;
        
        ProductSalesData(String productId, String productName, Integer quantitySold, Double revenue) {
            this.productId = productId;
            this.productName = productName;
            this.quantitySold = quantitySold;
            this.revenue = revenue;
        }
    }
}
