package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String orderCode;
    private String userId;
    private ShippingAddress shippingAddress;
    private List<OrderItem> items;
    private Double subtotal;
    private Double shippingFee;
    private Double totalAmount;
    private String status = "PENDING"; // PENDING, PROCESSING, SHIPPED, COMPLETED, CANCELLED
    private String paymentMethod; // COD, MOMO, BANK_TRANSFER
    private String paymentStatus = "UNPAID"; // UNPAID, PAID
    private String notes;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();

    // Nested class for shipping address (copied from user at order time)
    public static class ShippingAddress {
        private String recipientName;
        private String phoneNumber;
        private String street;
        private String ward;
        private String city;

        public ShippingAddress() {}

        public ShippingAddress(String recipientName, String phoneNumber, String street, String ward, String city) {
            this.recipientName = recipientName;
            this.phoneNumber = phoneNumber;
            this.street = street;
            this.ward = ward;
            this.city = city;
        }

        // Getters and Setters
        public String getRecipientName() { return recipientName; }
        public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }

        public String getWard() { return ward; }
        public void setWard(String ward) { this.ward = ward; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
    }

    // Nested class for order items
    public static class OrderItem {
        private String productId;
        private String productName; // Copied at purchase time
        private Double priceAtPurchase; // Copied at purchase time
        private Integer quantity;

        public OrderItem() {}

        public OrderItem(String productId, String productName, Double priceAtPurchase, Integer quantity) {
            this.productId = productId;
            this.productName = productName;
            this.priceAtPurchase = priceAtPurchase;
            this.quantity = quantity;
        }

        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public Double getPriceAtPurchase() { return priceAtPurchase; }
        public void setPriceAtPurchase(Double priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        // Helper method to calculate item total
        public Double getItemTotal() {
            return priceAtPurchase * quantity;
        }
    }

    // Constructors
    public Order() {}

    public Order(String orderCode, String userId, ShippingAddress shippingAddress, List<OrderItem> items, String paymentMethod) {
        this.orderCode = orderCode;
        this.userId = userId;
        this.shippingAddress = shippingAddress;
        this.items = items;
        this.paymentMethod = paymentMethod;
        calculateTotals();
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderCode() { return orderCode; }
    public void setOrderCode(String orderCode) { this.orderCode = orderCode; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public ShippingAddress getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(ShippingAddress shippingAddress) { this.shippingAddress = shippingAddress; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getShippingFee() { return shippingFee; }
    public void setShippingFee(Double shippingFee) { this.shippingFee = shippingFee; }

    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    // Helper method to calculate totals
    public void calculateTotals() {
        if (items != null && !items.isEmpty()) {
            this.subtotal = items.stream()
                .mapToDouble(OrderItem::getItemTotal)
                .sum();
            this.totalAmount = this.subtotal + (this.shippingFee != null ? this.shippingFee : 0.0);
        }
    }
}
