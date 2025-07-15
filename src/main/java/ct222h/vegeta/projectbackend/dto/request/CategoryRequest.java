package ct222h.vegeta.projectbackend.dto.request;

import ct222h.vegeta.projectbackend.constants.CategoryConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {

    @NotBlank(message = "Tên danh mục không được để trống")
    @Size(max = CategoryConstants.MAX_NAME_LENGTH, message = "Tên danh mục không được vượt quá " + CategoryConstants.MAX_NAME_LENGTH + " ký tự")
    private String name;

    @NotBlank(message = "Slug không được để trống")
    @Size(max = CategoryConstants.MAX_SLUG_LENGTH, message = "Slug không được vượt quá " + CategoryConstants.MAX_SLUG_LENGTH + " ký tự")
    private String slug;

    @Size(max = CategoryConstants.MAX_DESCRIPTION_LENGTH, message = "Mô tả không được vượt quá " + CategoryConstants.MAX_DESCRIPTION_LENGTH + " ký tự")
    private String description;

    private String parentCategoryId; // nullable nếu là danh mục gốc
}
