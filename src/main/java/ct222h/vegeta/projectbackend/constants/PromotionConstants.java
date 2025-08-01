package ct222h.vegeta.projectbackend.constants;

public class PromotionConstants {
    // Success messages
    public static final String SUCCESS_GET_PROMOTIONS = "Lấy danh sách khuyến mãi thành công";
    public static final String SUCCESS_GET_PROMOTION = "Lấy chi tiết khuyến mãi thành công";
    public static final String SUCCESS_CREATE_PROMOTION = "Tạo khuyến mãi thành công";
    public static final String SUCCESS_UPDATE_PROMOTION = "Cập nhật khuyến mãi thành công";
    public static final String SUCCESS_DELETE_PROMOTION = "Xóa khuyến mãi thành công";
    
    // Error messages
    public static final String ERROR_PROMOTION_NOT_FOUND = "Không tìm thấy khuyến mãi";
    public static final String ERROR_INVALID_DATE_RANGE = "Ngày kết thúc phải sau ngày bắt đầu";
    public static final String ERROR_PROMOTION_EXPIRED = "Khuyến mãi đã hết hạn";
    public static final String ERROR_PROMOTION_NOT_ACTIVE = "Khuyến mãi chưa được kích hoạt";
}
