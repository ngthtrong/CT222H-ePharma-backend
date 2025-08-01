package ct222h.vegeta.projectbackend.constants;

public class ReportConstants {
    // Success messages
    public static final String SUCCESS_GET_REVENUE_REPORT = "Lấy báo cáo doanh thu thành công";
    public static final String SUCCESS_GET_PRODUCT_REPORT = "Lấy báo cáo sản phẩm thành công";
    public static final String SUCCESS_GET_ORDER_REPORT = "Lấy báo cáo đơn hàng thành công";
    public static final String SUCCESS_GET_USER_REPORT = "Lấy báo cáo người dùng thành công";
    
    // Error messages
    public static final String ERROR_INVALID_DATE_RANGE = "Khoảng thời gian không hợp lệ";
    public static final String ERROR_REPORT_GENERATION_FAILED = "Tạo báo cáo thất bại";
    
    // Report types
    public static final String REPORT_TYPE_DAILY = "DAILY";
    public static final String REPORT_TYPE_MONTHLY = "MONTHLY";
    public static final String REPORT_TYPE_QUARTERLY = "QUARTERLY";
    public static final String REPORT_TYPE_YEARLY = "YEARLY";
}
