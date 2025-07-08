package ct222h.vegeta.projectbackend.dto.request;

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
    @Size(max = 100, message = "Tên danh mục không được vượt quá 100 ký tự")
    private String name;

    @NotBlank(message = "Slug không được để trống")
    @Size(max = 100, message = "Slug không được vượt quá 100 ký tự")
    private String slug;

    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    private String description;

    private String parentCategoryId; // nullable nếu là danh mục gốc
}
