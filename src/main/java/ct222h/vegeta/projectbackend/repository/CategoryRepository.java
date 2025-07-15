package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    
    // Basic queries
    Optional<Category> findBySlug(String slug);
    
    // Hierarchy queries
    List<Category> findByParentCategoryId(String parentCategoryId);
    List<Category> findByParentCategoryIdIsNull(); // Root categories
    
    // Existence checks
    boolean existsBySlug(String slug);
    boolean existsByParentCategoryId(String parentCategoryId);
    
    // Search queries
    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<Category> findByNameContainingIgnoreCase(String name);
    
    // Count queries
    long countByParentCategoryId(String parentCategoryId);
}
