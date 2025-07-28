package ct222h.vegeta.projectbackend.dto.response;

import java.util.Date;

public class DashboardResponse {
    
    public static class DashboardStatsResponse {
        private Integer totalOrders;
        private Integer pendingOrders;
        private Integer totalProducts;
        private Integer totalUsers;
        private Double totalRevenue;
        private Double todayRevenue;
        private Double monthlyRevenue;
        private Date lastUpdated;
        
        public DashboardStatsResponse() {}
        
        public DashboardStatsResponse(Integer totalOrders, Integer pendingOrders, Integer totalProducts,
                                     Integer totalUsers, Double totalRevenue, Double todayRevenue,
                                     Double monthlyRevenue, Date lastUpdated) {
            this.totalOrders = totalOrders;
            this.pendingOrders = pendingOrders;
            this.totalProducts = totalProducts;
            this.totalUsers = totalUsers;
            this.totalRevenue = totalRevenue;
            this.todayRevenue = todayRevenue;
            this.monthlyRevenue = monthlyRevenue;
            this.lastUpdated = lastUpdated;
        }
        
        // Getters and Setters
        public Integer getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
        
        public Integer getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(Integer pendingOrders) { this.pendingOrders = pendingOrders; }
        
        public Integer getTotalProducts() { return totalProducts; }
        public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
        
        public Integer getTotalUsers() { return totalUsers; }
        public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }
        
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
        
        public Double getTodayRevenue() { return todayRevenue; }
        public void setTodayRevenue(Double todayRevenue) { this.todayRevenue = todayRevenue; }
        
        public Double getMonthlyRevenue() { return monthlyRevenue; }
        public void setMonthlyRevenue(Double monthlyRevenue) { this.monthlyRevenue = monthlyRevenue; }
        
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
}
