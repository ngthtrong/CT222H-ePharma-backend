package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByProductId(String productId);
    List<Review> findByUserId(String userId);
    List<Review> findByProductIdAndUserId(String productId, String userId);
    
    @Query("{'productId': ?0, 'rating': {$gte: ?1}}")
    List<Review> findByProductIdAndRatingGreaterThanEqual(String productId, Integer rating);
    
    @Query("{'productId': ?0}")
    List<Review> findReviewsByProductId(String productId);
    
    @Query(value = "{'productId': ?0}", fields = "{'rating': 1}")
    List<Review> findRatingsByProductId(String productId);
}
