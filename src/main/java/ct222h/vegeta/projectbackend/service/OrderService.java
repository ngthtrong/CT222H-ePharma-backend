package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.constants.OrderConstants;
import ct222h.vegeta.projectbackend.exception.ProductNotFoundException;
import ct222h.vegeta.projectbackend.model.Cart;
import ct222h.vegeta.projectbackend.model.Order;
import ct222h.vegeta.projectbackend.model.Product;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.CartRepository;
import ct222h.vegeta.projectbackend.repository.OrderRepository;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, 
                       UserRepository userRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> getOrderByCode(String orderCode) {
        return orderRepository.findByOrderCode(orderCode);
    }

    public Order createOrder(Order order) {
        validateUserCart(order.getUserId());
        Cart cart = getUserCart(order.getUserId());
        
        populateOrderFromCart(order, cart);
        setOrderDefaults(order);
        
        Order savedOrder = orderRepository.save(order);
        clearUserCart(cart);
        
        return savedOrder;
    }

    private void validateUserCart(String userId) {
        Optional<Cart> userCart = cartRepository.findByUserId(userId);
        if (userCart.isEmpty() || userCart.get().getItems() == null || userCart.get().getItems().isEmpty()) {
            throw new RuntimeException(OrderConstants.ERROR_EMPTY_CART);
        }
    }

    private Cart getUserCart(String userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException(OrderConstants.ERROR_EMPTY_CART));
    }

    private void populateOrderFromCart(Order order, Cart cart) {
        order.setOrderCode(generateOrderCode());
        
        List<Order.OrderItem> orderItems = cart.getItems().stream()
                .map(this::convertCartItemToOrderItem)
                .collect(Collectors.toList());
        
        order.setItems(orderItems);
        order.setShippingFee(calculateShippingFee(order));
        order.calculateTotals();
    }

    private void setOrderDefaults(Order order) {
        Instant now = Instant.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        if (order.getStatus() == null) {
            order.setStatus(OrderConstants.STATUS_PENDING);
        }
        if (order.getPaymentStatus() == null) {
            order.setPaymentStatus(OrderConstants.PAYMENT_STATUS_UNPAID);
        }
    }

    private void clearUserCart(Cart cart) {
        cart.getItems().clear();
        cart.calculateTotalAmount();
        cartRepository.save(cart);
    }

    public Order updateOrderStatus(String id, String status, String notes) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(OrderConstants.ERROR_ORDER_NOT_FOUND + " với ID: " + id));
        
        order.setStatus(status);
        if (notes != null && !notes.trim().isEmpty()) {
            order.setNotes(notes);
        }
        order.setUpdatedAt(Instant.now());
        
        return orderRepository.save(order);
    }

    public Order updatePaymentStatus(String id, String paymentStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(OrderConstants.ERROR_ORDER_NOT_FOUND + " với ID: " + id));
        
        order.setPaymentStatus(paymentStatus);
        order.setUpdatedAt(Instant.now());
        
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy đơn hàng với ID: " + id);
        }
        orderRepository.deleteById(id);
    }

    public Order cancelOrder(String orderCode, String reason) {
        // Find order by order code
        Optional<Order> existingOrder = orderRepository.findByOrderCode(orderCode);
        if (existingOrder.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng với mã: " + orderCode);
        }

        Order order = existingOrder.get();
        
        // Check if order can be cancelled
        if ("SHIPPED".equals(order.getStatus()) || "COMPLETED".equals(order.getStatus())) {
            throw new IllegalStateException("Không thể hủy đơn hàng đã được giao");
        }
        
        if ("CANCELLED".equals(order.getStatus())) {
            throw new IllegalStateException("Đơn hàng đã được hủy trước đó");
        }
        
        // Only allow PENDING and PROCESSING orders to be cancelled
        if (!"PENDING".equals(order.getStatus()) && !"PROCESSING".equals(order.getStatus())) {
            throw new IllegalStateException("Chỉ có thể hủy đơn hàng ở trạng thái PENDING hoặc PROCESSING");
        }

        // Restore stock for all items in the order
        for (Order.OrderItem item : order.getItems()) {
            Optional<Product> productOpt = productRepository.findById(item.getProductId());
            if (productOpt.isPresent()) {
                Product product = productOpt.get();
                product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                productRepository.save(product);
            }
        }

        // Update order status to cancelled
        order.setStatus("CANCELLED");
        order.setCancelReason(reason);
        order.setCancelledAt(Instant.now());
        order.setUpdatedAt(Instant.now());
        
        return orderRepository.save(order);
    }

    // User-specific order methods
    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getUserOrderByCode(String userId, String orderCode) {
        return orderRepository.findByOrderCode(orderCode)
                .filter(order -> order.getUserId().equals(userId));
    }

    // Admin filter methods - Refactored for better readability
    public List<Order> getAllOrdersWithFilters(String status, String userId, Date startDate, Date endDate) {
        FilterParams params = new FilterParams(status, userId, startDate, endDate);
        
        if (params.hasAllFilters()) {
            return orderRepository.findByUserIdAndStatusAndDateRange(userId, status, startDate, endDate);
        }
        if (params.hasStatusAndDateRange()) {
            return orderRepository.findOrdersByStatusAndDateRange(status, startDate, endDate);
        }
        if (params.hasUserAndDateRange()) {
            return orderRepository.findUserOrdersByDateRange(userId, startDate, endDate);
        }
        if (params.hasDateRange()) {
            return orderRepository.findOrdersByDateRange(startDate, endDate);
        }
        if (params.hasStatusAndUser()) {
            return orderRepository.findByUserIdAndStatus(userId, status);
        }
        if (params.hasStatus()) {
            return orderRepository.findByStatus(status);
        }
        if (params.hasUser()) {
            return orderRepository.findByUserId(userId);
        }
        
        return orderRepository.findAll();
    }

    // Helper class for filter parameters
    private static class FilterParams {
        private final String status;
        private final String userId;
        private final Date startDate;
        private final Date endDate;
        
        public FilterParams(String status, String userId, Date startDate, Date endDate) {
            this.status = status;
            this.userId = userId;
            this.startDate = startDate;
            this.endDate = endDate;
        }
        
        public boolean hasAllFilters() {
            return status != null && userId != null && startDate != null && endDate != null;
        }
        
        public boolean hasStatusAndDateRange() {
            return status != null && startDate != null && endDate != null;
        }
        
        public boolean hasUserAndDateRange() {
            return userId != null && startDate != null && endDate != null;
        }
        
        public boolean hasDateRange() {
            return startDate != null && endDate != null;
        }
        
        public boolean hasStatusAndUser() {
            return status != null && userId != null;
        }
        
        public boolean hasStatus() {
            return status != null;
        }
        
        public boolean hasUser() {
            return userId != null;
        }
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> searchOrders(String search) {
        // Search by order code or recipient name
        List<Order> ordersByCode = orderRepository.findByOrderCodeContainingIgnoreCase(search);
        List<Order> ordersByRecipient = orderRepository.findByRecipientNameContainingIgnoreCase(search);
        
        // Combine results and remove duplicates using Set
        Set<String> seenIds = new HashSet<>();
        List<Order> combinedResults = new ArrayList<>();
        
        // Add orders found by code
        for (Order order : ordersByCode) {
            if (seenIds.add(order.getId())) {
                combinedResults.add(order);
            }
        }
        
        // Add orders found by recipient name (if not already added)
        for (Order order : ordersByRecipient) {
            if (seenIds.add(order.getId())) {
                combinedResults.add(order);
            }
        }
        
        return combinedResults;
    }

    // Helper methods
    private String generateOrderCode() {
        String prefix = OrderConstants.ORDER_CODE_PREFIX;
        long timestamp = System.currentTimeMillis();
        int randomSuffix = new Random().nextInt(9999);
        
        String orderCode;
        do {
            orderCode = String.format("%s%d%04d", prefix, timestamp, randomSuffix);
            randomSuffix = (randomSuffix + 1) % 10000; // Ensure uniqueness
        } while (orderRepository.existsByOrderCode(orderCode));
        
        return orderCode;
    }

    private Order.OrderItem convertCartItemToOrderItem(Cart.CartItem cartItem) {
        // Validate product still exists and has enough stock
        Optional<Product> product = productRepository.findById(cartItem.getProductId());
        if (product.isEmpty()) {
            throw new ProductNotFoundException("Không tìm thấy sản phẩm với ID: " + cartItem.getProductId());
        }
        
        Product p = product.get();
        
        // Check stock availability
        if (p.getStockQuantity() < cartItem.getQuantity()) {
            throw new RuntimeException("Không đủ hàng tồn kho cho sản phẩm: " + p.getName() + 
                                     ". Còn lại: " + p.getStockQuantity() + ", yêu cầu: " + cartItem.getQuantity());
        }
        
        // Use current price or discounted price from cart
        Double priceAtPurchase = cartItem.getDiscountedPrice() != null ? 
                                cartItem.getDiscountedPrice() : cartItem.getProductPrice();
        
        // Create order item
        Order.OrderItem orderItem = new Order.OrderItem(
                cartItem.getProductId(),
                cartItem.getProductName(),
                priceAtPurchase,
                cartItem.getQuantity()
        );
        
        // Update stock quantity
        p.setStockQuantity(p.getStockQuantity() - cartItem.getQuantity());
        productRepository.save(p);
        
        return orderItem;
    }

    private Double calculateShippingFee(Order order) {
        // Simple shipping fee calculation
        Double subtotal = order.getItems().stream()
                .mapToDouble(item -> item.getPriceAtPurchase() * item.getQuantity())
                .sum();
        
        if (subtotal >= 500000) { // Free shipping for orders >= 500k VND
            return 0.0;
        }
        return 30000.0; // Default shipping fee: 30k VND
    }

    public String getUserNameById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getFullName).orElse("Unknown User");
    }

    public Order.ShippingAddress resolveShippingAddress(String userId, String selectedAddressId, 
                                                        Order.ShippingAddress customAddress) {
        // Get user information
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Không tìm thấy người dùng với ID: " + userId);
        }
        
        User user = userOpt.get();
        
        // If custom address is provided, use it
        if (customAddress != null) {
            return customAddress;
        }
        
        // If selectedAddressId is provided, find and use that address
        if (selectedAddressId != null && !selectedAddressId.trim().isEmpty()) {
            if (user.getAddresses() != null) {
                Optional<User.Address> selectedAddress = user.getAddresses().stream()
                        .filter(addr -> selectedAddressId.equals(addr.getId()))
                        .findFirst();
                
                if (selectedAddress.isPresent()) {
                    User.Address addr = selectedAddress.get();
                    return new Order.ShippingAddress(
                            selectedAddressId, // Store source address ID
                            addr.getRecipientName(),
                            addr.getPhoneNumber(),
                            addr.getStreet(),
                            addr.getWard(),
                            addr.getCity()
                    );
                }
            }
            throw new RuntimeException("Không tìm thấy địa chỉ với ID: " + selectedAddressId);
        }
        
        // Use default address if available
        if (user.getAddresses() != null) {
            Optional<User.Address> defaultAddress = user.getAddresses().stream()
                    .filter(addr -> Boolean.TRUE.equals(addr.getIsDefault()))
                    .findFirst();
            
            if (defaultAddress.isPresent()) {
                User.Address addr = defaultAddress.get();
                return new Order.ShippingAddress(
                        addr.getId(), // Store source address ID
                        addr.getRecipientName(),
                        addr.getPhoneNumber(),
                        addr.getStreet(),
                        addr.getWard(),
                        addr.getCity()
                );
            }
        }
        
        // If no default address found, throw error
        throw new RuntimeException("Không tìm thấy địa chỉ giao hàng. Vui lòng thêm địa chỉ hoặc chọn địa chỉ có sẵn.");
    }

    public void saveNewAddressToUser(String userId, Order.ShippingAddress shippingAddress, 
                                   boolean setAsDefault) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            if (user.getAddresses() == null) {
                user.setAddresses(new ArrayList<>());
            }
            
            // If setting as default, unset other default addresses
            if (setAsDefault) {
                user.getAddresses().forEach(addr -> addr.setIsDefault(false));
            }
            
            // Create new address
            User.Address newAddress = new User.Address(
                    setAsDefault,
                    shippingAddress.getRecipientName(),
                    shippingAddress.getPhoneNumber(),
                    shippingAddress.getStreet(),
                    shippingAddress.getWard(),
                    shippingAddress.getCity()
            );
            
            user.getAddresses().add(newAddress);
            userRepository.save(user);
        }
    }
}
