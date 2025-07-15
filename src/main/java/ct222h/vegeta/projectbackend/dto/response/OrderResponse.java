package ct222h.vegeta.projectbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String orderCode;
    private String userId;
    private String userName;
    private ShippingAddressResponse shippingAddress;
    private List<OrderItemResponse> items;
    private Double subtotal;
    private Double shippingFee;
    private Double totalAmount;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
    private String trackingNumber;
    private String cancelReason;
    private Instant cancelledAt;
    private Instant createdAt;
    private Instant updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddressResponse {
        private String sourceAddressId; // ID địa chỉ gốc từ user (nếu có)
        private String recipientName;
        private String phoneNumber;
        private String street;
        private String ward;
        private String city;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private String productId;
        private String productName;
        private Double priceAtPurchase;
        private Integer quantity;
        private Double itemTotal;
    }
}
