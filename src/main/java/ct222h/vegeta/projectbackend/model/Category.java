package ct222h.vegeta.projectbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categories")
public class Category {
    
    @Id
    private String id;
    
    @Indexed
    private String name;
    
    @Indexed(unique = true)
    private String slug;
    
    private String description;
    
    @Indexed
    private String parentCategoryId;

    @CreatedDate
    @Indexed
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public Category(String name, String slug, String description, String parentCategoryId) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
    }
}
