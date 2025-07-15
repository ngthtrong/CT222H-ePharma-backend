package ct222h.vegeta.projectbackend.model;

import ct222h.vegeta.projectbackend.constants.ProductConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @Indexed
    private String name;
    
    @Indexed(unique = true)
    private String sku;
    
    @Indexed(unique = true)
    private String slug;
    
    private String description;
    private List<String> images;
    private BigDecimal price;
    private int discountPercent;
    
    @Indexed
    private int stockQuantity;
    
    @Indexed
    private String categoryId;
    
    private String brand;
    private List<Map<String, Object>> attributes; // [{ "key": "color", "value": "red" }, ...]
    
    @Indexed
    private boolean published = ProductConstants.DEFAULT_PUBLISHED_STATUS;
    
    private List<String> relatedProducts; // list of product ids

    @CreatedDate
    @Indexed
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
