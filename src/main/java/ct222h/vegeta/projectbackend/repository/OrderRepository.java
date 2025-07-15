package ct222h.vegeta.projectbackend.repository;

import ct222h.vegeta.projectbackend.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    
    // Basic queries
    List<Order> findByUserId(String userId);
    List<Order> findByStatus(String status);
    Optional<Order> findByOrderCode(String orderCode);
    Optional<Order> findByTrackingNumber(String trackingNumber);
    List<Order> findByUserIdAndStatus(String userId, String status);
    List<Order> findByPaymentStatus(String paymentStatus);
    List<Order> findByPaymentMethod(String paymentMethod);
    
    // Existence checks
    boolean existsByOrderCode(String orderCode);
    boolean existsByTrackingNumber(String trackingNumber);
    
    // Date range queries using Instant
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Order> findOrdersByCreatedAtBetween(Instant startDate, Instant endDate);
    
    @Query("{'userId': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<Order> findUserOrdersByCreatedAtBetween(String userId, Instant startDate, Instant endDate);
    
    @Query("{'status': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<Order> findOrdersByStatusAndCreatedAtBetween(String status, Instant startDate, Instant endDate);
    
    // Legacy date queries for backward compatibility
    @Query("{'createdAt': {$gte: ?0, $lte: ?1}}")
    List<Order> findOrdersByDateRange(Date startDate, Date endDate);
    
    @Query("{'userId': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<Order> findUserOrdersByDateRange(String userId, Date startDate, Date endDate);
    
    @Query("{'status': ?0, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<Order> findOrdersByStatusAndDateRange(String status, Date startDate, Date endDate);
    
    // Advanced queries
    @Query("{'totalAmount': {$gte: ?0, $lte: ?1}}")
    List<Order> findOrdersByTotalAmountBetween(Double minAmount, Double maxAmount);
    
    @Query("{'userId': ?0, 'status': {$in: ?1}}")
    List<Order> findByUserIdAndStatusIn(String userId, List<String> statuses);
    
    @Query("{'status': ?0, 'paymentStatus': ?1}")
    List<Order> findByStatusAndPaymentStatus(String status, String paymentStatus);
    
    // Count queries
    long countByStatus(String status);
    long countByUserId(String userId);
    long countByPaymentStatus(String paymentStatus);
    
    @Query(value = "{'createdAt': {$gte: ?0, $lte: ?1}}", count = true)
    long countOrdersByCreatedAtBetween(Instant startDate, Instant endDate);
    
    // Advanced combined queries for admin filters
    @Query("{'userId': ?0, 'status': ?1, 'createdAt': {$gte: ?2, $lte: ?3}}")
    List<Order> findByUserIdAndStatusAndDateRange(String userId, String status, Date startDate, Date endDate);
    
    @Query("{'userId': ?0, 'status': ?1, 'createdAt': {$gte: ?2, $lte: ?3}}")
    List<Order> findByUserIdAndStatusAndCreatedAtBetween(String userId, String status, Instant startDate, Instant endDate);
    
    // Search by order code pattern (for admin search)
    @Query("{'orderCode': {$regex: ?0, $options: 'i'}}")
    List<Order> findByOrderCodeContainingIgnoreCase(String orderCodePattern);
    
    // Search by recipient name (for admin search)
    @Query("{'shippingAddress.recipientName': {$regex: ?0, $options: 'i'}}")
    List<Order> findByRecipientNameContainingIgnoreCase(String recipientName);
}
