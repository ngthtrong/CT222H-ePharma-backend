package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.ReviewConstants;
import ct222h.vegeta.projectbackend.dto.request.ReviewRequest;
import ct222h.vegeta.projectbackend.dto.request.ReviewReplyRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.ReviewResponse;
import ct222h.vegeta.projectbackend.model.Review;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {

    private final ReviewService reviewService;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private UserRepository userRepository;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // PUBLIC ENDPOINTS - No authentication required

    @GetMapping("/products/{id}/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getProductReviews(@PathVariable String id) {
        List<Review> reviews = reviewService.getReviewsByProductId(id);
        List<ReviewResponse> responses = reviews.stream()
                .map(this::convertToReviewResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, ReviewConstants.SUCCESS_GET_REVIEWS, responses));
    }

    // USER ENDPOINTS - Require USER or ADMIN role

    @PostMapping("/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            Principal principal, 
            @Valid @RequestBody ReviewRequest request) {
        
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            // Get user ID from principal (this would need to be implemented based on your auth system)
            String userId = getUserIdFromPrincipal(principal);
            
            Review review = new Review(request.getProductId(), userId, request.getRating(), request.getComment());
            Review created = reviewService.createReview(review);
            
            return ResponseEntity.status(201).body(new ApiResponse<>(true, ReviewConstants.SUCCESS_CREATE_REVIEW, convertToReviewResponse(created)));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // ADMIN ENDPOINTS - Require ADMIN role

    @GetMapping("/admin/reviews")
    public ResponseEntity<ApiResponse<List<ReviewResponse>>> getAllReviews() {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        List<Review> reviews = reviewService.getAllReviews();
        List<ReviewResponse> responses = reviews.stream()
                .map(this::convertToReviewResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, ReviewConstants.SUCCESS_GET_REVIEWS, responses));
    }

    @PutMapping("/admin/reviews/{id}/reply")
    public ResponseEntity<ApiResponse<ReviewResponse>> replyToReview(
            @PathVariable String id, 
            @Valid @RequestBody ReviewReplyRequest request) {
        
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            Review updated = reviewService.replyToReview(id, request.getResponseText());
            return ResponseEntity.ok(new ApiResponse<>(true, ReviewConstants.SUCCESS_REPLY_REVIEW, convertToReviewResponse(updated)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/admin/reviews/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable String id) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok(new ApiResponse<>(true, ReviewConstants.SUCCESS_DELETE_REVIEW, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // HELPER METHODS

    private ReviewResponse convertToReviewResponse(Review review) {
        ReviewResponse.AdminReplyResponse adminReply = null;
        if (review.getAdminReply() != null) {
            adminReply = new ReviewResponse.AdminReplyResponse(
                    review.getAdminReply().getResponseText(),
                    review.getAdminReply().getRepliedAt()
            );
        }

        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                reviewService.getProductNameById(review.getProductId()),
                review.getUserId(),
                reviewService.getUserNameById(review.getUserId()),
                review.getRating(),
                review.getComment(),
                adminReply,
                review.getCreatedAt()
        );
    }

    private String getUserIdFromPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
