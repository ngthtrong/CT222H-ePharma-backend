package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.constants.ReviewConstants;
import ct222h.vegeta.projectbackend.model.Review;
import ct222h.vegeta.projectbackend.model.Product;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.ReviewRepository;
import ct222h.vegeta.projectbackend.repository.ProductRepository;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public List<Review> getReviewsByProductId(String productId) {
        return reviewRepository.findByProductId(productId);
    }

    public Optional<Review> getReviewById(String id) {
        return reviewRepository.findById(id);
    }

    public Review createReview(Review review) {
        // Validate product exists
        if (!productRepository.existsById(review.getProductId())) {
            throw new RuntimeException(ReviewConstants.ERROR_PRODUCT_NOT_FOUND);
        }

        // Validate user exists
        if (!userRepository.existsById(review.getUserId())) {
            throw new RuntimeException(ReviewConstants.ERROR_USER_NOT_FOUND);
        }

        // Check if user already reviewed this product
        List<Review> existingReviews = reviewRepository.findByProductIdAndUserId(review.getProductId(), review.getUserId());
        if (!existingReviews.isEmpty()) {
            throw new RuntimeException(ReviewConstants.ERROR_DUPLICATE_REVIEW);
        }

        // Validate rating
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new RuntimeException(ReviewConstants.ERROR_INVALID_RATING);
        }

        review.setCreatedAt(new Date());
        return reviewRepository.save(review);
    }

    public Review replyToReview(String reviewId, String responseText) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException(ReviewConstants.ERROR_REVIEW_NOT_FOUND));

        Review.AdminReply adminReply = new Review.AdminReply(responseText, new Date());
        review.setAdminReply(adminReply);

        return reviewRepository.save(review);
    }

    public void deleteReview(String id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException(ReviewConstants.ERROR_REVIEW_NOT_FOUND);
        }
        reviewRepository.deleteById(id);
    }

    // Helper methods
    public String getProductNameById(String productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(Product::getName).orElse("Unknown Product");
    }

    public String getUserNameById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getFullName).orElse("Unknown User");
    }
}
