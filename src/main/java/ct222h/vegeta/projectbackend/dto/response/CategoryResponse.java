package ct222h.vegeta.projectbackend.dto.response;

public class CategoryResponse {
    private String id;
    private String name;
    private String slug;
    private String description;
    private String parentCategoryId;
    private String parentCategoryName;

    public CategoryResponse() {}

    public CategoryResponse(String id, String name, String slug, String description, String parentCategoryId, String parentCategoryName) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
        this.parentCategoryName = parentCategoryName;
    }

    // Basic constructor
    public CategoryResponse(String id, String name, String slug, String description, String parentCategoryId) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parentCategoryId = parentCategoryId;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getParentCategoryId() { return parentCategoryId; }
    public void setParentCategoryId(String parentCategoryId) { this.parentCategoryId = parentCategoryId; }

    public String getParentCategoryName() { return parentCategoryName; }
    public void setParentCategoryName(String parentCategoryName) { this.parentCategoryName = parentCategoryName; }
}
