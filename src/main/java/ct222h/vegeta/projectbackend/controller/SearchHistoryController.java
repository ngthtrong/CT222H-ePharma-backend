package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.SearchHistoryConstants;
import ct222h.vegeta.projectbackend.dto.request.SearchHistoryRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.SearchHistoryResponse;
import ct222h.vegeta.projectbackend.model.SearchHistory;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.SearchHistoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;
    
    @Autowired
    private AuthorizationService authorizationService;

    public SearchHistoryController(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    // USER ENDPOINTS - Require USER or ADMIN role

    @GetMapping("/search-history")
    public ResponseEntity<ApiResponse<List<SearchHistoryResponse>>> getUserSearchHistory(
            @RequestParam(required = false, defaultValue = "false") boolean recent) {
        
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String currentUserId = authorizationService.getUserIdFromPrincipal();
            
            List<SearchHistory> searchHistories;
            if (recent) {
                searchHistories = searchHistoryService.getRecentSearchHistory(currentUserId);
            } else {
                searchHistories = searchHistoryService.getSearchHistoryByUserId(currentUserId);
            }
            
            List<SearchHistoryResponse> responses = searchHistories.stream()
                    .map(this::convertToSearchHistoryResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(true, SearchHistoryConstants.SUCCESS_GET_SEARCH_HISTORY, responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy lịch sử tìm kiếm: " + e.getMessage(), null));
        }
    }

    @GetMapping("/search-history/date-range")
    public ResponseEntity<ApiResponse<List<SearchHistoryResponse>>> getSearchHistoryByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String currentUserId = authorizationService.getUserIdFromPrincipal();
            
            List<SearchHistory> searchHistories = searchHistoryService.getSearchHistoryByDateRange(currentUserId, startDate, endDate);
            List<SearchHistoryResponse> responses = searchHistories.stream()
                    .map(this::convertToSearchHistoryResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(true, SearchHistoryConstants.SUCCESS_GET_SEARCH_HISTORY, responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy lịch sử tìm kiếm: " + e.getMessage(), null));
        }
    }

    @GetMapping("/search-history/popular")
    public ResponseEntity<ApiResponse<List<String>>> getPopularSearchQueries(
            @RequestParam(required = false, defaultValue = "10") int limit) {
        
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String currentUserId = authorizationService.getUserIdFromPrincipal();
            
            List<String> popularQueries = searchHistoryService.getPopularSearchQueries(currentUserId, limit);

            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy từ khóa phổ biến thành công", popularQueries));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy từ khóa phổ biến: " + e.getMessage(), null));
        }
    }

    @PostMapping("/search-history")
    public ResponseEntity<ApiResponse<SearchHistoryResponse>> saveSearchHistory(
            @Valid @RequestBody SearchHistoryRequest request) {
        
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String currentUserId = authorizationService.getUserIdFromPrincipal();
            
            SearchHistory searchHistory = searchHistoryService.saveSearchHistory(
                    currentUserId, 
                    request.getSearchQuery(), 
                    request.getSearchFilters()
            );

            SearchHistoryResponse response = convertToSearchHistoryResponse(searchHistory);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, SearchHistoryConstants.SUCCESS_SAVE_SEARCH_HISTORY, response));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lưu lịch sử tìm kiếm: " + e.getMessage(), null));
        }
    }

    @PutMapping("/search-history/{id}/clicked-products")
    public ResponseEntity<ApiResponse<SearchHistoryResponse>> updateClickedProducts(
            @PathVariable String id,
            @RequestBody List<String> clickedProducts) {
        
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String currentUserId = authorizationService.getUserIdFromPrincipal();
            
            SearchHistory updatedHistory = searchHistoryService.updateClickedProducts(id, currentUserId, clickedProducts);
            SearchHistoryResponse response = convertToSearchHistoryResponse(updatedHistory);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Cập nhật sản phẩm đã click thành công", response));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi cập nhật lịch sử: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/search-history/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSearchHistory(@PathVariable String id) {
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String currentUserId = authorizationService.getUserIdFromPrincipal();
            
            searchHistoryService.deleteSearchHistory(id, currentUserId);
            return ResponseEntity.ok(new ApiResponse<>(true, SearchHistoryConstants.SUCCESS_DELETE_SEARCH_HISTORY, null));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi xóa lịch sử tìm kiếm: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/search-history")
    public ResponseEntity<ApiResponse<Void>> clearSearchHistory() {
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String currentUserId = authorizationService.getUserIdFromPrincipal();
            
            searchHistoryService.clearSearchHistory(currentUserId);
            return ResponseEntity.ok(new ApiResponse<>(true, SearchHistoryConstants.SUCCESS_CLEAR_SEARCH_HISTORY, null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi xóa toàn bộ lịch sử tìm kiếm: " + e.getMessage(), null));
        }
    }

    // Helper method
    private SearchHistoryResponse convertToSearchHistoryResponse(SearchHistory searchHistory) {
        return new SearchHistoryResponse(
                searchHistory.getId(),
                searchHistory.getUserId(),
                searchHistory.getSearchQuery(),
                searchHistory.getSearchFilters(),
                searchHistory.getClickedProducts(),
                searchHistory.getTimestamp()
        );
    }
}
