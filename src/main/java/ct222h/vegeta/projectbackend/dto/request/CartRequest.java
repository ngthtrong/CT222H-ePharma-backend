package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public class CartRequest {

    public static class CartItemRequest {
        @NotBlank(message = "Product ID không được để trống")
        private String productId;
        
        @Min(value = 1, message = "Số lượng phải lớn hơn 0")
        private Integer quantity;

        // Getters, setters
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public static class CartItemQuantityRequest {
        @Min(value = 0, message = "Số lượng không được âm")
        private Integer quantity;

        // Getters, setters

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}