package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.request.CartRequest;
import ct222h.vegeta.projectbackend.dto.response.CartResponse;
import ct222h.vegeta.projectbackend.exception.CartNotFoundException;
import ct222h.vegeta.projectbackend.exception.ProductNotFoundException;
import ct222h.vegeta.projectbackend.model.Cart;
import ct222h.vegeta.projectbackend.model.Product;
import ct222h.vegeta.projectbackend.repository.CartRepository;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    // Lấy giỏ hàng
    public CartResponse getCart(String userId, String sessionId) {
        Cart cart;
        if (userId != null) {
            cart = cartRepository.findByUserId(userId).orElse(null);
        } else {
            cart = cartRepository.findBySessionId(sessionId).orElse(null);
        }
        
        if (cart == null) {
            // Tạo giỏ hàng mới nếu chưa có
            cart = new Cart(userId, sessionId);
            cart = cartRepository.save(cart);
        }
        
        return mapToCartResponse(cart);
    }
    
    // Thêm sản phẩm vào giỏ hàng
    public CartResponse addItemToCart(String userId, String sessionId, CartRequest.CartItemRequest request) {
        // Kiểm tra sản phẩm có tồn tại không
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Sản phẩm không tồn tại"));
        
        // Lấy hoặc tạo giỏ hàng
        Cart cart;
        if (userId != null) {
            cart = cartRepository.findByUserId(userId).orElse(new Cart(userId, null));
        } else {
            cart = cartRepository.findBySessionId(sessionId).orElse(new Cart(null, sessionId));
        }
        
        // Tạo cart item từ product
        Double actualPrice = product.getPrice().doubleValue();
        Double discountedPrice = null;
        if (product.getDiscountPercent() > 0) {
            discountedPrice = actualPrice * (100 - product.getDiscountPercent()) / 100.0;
        }
        
        Cart.CartItem cartItem = new Cart.CartItem(
                product.getId(),
                product.getName(),
                product.getImages() != null && !product.getImages().isEmpty() ? 
                    product.getImages().get(0) : null,
                actualPrice,
                discountedPrice,
                request.getQuantity()
        );
        
        // Thêm vào giỏ hàng
        cart.addItem(cartItem);
        cart = cartRepository.save(cart);
        
        return mapToCartResponse(cart);
    }
    
    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public CartResponse updateItemQuantity(String userId, String sessionId, String productId, 
                                         CartRequest.CartItemQuantityRequest request) {
        Cart cart = getCartEntity(userId, sessionId);
        if (cart == null) {
            throw new CartNotFoundException("Giỏ hàng không tồn tại");
        }
        
        cart.updateItemQuantity(productId, request.getQuantity());
        cart = cartRepository.save(cart);
        
        return mapToCartResponse(cart);
    }
    
    // Xóa sản phẩm khỏi giỏ hàng
    public CartResponse removeItemFromCart(String userId, String sessionId, String productId) {
        Cart cart = getCartEntity(userId, sessionId);
        if (cart == null) {
            throw new CartNotFoundException("Giỏ hàng không tồn tại");
        }
        
        cart.removeItem(productId);
        cart = cartRepository.save(cart);
        
        return mapToCartResponse(cart);
    }
    
    // Xóa toàn bộ giỏ hàng
    public void clearCart(String userId, String sessionId) {
        Cart cart = getCartEntity(userId, sessionId);
        if (cart != null) {
            cart.clearCart();
            cartRepository.save(cart);
        }
    }
    
    // Gộp giỏ hàng khách vào tài khoản người dùng
    public CartResponse mergeCart(String userId, String sessionId) {
        Optional<Cart> userCartOpt = cartRepository.findByUserId(userId);
        Optional<Cart> guestCartOpt = cartRepository.findBySessionId(sessionId);
        
        if (guestCartOpt.isEmpty()) {
            // Không có giỏ hàng guest, trả về giỏ hàng user hoặc tạo mới
            return getCart(userId, null);
        }
        
        Cart guestCart = guestCartOpt.get();
        
        if (userCartOpt.isEmpty()) {
            // Chuyển giỏ hàng guest thành giỏ hàng user
            guestCart.setUserId(userId);
            guestCart.setSessionId(null);
            cartRepository.save(guestCart);
            return mapToCartResponse(guestCart);
        }
        
        // Gộp 2 giỏ hàng
        Cart userCart = userCartOpt.get();
        for (Cart.CartItem guestItem : guestCart.getItems()) {
            userCart.addItem(guestItem);
        }
        
        // Lưu giỏ hàng user và xóa giỏ hàng guest
        cartRepository.save(userCart);
        cartRepository.delete(guestCart);
        
        return mapToCartResponse(userCart);
    }
    
    // Helper methods
    private Cart getCartEntity(String userId, String sessionId) {
        if (userId != null) {
            return cartRepository.findByUserId(userId).orElse(null);
        } else {
            return cartRepository.findBySessionId(sessionId).orElse(null);
        }
    }
    
    private CartResponse mapToCartResponse(Cart cart) {
        List<CartResponse.CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> new CartResponse.CartItemResponse(
                        item.getProductId(),
                        item.getProductName(),
                        item.getProductImage(),
                        item.getProductPrice(),
                        item.getDiscountedPrice(),
                        item.getQuantity(),
                        item.getSubtotal()
                ))
                .collect(Collectors.toList());
        
        return new CartResponse(
                cart.getId(),
                cart.getUserId(),
                cart.getSessionId(),
                itemResponses,
                cart.getTotalAmount(),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }
}
