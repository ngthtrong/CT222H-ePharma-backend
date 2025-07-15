package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.OrderConstants;
import ct222h.vegeta.projectbackend.dto.request.OrderCancelRequest;
import ct222h.vegeta.projectbackend.dto.request.OrderRequest;
import ct222h.vegeta.projectbackend.dto.request.OrderStatusUpdateRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.OrderResponse;
import ct222h.vegeta.projectbackend.model.Order;
import ct222h.vegeta.projectbackend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    
    // USER ENDPOINTS

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders(@PathVariable String userId) {
        List<Order> orders = orderService.getUserOrders(userId);
        List<OrderResponse> responses = orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách đơn hàng của user thành công", responses));
    }

    @GetMapping("/users/{userId}/orders/code/{orderCode}")
    public ResponseEntity<ApiResponse<OrderResponse>> getUserOrderByCode(
            @PathVariable String userId, 
            @PathVariable String orderCode) {
        Optional<Order> order = orderService.getUserOrderByCode(userId, orderCode);
        return order
                .map(o -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy đơn hàng thành công", convertToOrderResponse(o))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, "Không tìm thấy đơn hàng", null)));
    }

    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderRequest request) {
        Order order = convertToOrder(request);
        Order created = orderService.createOrder(order);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "Tạo đơn hàng thành công", convertToOrderResponse(created)));
    }

    @PutMapping("/orders/{orderCode}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable String orderCode,
            @Valid @RequestBody OrderCancelRequest request) {
        try {
            Order cancelled = orderService.cancelOrder(orderCode, request.getReason());
            return ResponseEntity.ok(new ApiResponse<>(true, "Hủy đơn hàng thành công", convertToOrderResponse(cancelled)));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, OrderConstants.ERROR_ORDER_NOT_FOUND, null));
        }
    }

    // ADMIN ENDPOINTS
    
    @GetMapping("/admin/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrdersWithFilters(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(required = false) String search) {
        
        List<Order> orders;
        
        if (search != null && !search.trim().isEmpty()) {
            // Search functionality (order code or recipient name)
            orders = orderService.searchOrders(search.trim());
        } else {
            // Filter functionality
            orders = orderService.getAllOrdersWithFilters(status, userId, startDate, endDate);
        }
        
        List<OrderResponse> responses = orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());

        String message = search != null ? "Tìm kiếm đơn hàng thành công" : "Lấy danh sách đơn hàng với filter thành công";
        return ResponseEntity.ok(new ApiResponse<>(true, message, responses));
    }

    @GetMapping("/admin/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByIdAdmin(@PathVariable String orderId) {
        Optional<Order> order = orderService.getOrderById(orderId);
        return order
                .map(o -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy chi tiết đơn hàng thành công", convertToOrderResponse(o))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, "Không tìm thấy đơn hàng", null)));
    }

    @GetMapping("/admin/orders/code/{orderCode}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByCodeAdmin(@PathVariable String orderCode) {
        Optional<Order> order = orderService.getOrderByCode(orderCode);
        return order
                .map(o -> ResponseEntity.ok(new ApiResponse<>(true, "Lấy đơn hàng thành công", convertToOrderResponse(o))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, "Không tìm thấy đơn hàng", null)));
    }

    @PutMapping("/admin/orders/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable String id, 
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        Order updated = orderService.updateOrderStatus(id, request.getStatus(), request.getNotes());
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật trạng thái đơn hàng thành công", convertToOrderResponse(updated)));
    }

    @PutMapping("/admin/orders/{id}/payment-status")
    public ResponseEntity<ApiResponse<OrderResponse>> updatePaymentStatus(
            @PathVariable String id, 
            @RequestParam String paymentStatus) {
        Order updated = orderService.updatePaymentStatus(id, paymentStatus);
        return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật trạng thái thanh toán thành công", convertToOrderResponse(updated)));
    }

    @DeleteMapping("/admin/orders/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Xóa đơn hàng thành công", null));
    }

    // Helper methods
    private Order convertToOrder(OrderRequest request) {
        // Resolve shipping address based on request
        Order.ShippingAddress shippingAddress;
        
        if (request.getCustomShippingAddress() != null) {
            // Use custom address
            OrderRequest.ShippingAddressRequest customAddr = request.getCustomShippingAddress();
            shippingAddress = new Order.ShippingAddress(
                    customAddr.getRecipientName(),
                    customAddr.getPhoneNumber(),
                    customAddr.getStreet(),
                    customAddr.getWard(),
                    customAddr.getCity()
            );
            
            // Save as new address if requested
            if (Boolean.TRUE.equals(customAddr.getSaveAsNewAddress())) {
                orderService.saveNewAddressToUser(
                        request.getUserId(), 
                        shippingAddress, 
                        Boolean.TRUE.equals(customAddr.getSetAsDefault())
                );
            }
        } else {
            // Use existing address (selected or default)
            shippingAddress = orderService.resolveShippingAddress(
                    request.getUserId(), 
                    request.getSelectedAddressId(), 
                    null
            );
        }

        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setNotes(request.getNotes());
        // Items will be populated from cart in service layer

        return order;
    }

    private OrderResponse convertToOrderResponse(Order order) {
        // Convert shipping address
        OrderResponse.ShippingAddressResponse shippingAddress = new OrderResponse.ShippingAddressResponse(
                order.getShippingAddress().getSourceAddressId(),
                order.getShippingAddress().getRecipientName(),
                order.getShippingAddress().getPhoneNumber(),
                order.getShippingAddress().getStreet(),
                order.getShippingAddress().getWard(),
                order.getShippingAddress().getCity()
        );

        // Convert order items
        List<OrderResponse.OrderItemResponse> items = order.getItems().stream()
                .map(item -> new OrderResponse.OrderItemResponse(
                        item.getProductId(),
                        item.getProductName(),
                        item.getPriceAtPurchase(),
                        item.getQuantity(),
                        item.getItemTotal()
                )).collect(Collectors.toList());

        // Get user name
        String userName = orderService.getUserNameById(order.getUserId());

        return new OrderResponse(
                order.getId(),
                order.getOrderCode(),
                order.getUserId(),
                userName,
                shippingAddress,
                items,
                order.getSubtotal(),
                order.getShippingFee(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getNotes(),
                order.getTrackingNumber(),
                order.getCancelReason(),
                order.getCancelledAt(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
