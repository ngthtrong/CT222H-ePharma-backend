package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.CartRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.CartResponse;
import ct222h.vegeta.projectbackend.service.CartService;
import ct222h.vegeta.projectbackend.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@Tag(name = "Cart", description = "API quản lý giỏ hàng")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping
    @Operation(summary = "Lấy giỏ hàng hiện tại")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-Cart-Session-ID", required = false) String sessionId) {
        
        String userId = getUserIdFromToken(authHeader);
        String effectiveSessionId = getOrCreateSessionId(userId, sessionId);
        CartResponse cart = cartService.getCart(userId, effectiveSessionId);
        
        HttpHeaders headers = new HttpHeaders();
        if (userId == null && effectiveSessionId != null) {
            headers.add("X-Cart-Session-ID", effectiveSessionId);
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.success(cart, "Lấy giỏ hàng thành công"));
    }
    
    @PostMapping("/items")
    @Operation(summary = "Thêm sản phẩm vào giỏ hàng")
    public ResponseEntity<ApiResponse<CartResponse>> addItemToCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-Cart-Session-ID", required = false) String sessionId,
            @Valid @RequestBody CartRequest.CartItemRequest request) {
        
        String userId = getUserIdFromToken(authHeader);
        String effectiveSessionId = getOrCreateSessionId(userId, sessionId);
        CartResponse cart = cartService.addItemToCart(userId, effectiveSessionId, request);
        
        HttpHeaders headers = new HttpHeaders();
        if (userId == null && effectiveSessionId != null) {
            headers.add("X-Cart-Session-ID", effectiveSessionId);
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.success(cart, "Thêm sản phẩm vào giỏ hàng thành công"));
    }
    
    @PutMapping("/items/{productId}")
    @Operation(summary = "Cập nhật số lượng sản phẩm trong giỏ hàng")
    public ResponseEntity<ApiResponse<CartResponse>> updateItemQuantity(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-Cart-Session-ID", required = false) String sessionId,
            @PathVariable String productId,
            @Valid @RequestBody CartRequest.CartItemQuantityRequest request) {
        
        String userId = getUserIdFromToken(authHeader);
        String effectiveSessionId = getOrCreateSessionId(userId, sessionId);
        CartResponse cart = cartService.updateItemQuantity(userId, effectiveSessionId, productId, request);
        
        HttpHeaders headers = new HttpHeaders();
        if (userId == null && effectiveSessionId != null) {
            headers.add("X-Cart-Session-ID", effectiveSessionId);
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.success(cart, "Cập nhật số lượng thành công"));
    }
    
    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Xóa sản phẩm khỏi giỏ hàng")
    public ResponseEntity<ApiResponse<CartResponse>> removeItemFromCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-Cart-Session-ID", required = false) String sessionId,
            @PathVariable String productId) {
        
        String userId = getUserIdFromToken(authHeader);
        String effectiveSessionId = getOrCreateSessionId(userId, sessionId);
        CartResponse cart = cartService.removeItemFromCart(userId, effectiveSessionId, productId);
        
        HttpHeaders headers = new HttpHeaders();
        if (userId == null && effectiveSessionId != null) {
            headers.add("X-Cart-Session-ID", effectiveSessionId);
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.success(cart, "Xóa sản phẩm khỏi giỏ hàng thành công"));
    }
    
    @DeleteMapping
    @Operation(summary = "Xóa toàn bộ giỏ hàng")
    public ResponseEntity<ApiResponse<Void>> clearCart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestHeader(value = "X-Cart-Session-ID", required = false) String sessionId) {
        
        String userId = getUserIdFromToken(authHeader);
        String effectiveSessionId = getOrCreateSessionId(userId, sessionId);
        cartService.clearCart(userId, effectiveSessionId);
        
        HttpHeaders headers = new HttpHeaders();
        if (userId == null && effectiveSessionId != null) {
            headers.add("X-Cart-Session-ID", effectiveSessionId);
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(ApiResponse.success(null, "Xóa toàn bộ giỏ hàng thành công"));
    }
    
    @PostMapping("/merge")
    @Operation(summary = "Gộp giỏ hàng khách vào tài khoản người dùng")
    public ResponseEntity<ApiResponse<CartResponse>> mergeCart(
            @RequestHeader("Authorization") String authHeader,
            @RequestHeader("X-Cart-Session-ID") String sessionId) {
        
        String userId = getUserIdFromToken(authHeader);
        if (userId == null) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Yêu cầu đăng nhập để gộp giỏ hàng", 400));
        }
        
        CartResponse cart = cartService.mergeCart(userId, sessionId);
        
        return ResponseEntity.ok(ApiResponse.success(cart, "Gộp giỏ hàng thành công"));
    }
    
    // Helper method để lấy userId từ token
    private String getUserIdFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                return jwtUtil.extractUserId(token);
            } catch (Exception e) {
                // Token không hợp lệ, xử lý như guest user
                return null;
            }
        }
        return null;
    }
    
    // Helper method để tạo hoặc sử dụng session ID có sẵn
    private String getOrCreateSessionId(String userId, String sessionId) {
        // Nếu user đã đăng nhập, không cần session ID
        if (userId != null) {
            return sessionId; // Có thể là null, không sao
        }
        
        // Nếu user chưa đăng nhập và chưa có session ID, tạo mới
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return UUID.randomUUID().toString();
        }
        
        // Sử dụng session ID hiện tại
        return sessionId;
    }
}
