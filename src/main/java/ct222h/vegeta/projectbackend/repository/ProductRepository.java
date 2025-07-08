package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findBySlug(String slug);
    boolean existsBySlug(String slug);
    boolean existsBySku(String sku);

    List<Product> findByNameContainingIgnoreCaseOrSlugContainingIgnoreCase(String name, String slug);

}
