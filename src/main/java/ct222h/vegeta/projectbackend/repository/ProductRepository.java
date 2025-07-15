package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
    // Basic queries
    Optional<Product> findBySlug(String slug);
    Optional<Product> findBySku(String sku);
    
    // Existence checks
    boolean existsBySlug(String slug);
    boolean existsBySku(String sku);
    boolean existsByCategoryId(String categoryId);

    // Search queries
    List<Product> findByNameContainingIgnoreCaseOrSlugContainingIgnoreCase(String name, String slug);
    
    @Query("{'name': {$regex: ?0, $options: 'i'}}")
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Category-based queries
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByCategoryIdAndPublished(String categoryId, boolean published);
    
    // Brand queries
    List<Product> findByBrand(String brand);
    List<Product> findByBrandIgnoreCase(String brand);
    
    // Price range queries
    @Query("{'price': {$gte: ?0, $lte: ?1}}")
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Stock queries
    List<Product> findByStockQuantityGreaterThan(int minStock);
    List<Product> findByStockQuantityLessThanEqual(int maxStock);
    
    // Published status queries
    List<Product> findByPublished(boolean published);
    
    // Combined queries
    List<Product> findByCategoryIdAndPublishedAndStockQuantityGreaterThan(String categoryId, boolean published, int minStock);
}
