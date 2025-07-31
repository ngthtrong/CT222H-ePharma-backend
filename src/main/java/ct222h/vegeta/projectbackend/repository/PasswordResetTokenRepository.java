package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    
    Optional<PasswordResetToken> findByToken(String token);
    
    Optional<PasswordResetToken> findByUserId(String userId);
    
    void deleteByUserId(String userId);
    
    void deleteByExpiryDateBefore(LocalDateTime dateTime);
    
    boolean existsByToken(String token);
}
