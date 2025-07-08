package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findBySlug(String slug);
    List<Category> findByParentCategoryId(String parentCategoryId);
    boolean existsBySlug(String slug);
}
