package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.PromotionConstants;
import ct222h.vegeta.projectbackend.dto.request.PromotionRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.PromotionResponse;
import ct222h.vegeta.projectbackend.model.Promotion;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.PromotionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class PromotionController {

    private final PromotionService promotionService;
    
    @Autowired
    private AuthorizationService authorizationService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    // PUBLIC ENDPOINTS - No authentication required

    @GetMapping("/promotions")
    public ResponseEntity<ApiResponse<List<PromotionResponse>>> getActivePromotions() {
        List<Promotion> promotions = promotionService.getActivePromotions();
        List<PromotionResponse> responses = promotions.stream()
                .map(this::convertToPromotionResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, PromotionConstants.SUCCESS_GET_PROMOTIONS, responses));
    }

    // ADMIN ENDPOINTS - Require ADMIN role

    @GetMapping("/admin/promotions")
    public ResponseEntity<ApiResponse<List<PromotionResponse>>> getAllPromotions() {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        List<Promotion> promotions = promotionService.getAllPromotions();
        List<PromotionResponse> responses = promotions.stream()
                .map(this::convertToPromotionResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, PromotionConstants.SUCCESS_GET_PROMOTIONS, responses));
    }

    @GetMapping("/admin/promotions/{id}")
    public ResponseEntity<ApiResponse<PromotionResponse>> getPromotionById(@PathVariable String id) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        Optional<Promotion> promotion = promotionService.getPromotionById(id);
        return promotion
                .map(p -> ResponseEntity.ok(new ApiResponse<>(true, PromotionConstants.SUCCESS_GET_PROMOTION, convertToPromotionResponse(p))))
                .orElseGet(() -> ResponseEntity.status(404).body(new ApiResponse<>(false, PromotionConstants.ERROR_PROMOTION_NOT_FOUND, null)));
    }

    @PostMapping("/admin/promotions")
    public ResponseEntity<ApiResponse<PromotionResponse>> createPromotion(@Valid @RequestBody PromotionRequest request) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            Promotion promotion = convertToPromotion(request);
            Promotion created = promotionService.createPromotion(promotion);
            return ResponseEntity.status(201).body(new ApiResponse<>(true, PromotionConstants.SUCCESS_CREATE_PROMOTION, convertToPromotionResponse(created)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @PutMapping("/admin/promotions/{id}")
    public ResponseEntity<ApiResponse<PromotionResponse>> updatePromotion(
            @PathVariable String id,
            @Valid @RequestBody PromotionRequest request) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            Promotion promotion = convertToPromotion(request);
            Promotion updated = promotionService.updatePromotion(id, promotion);
            return ResponseEntity.ok(new ApiResponse<>(true, PromotionConstants.SUCCESS_UPDATE_PROMOTION, convertToPromotionResponse(updated)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/admin/promotions/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable String id) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            promotionService.deletePromotion(id);
            return ResponseEntity.ok(new ApiResponse<>(true, PromotionConstants.SUCCESS_DELETE_PROMOTION, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // HELPER METHODS

    private Promotion convertToPromotion(PromotionRequest request) {
        return new Promotion(
                request.getName(),
                request.getDescription(),
                request.getStartDate(),
                request.getEndDate(),
                request.getApplicableProductIds()
        );
    }

    private PromotionResponse convertToPromotionResponse(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getName(),
                promotion.getDescription(),
                promotion.getBannerImageUrl(),
                promotion.getStartDate(),
                promotion.getEndDate(),
                promotion.getIsActive(),
                promotion.getApplicableProductIds(),
                promotion.getPromoImageOverlay(),
                promotion.getPromoIcon(),
                promotion.isCurrentlyActive()
        );
    }
}
