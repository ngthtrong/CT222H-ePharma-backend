package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findBySlug(String slug);

    @Query("{ 'slug' : { $regex: ?0, $options: 'i' } }")
    Optional<Category> findBySlugIgnoreCase(String slug);
}


