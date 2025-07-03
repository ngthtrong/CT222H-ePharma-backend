package ct222h.vegeta.projectbackend.dto.response;

import java.util.List;

public class CategoryResponse {
    private String id;
    private String name;
    private String slug;
    private String parentId;
    private List<String> childrenIds;
    private String description;

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
