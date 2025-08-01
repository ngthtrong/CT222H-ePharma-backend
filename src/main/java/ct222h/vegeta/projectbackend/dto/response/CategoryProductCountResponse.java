package ct222h.vegeta.projectbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryProductCountResponse {
    private String categoryId;
    private String categoryName;
    private String categorySlug;
    private long productCount;
}
