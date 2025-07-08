package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private List<Address> addresses;
    private String role = "USER";
    private String authProvider = "local";
    private Cart cart;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();

    // Nested classes for embedded documents
    public static class Address {
        private Boolean isDefault;
        private String recipientName;
        private String phoneNumber;
        private String street;
        private String ward;
        private String city;

        // Constructors
        public Address() {}

        public Address(Boolean isDefault, String recipientName, String phoneNumber, String street, String ward, String city) {
            this.isDefault = isDefault;
            this.recipientName = recipientName;
            this.phoneNumber = phoneNumber;
            this.street = street;
            this.ward = ward;
            this.city = city;
        }

        // Getters and Setters
        public Boolean getIsDefault() { return isDefault; }
        public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }

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

    public static class Cart {
        private List<CartItem> items;
        private Date updatedAt;

        // Constructors
        public Cart() {}

        public Cart(List<CartItem> items, Date updatedAt) {
            this.items = items;
            this.updatedAt = updatedAt;
        }

        // Getters and Setters
        public List<CartItem> getItems() { return items; }
        public void setItems(List<CartItem> items) { this.items = items; }

        public Date getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    }

    public static class CartItem {
        private String productId;
        private Integer quantity;

        // Constructors
        public CartItem() {}

        public CartItem(String productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        // Getters and Setters
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }


    // Main User getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
