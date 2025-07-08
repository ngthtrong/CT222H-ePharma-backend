package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PromotionRepository extends MongoRepository<Promotion, String> {
    List<Promotion> findByIsActive(Boolean isActive);
    
    @Query("{'isActive': true, 'startDate': {$lte: ?0}, 'endDate': {$gte: ?0}}")
    List<Promotion> findActivePromotionsByDate(Date currentDate);
    
    @Query("{'applicableProductIds': ?0}")
    List<Promotion> findPromotionsByProductId(String productId);
    
    @Query("{'applicableProductIds': ?0, 'isActive': true, 'startDate': {$lte: ?1}, 'endDate': {$gte: ?1}}")
    List<Promotion> findActivePromotionsByProductIdAndDate(String productId, Date currentDate);
}
