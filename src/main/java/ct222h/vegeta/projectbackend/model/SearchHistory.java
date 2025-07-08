package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(collection = "searchHistory")
public class SearchHistory {
    @Id
    private String id;
    private String userId;
    private String searchQuery;
    private Map<String, Object> searchFilters;
    private List<String> clickedProducts;
    private Date timestamp = new Date();

    // Constructors
    public SearchHistory() {}

    public SearchHistory(String userId, String searchQuery, Map<String, Object> searchFilters) {
        this.userId = userId;
        this.searchQuery = searchQuery;
        this.searchFilters = searchFilters;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public Map<String, Object> getSearchFilters() { return searchFilters; }
    public void setSearchFilters(Map<String, Object> searchFilters) { this.searchFilters = searchFilters; }

    public List<String> getClickedProducts() { return clickedProducts; }
    public void setClickedProducts(List<String> clickedProducts) { this.clickedProducts = clickedProducts; }

    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
