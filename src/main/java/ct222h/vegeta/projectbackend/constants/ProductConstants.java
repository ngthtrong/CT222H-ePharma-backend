package ct222h.vegeta.projectbackend.constants;

public class ProductConstants {
    
    // Product Status
    public static final boolean DEFAULT_PUBLISHED_STATUS = true;
    
    // Error Messages
    public static final String ERROR_PRODUCT_NOT_FOUND = "Không tìm thấy sản phẩm";
    public static final String ERROR_SLUG_EXISTS = "Slug đã tồn tại, vui lòng chọn slug khác";
    public static final String ERROR_SKU_EXISTS = "SKU đã tồn tại, vui lòng chọn SKU khác";
    public static final String ERROR_CATEGORY_NOT_FOUND = "Không tìm thấy danh mục";
    
    // Success Messages
    public static final String SUCCESS_GET_PRODUCTS = "Lấy danh sách sản phẩm thành công";
    public static final String SUCCESS_GET_PRODUCT = "Lấy sản phẩm thành công";
    public static final String SUCCESS_SEARCH_PRODUCTS = "Tìm kiếm sản phẩm thành công";
    public static final String SUCCESS_GET_RELATED_PRODUCTS = "Lấy danh sách sản phẩm liên quan thành công";
    public static final String SUCCESS_CREATE_PRODUCT = "Tạo sản phẩm thành công";
    public static final String SUCCESS_UPDATE_PRODUCT = "Cập nhật sản phẩm thành công";
    public static final String SUCCESS_DELETE_PRODUCT = "Xóa sản phẩm thành công";
    public static final String SUCCESS_FILTER_PRODUCTS = "Lọc sản phẩm thành công";
    
    // Validation
    public static final int MAX_NAME_LENGTH = 150;
    public static final int MAX_SKU_LENGTH = 100;
    public static final int MAX_SLUG_LENGTH = 150;
    public static final int MAX_DESCRIPTION_LENGTH = 500;
    public static final int MAX_BRAND_LENGTH = 100;
    public static final int MAX_DISCOUNT_PERCENT = 100;
    public static final int MIN_DISCOUNT_PERCENT = 0;
    public static final int MIN_STOCK_QUANTITY = 0;
    
    private ProductConstants() {
        // Utility class
    }
}
