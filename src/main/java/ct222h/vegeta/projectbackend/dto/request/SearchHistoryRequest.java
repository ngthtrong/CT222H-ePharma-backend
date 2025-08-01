package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;

public class SearchHistoryRequest {
    
    @NotBlank(message = "Từ khóa tìm kiếm không được để trống")
    @Size(max = 200, message = "Từ khóa tìm kiếm không được vượt quá 200 ký tự")
    private String searchQuery;
    
    private Map<String, Object> searchFilters;
    private List<String> clickedProducts;

    // Constructors
    public SearchHistoryRequest() {}

    public SearchHistoryRequest(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public SearchHistoryRequest(String searchQuery, Map<String, Object> searchFilters) {
        this.searchQuery = searchQuery;
        this.searchFilters = searchFilters;
    }

    // Getters and Setters
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
}
