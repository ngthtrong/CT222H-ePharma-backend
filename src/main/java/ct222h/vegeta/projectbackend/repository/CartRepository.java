package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findByUserId(String userId);
    Optional<Cart> findBySessionId(String sessionId);
    void deleteByUserId(String userId);
    void deleteBySessionId(String sessionId);
}
