package ct222h.vegeta.projectbackend.dto.response;

import java.util.Date;
import java.util.List;

public class DashboardResponse {
    
    public static class DashboardStatsResponse {
        private Double totalRevenue;
        private Double revenueGrowthRate;
        private Integer totalOrders;
        private Double averageOrderValue;
        private Double conversionRate;
        private List<DailyRevenueResponse> revenueMetrics;
        private List<CategoryPerformanceResponse> categoryPerformance;
        private CustomerSegmentResponse customerSegments;
        private List<TopProductResponse> topProducts;
        private List<RecentOrderResponse> recentOrders;
        private Date lastUpdated;
        
        public DashboardStatsResponse() {}
        
        public DashboardStatsResponse(Double totalRevenue, Double revenueGrowthRate, Integer totalOrders,
                                     Double averageOrderValue, Double conversionRate, 
                                     List<DailyRevenueResponse> revenueMetrics,
                                     List<CategoryPerformanceResponse> categoryPerformance,
                                     CustomerSegmentResponse customerSegments,
                                     List<TopProductResponse> topProducts,
                                     List<RecentOrderResponse> recentOrders,
                                     Date lastUpdated) {
            this.totalRevenue = totalRevenue;
            this.revenueGrowthRate = revenueGrowthRate;
            this.totalOrders = totalOrders;
            this.averageOrderValue = averageOrderValue;
            this.conversionRate = conversionRate;
            this.revenueMetrics = revenueMetrics;
            this.categoryPerformance = categoryPerformance;
            this.customerSegments = customerSegments;
            this.topProducts = topProducts;
            this.recentOrders = recentOrders;
            this.lastUpdated = lastUpdated;
        }
        
        // Getters and Setters
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
        
        public Double getRevenueGrowthRate() { return revenueGrowthRate; }
        public void setRevenueGrowthRate(Double revenueGrowthRate) { this.revenueGrowthRate = revenueGrowthRate; }
        
        public Integer getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
        
        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
        
        public Double getConversionRate() { return conversionRate; }
        public void setConversionRate(Double conversionRate) { this.conversionRate = conversionRate; }
        
        public List<DailyRevenueResponse> getRevenueMetrics() { return revenueMetrics; }
        public void setRevenueMetrics(List<DailyRevenueResponse> revenueMetrics) { this.revenueMetrics = revenueMetrics; }
        
        public List<CategoryPerformanceResponse> getCategoryPerformance() { return categoryPerformance; }
        public void setCategoryPerformance(List<CategoryPerformanceResponse> categoryPerformance) { this.categoryPerformance = categoryPerformance; }
        
        public CustomerSegmentResponse getCustomerSegments() { return customerSegments; }
        public void setCustomerSegments(CustomerSegmentResponse customerSegments) { this.customerSegments = customerSegments; }
        
        public List<TopProductResponse> getTopProducts() { return topProducts; }
        public void setTopProducts(List<TopProductResponse> topProducts) { this.topProducts = topProducts; }
        
        public List<RecentOrderResponse> getRecentOrders() { return recentOrders; }
        public void setRecentOrders(List<RecentOrderResponse> recentOrders) { this.recentOrders = recentOrders; }
        
        public Date getLastUpdated() { return lastUpdated; }
        public void setLastUpdated(Date lastUpdated) { this.lastUpdated = lastUpdated; }
    }
    
    public static class RecentOrderResponse {
        private String orderId;
        private String orderCode;
        private String customerName;
        private Double totalAmount;
        private String status;
        private Date createdAt;
        
        public RecentOrderResponse() {}
        
        public RecentOrderResponse(String orderId, String orderCode, String customerName,
                                  Double totalAmount, String status, Date createdAt) {
            this.orderId = orderId;
            this.orderCode = orderCode;
            this.customerName = customerName;
            this.totalAmount = totalAmount;
            this.status = status;
            this.createdAt = createdAt;
        }
        
        // Getters and Setters
        public String getOrderId() { return orderId; }
        public void setOrderId(String orderId) { this.orderId = orderId; }
        
        public String getOrderCode() { return orderCode; }
        public void setOrderCode(String orderCode) { this.orderCode = orderCode; }
        
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        
        public Double getTotalAmount() { return totalAmount; }
        public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public Date getCreatedAt() { return createdAt; }
        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    }
    
    public static class TopProductResponse {
        private String productId;
        private String productName;
        private String productImage;
        private Integer quantitySold;
        private Double revenue;
        private String categoryName;
        
        public TopProductResponse() {}
        
        public TopProductResponse(String productId, String productName, String productImage,
                                 Integer quantitySold, Double revenue, String categoryName) {
            this.productId = productId;
            this.productName = productName;
            this.productImage = productImage;
            this.quantitySold = quantitySold;
            this.revenue = revenue;
            this.categoryName = categoryName;
        }
        
        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public String getProductImage() { return productImage; }
        public void setProductImage(String productImage) { this.productImage = productImage; }
        
        public Integer getQuantitySold() { return quantitySold; }
        public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }
        
        public Double getRevenue() { return revenue; }
        public void setRevenue(Double revenue) { this.revenue = revenue; }
        
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    }

    public static class AdvancedDashboardMetrics {
        private Double totalRevenue;
        private Integer totalOrders;
        private Double averageOrderValue;
        private Double revenueGrowthRate;
        private Double conversionRate;
        private Integer activeCustomers;
        private List<CategoryPerformanceResponse> topCategories;
        private List<DailyRevenueResponse> dailyRevenue;
        private CustomerSegmentResponse customerSegments;
        private Double inventoryTurnover;
        private Date generatedAt;

        public AdvancedDashboardMetrics() {}

        public AdvancedDashboardMetrics(Double totalRevenue, Integer totalOrders, Double averageOrderValue,
                                      Double revenueGrowthRate, Double conversionRate, Integer activeCustomers,
                                      List<CategoryPerformanceResponse> topCategories, List<DailyRevenueResponse> dailyRevenue,
                                      CustomerSegmentResponse customerSegments, Double inventoryTurnover, Date generatedAt) {
            this.totalRevenue = totalRevenue;
            this.totalOrders = totalOrders;
            this.averageOrderValue = averageOrderValue;
            this.revenueGrowthRate = revenueGrowthRate;
            this.conversionRate = conversionRate;
            this.activeCustomers = activeCustomers;
            this.topCategories = topCategories;
            this.dailyRevenue = dailyRevenue;
            this.customerSegments = customerSegments;
            this.inventoryTurnover = inventoryTurnover;
            this.generatedAt = generatedAt;
        }

        // Getters and Setters
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }

        public Integer getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }

        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }

        public Double getRevenueGrowthRate() { return revenueGrowthRate; }
        public void setRevenueGrowthRate(Double revenueGrowthRate) { this.revenueGrowthRate = revenueGrowthRate; }

        public Double getConversionRate() { return conversionRate; }
        public void setConversionRate(Double conversionRate) { this.conversionRate = conversionRate; }

        public Integer getActiveCustomers() { return activeCustomers; }
        public void setActiveCustomers(Integer activeCustomers) { this.activeCustomers = activeCustomers; }

        public List<CategoryPerformanceResponse> getTopCategories() { return topCategories; }
        public void setTopCategories(List<CategoryPerformanceResponse> topCategories) { this.topCategories = topCategories; }

        public List<DailyRevenueResponse> getDailyRevenue() { return dailyRevenue; }
        public void setDailyRevenue(List<DailyRevenueResponse> dailyRevenue) { this.dailyRevenue = dailyRevenue; }

        public CustomerSegmentResponse getCustomerSegments() { return customerSegments; }
        public void setCustomerSegments(CustomerSegmentResponse customerSegments) { this.customerSegments = customerSegments; }

        public Double getInventoryTurnover() { return inventoryTurnover; }
        public void setInventoryTurnover(Double inventoryTurnover) { this.inventoryTurnover = inventoryTurnover; }

        public Date getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(Date generatedAt) { this.generatedAt = generatedAt; }
    }

    public static class CategoryPerformanceResponse {
        private String categoryName;
        private Integer totalSold;
        private Double revenue;

        public CategoryPerformanceResponse() {}

        public CategoryPerformanceResponse(String categoryName, Integer totalSold, Double revenue) {
            this.categoryName = categoryName;
            this.totalSold = totalSold;
            this.revenue = revenue;
        }

        // Getters and Setters
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

        public Integer getTotalSold() { return totalSold; }
        public void setTotalSold(Integer totalSold) { this.totalSold = totalSold; }

        public Double getRevenue() { return revenue; }
        public void setRevenue(Double revenue) { this.revenue = revenue; }
    }

    public static class DailyRevenueResponse {
        private String date;
        private Double revenue;

        public DailyRevenueResponse() {}

        public DailyRevenueResponse(String date, Double revenue) {
            this.date = date;
            this.revenue = revenue;
        }

        // Getters and Setters
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }

        public Double getRevenue() { return revenue; }
        public void setRevenue(Double revenue) { this.revenue = revenue; }
    }

    public static class CustomerSegmentResponse {
        private Integer highValueCustomers;
        private Integer mediumValueCustomers;
        private Integer lowValueCustomers;

        public CustomerSegmentResponse() {}

        public CustomerSegmentResponse(Integer highValueCustomers, Integer mediumValueCustomers, Integer lowValueCustomers) {
            this.highValueCustomers = highValueCustomers;
            this.mediumValueCustomers = mediumValueCustomers;
            this.lowValueCustomers = lowValueCustomers;
        }

        // Getters and Setters
        public Integer getHighValueCustomers() { return highValueCustomers; }
        public void setHighValueCustomers(Integer highValueCustomers) { this.highValueCustomers = highValueCustomers; }

        public Integer getMediumValueCustomers() { return mediumValueCustomers; }
        public void setMediumValueCustomers(Integer mediumValueCustomers) { this.mediumValueCustomers = mediumValueCustomers; }

        public Integer getLowValueCustomers() { return lowValueCustomers; }
        public void setLowValueCustomers(Integer lowValueCustomers) { this.lowValueCustomers = lowValueCustomers; }
    }

    public static class RealTimeMetrics {
        private Integer ordersLast24h;
        private Double revenueLast24h;
        private List<HourlyOrderResponse> hourlyOrders;
        private String peakHour;
        private Integer activeUsersOnline;
        private Date timestamp;

        public RealTimeMetrics() {}

        public RealTimeMetrics(Integer ordersLast24h, Double revenueLast24h, List<HourlyOrderResponse> hourlyOrders,
                             String peakHour, Integer activeUsersOnline, Date timestamp) {
            this.ordersLast24h = ordersLast24h;
            this.revenueLast24h = revenueLast24h;
            this.hourlyOrders = hourlyOrders;
            this.peakHour = peakHour;
            this.activeUsersOnline = activeUsersOnline;
            this.timestamp = timestamp;
        }

        // Getters and Setters
        public Integer getOrdersLast24h() { return ordersLast24h; }
        public void setOrdersLast24h(Integer ordersLast24h) { this.ordersLast24h = ordersLast24h; }

        public Double getRevenueLast24h() { return revenueLast24h; }
        public void setRevenueLast24h(Double revenueLast24h) { this.revenueLast24h = revenueLast24h; }

        public List<HourlyOrderResponse> getHourlyOrders() { return hourlyOrders; }
        public void setHourlyOrders(List<HourlyOrderResponse> hourlyOrders) { this.hourlyOrders = hourlyOrders; }

        public String getPeakHour() { return peakHour; }
        public void setPeakHour(String peakHour) { this.peakHour = peakHour; }

        public Integer getActiveUsersOnline() { return activeUsersOnline; }
        public void setActiveUsersOnline(Integer activeUsersOnline) { this.activeUsersOnline = activeUsersOnline; }

        public Date getTimestamp() { return timestamp; }
        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    }

    public static class HourlyOrderResponse {
        private String hour;
        private Integer orderCount;

        public HourlyOrderResponse() {}

        public HourlyOrderResponse(String hour, Integer orderCount) {
            this.hour = hour;
            this.orderCount = orderCount;
        }

        // Getters and Setters
        public String getHour() { return hour; }
        public void setHour(String hour) { this.hour = hour; }

        public Integer getOrderCount() { return orderCount; }
        public void setOrderCount(Integer orderCount) { this.orderCount = orderCount; }
    }
}
