package ct222h.vegeta.projectbackend.constants;

public class OrderConstants {
    
    // Order Status
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_PROCESSING = "PROCESSING";
    public static final String STATUS_SHIPPED = "SHIPPED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    
    // Payment Status
    public static final String PAYMENT_STATUS_UNPAID = "UNPAID";
    public static final String PAYMENT_STATUS_PAID = "PAID";
    
    // Payment Methods
    public static final String PAYMENT_METHOD_COD = "COD";
    public static final String PAYMENT_METHOD_MOMO = "MOMO";
    public static final String PAYMENT_METHOD_BANK_TRANSFER = "BANK_TRANSFER";
    
    // Error Messages
    public static final String ERROR_EMPTY_CART = "Giỏ hàng trống. Không thể tạo đơn hàng.";
    public static final String ERROR_ORDER_NOT_FOUND = "Không tìm thấy đơn hàng";
    public static final String ERROR_INSUFFICIENT_STOCK = "Không đủ hàng tồn kho cho sản phẩm: %s. Còn lại: %d, yêu cầu: %d";
    public static final String ERROR_CANNOT_CANCEL_ORDER = "Không thể hủy đơn hàng ở trạng thái %s";
    public static final String ERROR_NO_PERMISSION_CANCEL = "Bạn không có quyền hủy đơn hàng này";
    public static final String ERROR_USER_NOT_FOUND = "Không tìm thấy người dùng";
    public static final String ERROR_PRODUCT_NOT_FOUND = "Không tìm thấy sản phẩm";
    
    // Order Code Format
    public static final String ORDER_CODE_PREFIX = "ORD";
    public static final int ORDER_CODE_LENGTH = 16;
    
    // Default Values
    public static final Double DEFAULT_SHIPPING_FEE = 0.0;
    
    private OrderConstants() {
        // Utility class
    }
}
