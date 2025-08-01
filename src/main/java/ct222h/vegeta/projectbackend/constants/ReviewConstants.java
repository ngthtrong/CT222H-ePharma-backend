package ct222h.vegeta.projectbackend.constants;

public class ReviewConstants {
    // Success messages
    public static final String SUCCESS_GET_REVIEWS = "Lấy danh sách đánh giá thành công";
    public static final String SUCCESS_GET_REVIEW = "Lấy chi tiết đánh giá thành công";
    public static final String SUCCESS_CREATE_REVIEW = "Tạo đánh giá thành công";
    public static final String SUCCESS_UPDATE_REVIEW = "Cập nhật đánh giá thành công";
    public static final String SUCCESS_DELETE_REVIEW = "Xóa đánh giá thành công";
    public static final String SUCCESS_REPLY_REVIEW = "Trả lời đánh giá thành công";
    
    // Error messages
    public static final String ERROR_REVIEW_NOT_FOUND = "Không tìm thấy đánh giá";
    public static final String ERROR_INVALID_RATING = "Đánh giá phải từ 1 đến 5 sao";
    public static final String ERROR_DUPLICATE_REVIEW = "Bạn đã đánh giá sản phẩm này rồi";
    public static final String ERROR_PRODUCT_NOT_FOUND = "Không tìm thấy sản phẩm";
    public static final String ERROR_USER_NOT_FOUND = "Không tìm thấy người dùng";
}
