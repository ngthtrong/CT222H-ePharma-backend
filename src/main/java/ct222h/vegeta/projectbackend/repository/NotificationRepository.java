package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserId(String userId);
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Notification> findByUserIdAndIsRead(String userId, Boolean isRead);
    List<Notification> findByType(String type);
    List<Notification> findByRelatedId(String relatedId);
    
    @Query("{'userId': ?0, 'isRead': false}")
    List<Notification> findUnreadNotificationsByUserId(String userId);
    
    @Query("{'userId': ?0, 'type': ?1}")
    List<Notification> findByUserIdAndType(String userId, String type);
    
    @Query(value = "{'userId': ?0}", sort = "{'createdAt': -1}")
    List<Notification> findTop10ByUserIdOrderByCreatedAtDesc(String userId);
}
