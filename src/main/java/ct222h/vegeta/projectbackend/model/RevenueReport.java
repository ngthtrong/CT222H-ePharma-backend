package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "revenueReports")
public class RevenueReport {
    @Id
    private String id;
    private String reportType; // DAILY, MONTHLY, QUARTERLY
    private Date periodStart;
    private Date periodEnd;
    private Double totalRevenue;
    private Integer totalOrders;
    private List<ProductPerformance> productPerformance;
    private Date createdAt = new Date();

    // Nested class for product performance
    public static class ProductPerformance {
        private String productId;
        private Integer quantitySold;
        private Double revenue;

        public ProductPerformance() {}

        public ProductPerformance(String productId, Integer quantitySold, Double revenue) {
            this.productId = productId;
            this.quantitySold = quantitySold;
            this.revenue = revenue;
        }

        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public Integer getQuantitySold() { return quantitySold; }
        public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }

        public Double getRevenue() { return revenue; }
        public void setRevenue(Double revenue) { this.revenue = revenue; }
    }

    // Constructors
    public RevenueReport() {}

    public RevenueReport(String reportType, Date periodStart, Date periodEnd, Double totalRevenue, Integer totalOrders) {
        this.reportType = reportType;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public List<ProductPerformance> getProductPerformance() { return productPerformance; }
    public void setProductPerformance(List<ProductPerformance> productPerformance) { this.productPerformance = productPerformance; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
