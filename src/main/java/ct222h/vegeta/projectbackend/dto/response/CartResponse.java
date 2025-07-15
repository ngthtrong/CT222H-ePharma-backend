package ct222h.vegeta.projectbackend.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class CartResponse {
    private String id;
    private String userId;
    private String sessionId;
    private List<CartItemResponse> items;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static class CartItemResponse {
        private String productId;
        private String productName;
        private String productImage;
        private Double productPrice;
        private Double discountedPrice;
        private Integer quantity;
        private Double subtotal;
        
        public CartItemResponse() {}
        
        public CartItemResponse(String productId, String productName, String productImage,
                               Double productPrice, Double discountedPrice, Integer quantity, Double subtotal) {
            this.productId = productId;
            this.productName = productName;
            this.productImage = productImage;
            this.productPrice = productPrice;
            this.discountedPrice = discountedPrice;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
        
        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public String getProductImage() { return productImage; }
        public void setProductImage(String productImage) { this.productImage = productImage; }
        
        public Double getProductPrice() { return productPrice; }
        public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }
        
        public Double getDiscountedPrice() { return discountedPrice; }
        public void setDiscountedPrice(Double discountedPrice) { this.discountedPrice = discountedPrice; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }
    
    // Constructors
    public CartResponse() {}
    
    public CartResponse(String id, String userId, String sessionId, List<CartItemResponse> items,
                       Double totalAmount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.sessionId = sessionId;
        this.items = items;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public List<CartItemResponse> getItems() { return items; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
