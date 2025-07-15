package ct222h.vegeta.projectbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String sku;
    private String slug;
    private String description;
    private List<String> images;
    private BigDecimal price;
    private int discountPercent;
    private int stockQuantity;
    private String categoryId;
    private String categoryName;
    private String brand;
    private List<Map<String, Object>> attributes;
    private boolean published;
    private List<String> relatedProducts;
    private Instant createdAt;
    private Instant updatedAt;
}
