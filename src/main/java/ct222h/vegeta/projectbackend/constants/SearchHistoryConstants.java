package ct222h.vegeta.projectbackend.constants;

public class SearchHistoryConstants {
    
    // Success messages
    public static final String SUCCESS_GET_SEARCH_HISTORY = "Lấy lịch sử tìm kiếm thành công";
    public static final String SUCCESS_SAVE_SEARCH_HISTORY = "Lưu lịch sử tìm kiếm thành công";
    public static final String SUCCESS_DELETE_SEARCH_HISTORY = "Xóa lịch sử tìm kiếm thành công";
    public static final String SUCCESS_CLEAR_SEARCH_HISTORY = "Xóa toàn bộ lịch sử tìm kiếm thành công";
    
    // Error messages
    public static final String ERROR_SEARCH_HISTORY_NOT_FOUND = "Không tìm thấy lịch sử tìm kiếm";
    public static final String ERROR_SEARCH_QUERY_REQUIRED = "Từ khóa tìm kiếm không được để trống";
    public static final String ERROR_USER_NOT_FOUND = "Người dùng không tồn tại";
    public static final String ERROR_UNAUTHORIZED_ACCESS = "Không có quyền truy cập lịch sử tìm kiếm";
    
    // Validation
    public static final int MAX_SEARCH_QUERY_LENGTH = 200;
    public static final int MAX_SEARCH_HISTORY_LIMIT = 50;
    
    private SearchHistoryConstants() {
        // Private constructor to prevent instantiation
    }
}
