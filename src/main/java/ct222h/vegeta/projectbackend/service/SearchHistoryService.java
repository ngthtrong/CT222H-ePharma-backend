package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.constants.SearchHistoryConstants;
import ct222h.vegeta.projectbackend.model.SearchHistory;
import ct222h.vegeta.projectbackend.repository.SearchHistoryRepository;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final UserRepository userRepository;

    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository, UserRepository userRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get search history for a specific user
     */
    public List<SearchHistory> getSearchHistoryByUserId(String userId) {
        validateUserExists(userId);
        return searchHistoryRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    /**
     * Get recent search history for a specific user (limit to 10)
     */
    public List<SearchHistory> getRecentSearchHistory(String userId) {
        validateUserExists(userId);
        return searchHistoryRepository.findTop10ByUserIdOrderByTimestampDesc(userId);
    }

    /**
     * Save a new search history entry
     */
    public SearchHistory saveSearchHistory(String userId, String searchQuery, Map<String, Object> searchFilters) {
        validateUserExists(userId);
        validateSearchQuery(searchQuery);
        
        // Check if this exact search already exists recently
        List<SearchHistory> recentSearches = searchHistoryRepository.findTop10ByUserIdOrderByTimestampDesc(userId);
        for (SearchHistory search : recentSearches) {
            if (search.getSearchQuery().equals(searchQuery) && 
                areFiltersEqual(search.getSearchFilters(), searchFilters)) {
                // Update timestamp instead of creating duplicate
                search.setTimestamp(new Date());
                return searchHistoryRepository.save(search);
            }
        }
        
        // Create new search history entry
        SearchHistory searchHistory = new SearchHistory(userId, searchQuery, searchFilters);
        return searchHistoryRepository.save(searchHistory);
    }

    /**
     * Update clicked products for a search history entry
     */
    public SearchHistory updateClickedProducts(String searchHistoryId, String userId, List<String> clickedProducts) {
        SearchHistory searchHistory = getSearchHistoryByIdAndUserId(searchHistoryId, userId);
        searchHistory.setClickedProducts(clickedProducts);
        return searchHistoryRepository.save(searchHistory);
    }

    /**
     * Delete a specific search history entry
     */
    public void deleteSearchHistory(String searchHistoryId, String userId) {
        SearchHistory searchHistory = getSearchHistoryByIdAndUserId(searchHistoryId, userId);
        searchHistoryRepository.delete(searchHistory);
    }

    /**
     * Clear all search history for a user
     */
    public void clearSearchHistory(String userId) {
        validateUserExists(userId);
        List<SearchHistory> userSearchHistory = searchHistoryRepository.findByUserId(userId);
        searchHistoryRepository.deleteAll(userSearchHistory);
    }

    /**
     * Get search history within a date range
     */
    public List<SearchHistory> getSearchHistoryByDateRange(String userId, Date startDate, Date endDate) {
        validateUserExists(userId);
        return searchHistoryRepository.findByUserIdAndTimestampBetween(userId, startDate, endDate);
    }

    /**
     * Get popular search queries (for suggestions)
     */
    public List<String> getPopularSearchQueries(String userId, int limit) {
        List<SearchHistory> searchHistory = searchHistoryRepository.findByUserIdOrderByTimestampDesc(userId);
        return searchHistory.stream()
                .map(SearchHistory::getSearchQuery)
                .distinct()
                .limit(limit)
                .toList();
    }

    // Helper methods

    private SearchHistory getSearchHistoryByIdAndUserId(String id, String userId) {
        Optional<SearchHistory> searchHistory = searchHistoryRepository.findById(id);
        
        if (searchHistory.isEmpty()) {
            throw new RuntimeException(SearchHistoryConstants.ERROR_SEARCH_HISTORY_NOT_FOUND);
        }
        
        SearchHistory history = searchHistory.get();
        if (!history.getUserId().equals(userId)) {
            throw new SecurityException(SearchHistoryConstants.ERROR_UNAUTHORIZED_ACCESS);
        }
        
        return history;
    }

    private void validateUserExists(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException(SearchHistoryConstants.ERROR_USER_NOT_FOUND);
        }
    }

    private void validateSearchQuery(String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            throw new IllegalArgumentException(SearchHistoryConstants.ERROR_SEARCH_QUERY_REQUIRED);
        }
        
        if (searchQuery.length() > SearchHistoryConstants.MAX_SEARCH_QUERY_LENGTH) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm quá dài");
        }
    }

    private boolean areFiltersEqual(Map<String, Object> filters1, Map<String, Object> filters2) {
        if (filters1 == null && filters2 == null) {
            return true;
        }
        if (filters1 == null || filters2 == null) {
            return false;
        }
        return filters1.equals(filters2);
    }
}
