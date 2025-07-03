package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "categories")
public class Category {
    @Id
    private String id;
    private String name;
    private String slug;
    private String parentId; // nếu là con, sẽ lưu id cha
    private List<String> childrenIds; // id các con, nếu muốn dễ lấy cây
    private String description;

    public Category() {}

    public Category(String name, String slug, String parentId, String description) {
        this.name = name;
        this.slug = slug;
        this.parentId = parentId;
        this.description = description;
    }

    // Getters & Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public List<String> getChildrenIds() { return childrenIds; }
    public void setChildrenIds(List<String> childrenIds) { this.childrenIds = childrenIds; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
