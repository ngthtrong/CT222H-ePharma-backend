package ct222h.vegeta.projectbackend.dto.request;

public class CartRequest {

    public static class CartItemRequest {
        private String productId;
        private int quantity;

        // Getters, setters

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class CartItemQuantityRequest {
        private int quantity;

        // Getters, setters

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}