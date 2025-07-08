package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
    List<Order> findByStatus(String status);
    Optional<Order> findByOrderCode(String orderCode);
    List<Order> findByUserIdAndStatus(String userId, String status);
    
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Order> findOrdersByDateRange(Date startDate, Date endDate);
    
    @Query("{'userId': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<Order> findUserOrdersByDateRange(String userId, Date startDate, Date endDate);
    
    @Query("{'status': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<Order> findOrdersByStatusAndDateRange(String status, Date startDate, Date endDate);
}
