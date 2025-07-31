package ct222h.vegeta.projectbackend.dto.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SearchHistoryResponse {
    
    private String id;
    private String userId;
    private String searchQuery;
    private Map<String, Object> searchFilters;
    private List<String> clickedProducts;
    private Date timestamp;

    // Constructors
    public SearchHistoryResponse() {}

    public SearchHistoryResponse(String id, String userId, String searchQuery, 
                                Map<String, Object> searchFilters, 
                                List<String> clickedProducts, Date timestamp) {
        this.id = id;
        this.userId = userId;
        this.searchQuery = searchQuery;
        this.searchFilters = searchFilters;
        this.clickedProducts = clickedProducts;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public Map<String, Object> getSearchFilters() {
        return searchFilters;
    }

    public void setSearchFilters(Map<String, Object> searchFilters) {
        this.searchFilters = searchFilters;
    }

    public List<String> getClickedProducts() {
        return clickedProducts;
    }

    public void setClickedProducts(List<String> clickedProducts) {
        this.clickedProducts = clickedProducts;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
