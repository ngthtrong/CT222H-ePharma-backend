package ct222h.vegeta.projectbackend.dto.request;

import ct222h.vegeta.projectbackend.model.User;
import java.util.List;

public class CartRequest {
    private List<CartItemRequest> items;

    public CartRequest() {}

    public CartRequest(List<CartItemRequest> items) {
        this.items = items;
    }

    // Convert to User.Cart
    public User.Cart toCart() {
        List<User.CartItem> cartItems = items.stream()
                .map(CartItemRequest::toCartItem)
                .toList();
        return new User.Cart(cartItems, new java.util.Date());
    }

    // Getters and Setters
    public List<CartItemRequest> getItems() { return items; }
    public void setItems(List<CartItemRequest> items) { this.items = items; }

    // Nested class for cart item request
    public static class CartItemRequest {
        private String productId;
        private Integer quantity;

        public CartItemRequest() {}

        public CartItemRequest(String productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        // Convert to User.CartItem
        public User.CartItem toCartItem() {
            return new User.CartItem(productId, quantity);
        }

        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
