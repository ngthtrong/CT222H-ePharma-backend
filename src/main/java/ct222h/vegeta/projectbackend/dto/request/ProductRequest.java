package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 150)
    private String name;

    @NotBlank(message = "SKU không được để trống")
    @Size(max = 100)
    private String sku;

    @NotBlank(message = "Slug không được để trống")
    @Size(max = 150)
    private String slug;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Danh sách ảnh không được để trống")
    private List<String> images;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    private BigDecimal price;

    @Min(value = 0, message = "Giảm giá không được âm")
    @Max(value = 100, message = "Giảm giá tối đa là 100%")
    private int discountPercent;

    @Min(value = 0, message = "Số lượng tồn kho không được âm")
    private int stockQuantity;

    @NotBlank(message = "Danh mục không được để trống")
    private String categoryId;

    @Size(max = 100)
    private String brand;

    private List<Map<String, Object>> attributes;

    private Boolean published = true;

    private List<String> relatedProducts;
}
