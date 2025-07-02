package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    // Tìm user theo email
    Optional<User> findByEmail(String email);
    
    // Tìm user theo tên (case insensitive)
    List<User> findByNameContainingIgnoreCase(String name);
    
    // Tìm user theo độ tuổi trong khoảng
    List<User> findByAgeBetween(int minAge, int maxAge);
    
    // Kiểm tra email có tồn tại không
    boolean existsByEmail(String email);
}
