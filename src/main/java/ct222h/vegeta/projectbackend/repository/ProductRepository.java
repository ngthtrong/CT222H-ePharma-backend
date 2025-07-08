package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategoryId(String categoryId);
    List<Product> findByIsPublished(Boolean isPublished);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findBySkuContainingIgnoreCase(String sku);
    List<Product> findByBrandContainingIgnoreCase(String brand);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
    
    @Query("{'isPublished': true, 'stockQuantity': {$gt: 0}}")
    List<Product> findAvailableProducts();
    
    @Query("{'categoryId': ?0, 'isPublished': true}")
    List<Product> findPublishedProductsByCategory(String categoryId);
}
