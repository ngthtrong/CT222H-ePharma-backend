package ct222h.vegeta.projectbackend.constants;

public class NotificationConstants {
    // Success messages
    public static final String SUCCESS_GET_NOTIFICATIONS = "Lấy danh sách thông báo thành công";
    public static final String SUCCESS_GET_NOTIFICATION = "Lấy chi tiết thông báo thành công";
    public static final String SUCCESS_CREATE_NOTIFICATION = "Tạo thông báo thành công";
    public static final String SUCCESS_SEND_NOTIFICATION = "Gửi thông báo thành công";
    public static final String SUCCESS_MARK_READ = "Đánh dấu đã đọc thành công";
    public static final String SUCCESS_DELETE_NOTIFICATION = "Xóa thông báo thành công";
    
    // Error messages
    public static final String ERROR_NOTIFICATION_NOT_FOUND = "Không tìm thấy thông báo";
    public static final String ERROR_USER_NOT_FOUND = "Không tìm thấy người dùng";
    public static final String ERROR_INVALID_NOTIFICATION_TYPE = "Loại thông báo không hợp lệ";
    
    // Notification types
    public static final String TYPE_ORDER_STATUS = "ORDER_STATUS";
    public static final String TYPE_PROMOTION = "PROMOTION";
    public static final String TYPE_SYSTEM = "SYSTEM";
    public static final String TYPE_GENERAL = "GENERAL";
}
