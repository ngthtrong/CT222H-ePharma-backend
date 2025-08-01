package ct222h.vegeta.projectbackend.dto.response;

import java.util.Date;
import java.util.List;

public class ReportResponse {
    
    public static class RevenueReportResponse {
        private String reportType;
        private Date periodStart;
        private Date periodEnd;
        private Double totalRevenue;
        private Integer totalOrders;
        private List<ProductPerformanceResponse> topProducts;
        private Date generatedAt;
        
        public RevenueReportResponse() {}
        
        public RevenueReportResponse(String reportType, Date periodStart, Date periodEnd, 
                                   Double totalRevenue, Integer totalOrders, 
                                   List<ProductPerformanceResponse> topProducts, Date generatedAt) {
            this.reportType = reportType;
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
            this.totalRevenue = totalRevenue;
            this.totalOrders = totalOrders;
            this.topProducts = topProducts;
            this.generatedAt = generatedAt;
        }
        
        // Getters and Setters
        public String getReportType() { return reportType; }
        public void setReportType(String reportType) { this.reportType = reportType; }
        
        public Date getPeriodStart() { return periodStart; }
        public void setPeriodStart(Date periodStart) { this.periodStart = periodStart; }
        
        public Date getPeriodEnd() { return periodEnd; }
        public void setPeriodEnd(Date periodEnd) { this.periodEnd = periodEnd; }
        
        public Double getTotalRevenue() { return totalRevenue; }
        public void setTotalRevenue(Double totalRevenue) { this.totalRevenue = totalRevenue; }
        
        public Integer getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
        
        public List<ProductPerformanceResponse> getTopProducts() { return topProducts; }
        public void setTopProducts(List<ProductPerformanceResponse> topProducts) { this.topProducts = topProducts; }
        
        public Date getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(Date generatedAt) { this.generatedAt = generatedAt; }
    }
    
    public static class ProductPerformanceResponse {
        private String productId;
        private String productName;
        private Integer quantitySold;
        private Double revenue;
        
        public ProductPerformanceResponse() {}
        
        public ProductPerformanceResponse(String productId, String productName, Integer quantitySold, Double revenue) {
            this.productId = productId;
            this.productName = productName;
            this.quantitySold = quantitySold;
            this.revenue = revenue;
        }
        
        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public Integer getQuantitySold() { return quantitySold; }
        public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }
        
        public Double getRevenue() { return revenue; }
        public void setRevenue(Double revenue) { this.revenue = revenue; }
    }
    
    public static class OrderStatisticsResponse {
        private Date periodStart;
        private Date periodEnd;
        private Integer totalOrders;
        private Integer completedOrders;
        private Integer cancelledOrders;
        private Integer pendingOrders;
        private Double averageOrderValue;
        private Date generatedAt;
        
        public OrderStatisticsResponse() {}
        
        public OrderStatisticsResponse(Date periodStart, Date periodEnd, Integer totalOrders,
                                     Integer completedOrders, Integer cancelledOrders, Integer pendingOrders,
                                     Double averageOrderValue, Date generatedAt) {
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
            this.totalOrders = totalOrders;
            this.completedOrders = completedOrders;
            this.cancelledOrders = cancelledOrders;
            this.pendingOrders = pendingOrders;
            this.averageOrderValue = averageOrderValue;
            this.generatedAt = generatedAt;
        }
        
        // Getters and Setters
        public Date getPeriodStart() { return periodStart; }
        public void setPeriodStart(Date periodStart) { this.periodStart = periodStart; }
        
        public Date getPeriodEnd() { return periodEnd; }
        public void setPeriodEnd(Date periodEnd) { this.periodEnd = periodEnd; }
        
        public Integer getTotalOrders() { return totalOrders; }
        public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }
        
        public Integer getCompletedOrders() { return completedOrders; }
        public void setCompletedOrders(Integer completedOrders) { this.completedOrders = completedOrders; }
        
        public Integer getCancelledOrders() { return cancelledOrders; }
        public void setCancelledOrders(Integer cancelledOrders) { this.cancelledOrders = cancelledOrders; }
        
        public Integer getPendingOrders() { return pendingOrders; }
        public void setPendingOrders(Integer pendingOrders) { this.pendingOrders = pendingOrders; }
        
        public Double getAverageOrderValue() { return averageOrderValue; }
        public void setAverageOrderValue(Double averageOrderValue) { this.averageOrderValue = averageOrderValue; }
        
        public Date getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(Date generatedAt) { this.generatedAt = generatedAt; }
    }
    
    public static class UserAnalyticsResponse {
        private Date periodStart;
        private Date periodEnd;
        private Integer totalUsers;
        private Integer newUsers;
        private Integer activeUsers;
        private Date generatedAt;
        
        public UserAnalyticsResponse() {}
        
        public UserAnalyticsResponse(Date periodStart, Date periodEnd, Integer totalUsers,
                                   Integer newUsers, Integer activeUsers, Date generatedAt) {
            this.periodStart = periodStart;
            this.periodEnd = periodEnd;
            this.totalUsers = totalUsers;
            this.newUsers = newUsers;
            this.activeUsers = activeUsers;
            this.generatedAt = generatedAt;
        }
        
        // Getters and Setters
        public Date getPeriodStart() { return periodStart; }
        public void setPeriodStart(Date periodStart) { this.periodStart = periodStart; }
        
        public Date getPeriodEnd() { return periodEnd; }
        public void setPeriodEnd(Date periodEnd) { this.periodEnd = periodEnd; }
        
        public Integer getTotalUsers() { return totalUsers; }
        public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }
        
        public Integer getNewUsers() { return newUsers; }
        public void setNewUsers(Integer newUsers) { this.newUsers = newUsers; }
        
        public Integer getActiveUsers() { return activeUsers; }
        public void setActiveUsers(Integer activeUsers) { this.activeUsers = activeUsers; }
        
        public Date getGeneratedAt() { return generatedAt; }
        public void setGeneratedAt(Date generatedAt) { this.generatedAt = generatedAt; }
    }
}
