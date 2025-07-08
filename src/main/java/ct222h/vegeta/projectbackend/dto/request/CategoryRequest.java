package ct222h.vegeta.projectbackend.dto.request;

public class CategoryRequest {
    private String name;
    private String slug;
    private String parentCategoryId;
    private String description;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(String parentCategoryId) { this.parentCategoryId = parentCategoryId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

