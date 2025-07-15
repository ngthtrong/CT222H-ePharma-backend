package ct222h.vegeta.projectbackend.constants;

public class CategoryConstants {
    
    // Error Messages
    public static final String ERROR_CATEGORY_NOT_FOUND = "Không tìm thấy danh mục";
    public static final String ERROR_SLUG_EXISTS = "Slug đã tồn tại, vui lòng chọn slug khác";
    public static final String ERROR_CATEGORY_HAS_CHILDREN = "Không thể xóa danh mục có danh mục con";
    public static final String ERROR_CATEGORY_HAS_PRODUCTS = "Không thể xóa danh mục có sản phẩm";
    public static final String ERROR_INVALID_PARENT_CATEGORY = "Danh mục cha không tồn tại";
    public static final String ERROR_CIRCULAR_REFERENCE = "Không thể tạo tham chiếu vòng tròn trong danh mục";
    
    // Success Messages
    public static final String SUCCESS_GET_CATEGORIES = "Lấy danh sách danh mục thành công";
    public static final String SUCCESS_GET_CATEGORY = "Lấy danh mục thành công";
    public static final String SUCCESS_CREATE_CATEGORY = "Tạo danh mục thành công";
    public static final String SUCCESS_UPDATE_CATEGORY = "Cập nhật danh mục thành công";
    public static final String SUCCESS_DELETE_CATEGORY = "Xóa danh mục thành công";
    public static final String SUCCESS_GET_CATEGORY_CHILDREN = "Lấy danh sách danh mục con thành công";
    
    // Validation
    public static final int MAX_NAME_LENGTH = 100;
    public static final int MAX_SLUG_LENGTH = 100;
    public static final int MAX_DESCRIPTION_LENGTH = 255;
    
    // Default Values
    public static final String DEFAULT_PARENT_CATEGORY_NAME = "Danh mục gốc";
    
    private CategoryConstants() {
        // Utility class
    }
}
