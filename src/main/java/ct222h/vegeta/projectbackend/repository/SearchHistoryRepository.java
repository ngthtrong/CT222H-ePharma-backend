package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.SearchHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SearchHistoryRepository extends MongoRepository<SearchHistory, String> {
    List<SearchHistory> findByUserId(String userId);
    List<SearchHistory> findByUserIdOrderByTimestampDesc(String userId);
    List<SearchHistory> findBySearchQueryContainingIgnoreCase(String searchQuery);
    
    @Query("{'userId': ?0, 'timestamp': {$gte: ?1, $lte: ?2}}")
    List<SearchHistory> findByUserIdAndTimestampBetween(String userId, Date startDate, Date endDate);
    
    @Query("{'userId': ?0}")
    List<SearchHistory> findRecentSearchesByUserId(String userId);
    
    @Query(value = "{'userId': ?0}", sort = "{'timestamp': -1}")
    List<SearchHistory> findTop10ByUserIdOrderByTimestampDesc(String userId);
}
