package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Document(collection = "carts")
public class Cart {
    @Id
    private String id;
    
    @Indexed
    private String userId; // Null cho guest user
    
    @Indexed
    private String sessionId; // Cho guest user
    
    private List<CartItem> items = new ArrayList<>();
    private Double totalAmount = 0.0;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    // Nested class cho cart items
    public static class CartItem {
        private String productId;
        private String productName;
        private String productImage;
        private Double productPrice;
        private Double discountedPrice;
        private Integer quantity;
        private Double subtotal;
        
        public CartItem() {}
        
        public CartItem(String productId, String productName, String productImage, 
                       Double productPrice, Double discountedPrice, Integer quantity) {
            this.productId = productId;
            this.productName = productName;
            this.productImage = productImage;
            this.productPrice = productPrice;
            this.discountedPrice = discountedPrice;
            this.quantity = quantity;
            this.subtotal = (discountedPrice != null ? discountedPrice : productPrice) * quantity;
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
        public void setQuantity(Integer quantity) { 
            this.quantity = quantity;
            this.subtotal = (discountedPrice != null ? discountedPrice : productPrice) * quantity;
        }
        
        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    }
    
    // Constructors
    public Cart() {}
    
    public Cart(String userId, String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
    }
    
    // Business methods
    public void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
        this.updatedAt = LocalDateTime.now();
    }
    
    public void addItem(CartItem newItem) {
        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        for (CartItem existingItem : items) {
            if (existingItem.getProductId().equals(newItem.getProductId())) {
                // Cập nhật số lượng
                existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                calculateTotalAmount();
                return;
            }
        }
        // Thêm item mới
        items.add(newItem);
        calculateTotalAmount();
    }
    
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        calculateTotalAmount();
    }
    
    public void updateItemQuantity(String productId, Integer quantity) {
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                if (quantity <= 0) {
                    removeItem(productId);
                } else {
                    item.setQuantity(quantity);
                    calculateTotalAmount();
                }
                return;
            }
        }
    }
    
    public void clearCart() {
        items.clear();
        totalAmount = 0.0;
        updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public List<CartItem> getItems() { return items; }
    public void setItems(List<CartItem> items) { 
        this.items = items; 
        calculateTotalAmount();
    }
    
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
