package ct222h.vegeta.projectbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String parentCategoryId;
    private String parentCategoryName;
}
