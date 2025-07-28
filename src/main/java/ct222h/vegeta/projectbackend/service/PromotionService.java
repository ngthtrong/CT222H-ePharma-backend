package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.model.Promotion;
import ct222h.vegeta.projectbackend.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PromotionService {
    
    private final PromotionRepository promotionRepository;
    
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }
    
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
    
    public List<Promotion> getActivePromotions() {
        Date now = new Date();
        return promotionRepository.findAll().stream()
                .filter(promotion -> promotion.getIsActive())
                .filter(promotion -> promotion.getStartDate().before(now) && promotion.getEndDate().after(now))
                .collect(Collectors.toList());
    }
    
    public Optional<Promotion> getPromotionById(String id) {
        return promotionRepository.findById(id);
    }
    
    public Promotion createPromotion(Promotion promotion) {
        if (promotion.getStartDate().after(promotion.getEndDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        return promotionRepository.save(promotion);
    }
    
    public Promotion updatePromotion(String id, Promotion promotion) {
        Optional<Promotion> existingPromotion = promotionRepository.findById(id);
        if (existingPromotion.isEmpty()) {
            throw new RuntimeException("Không tìm thấy khuyến mãi với ID: " + id);
        }
        
        if (promotion.getStartDate().after(promotion.getEndDate())) {
            throw new IllegalArgumentException("Ngày kết thúc phải sau ngày bắt đầu");
        }
        
        promotion.setId(id);
        return promotionRepository.save(promotion);
    }
    
    public void deletePromotion(String id) {
        Optional<Promotion> existingPromotion = promotionRepository.findById(id);
        if (existingPromotion.isEmpty()) {
            throw new RuntimeException("Không tìm thấy khuyến mãi với ID: " + id);
        }
        promotionRepository.deleteById(id);
    }
    
    public List<Promotion> getPromotionsForProduct(String productId) {
        return getActivePromotions().stream()
                .filter(promotion -> promotion.getApplicableProductIds() != null && 
                                   promotion.getApplicableProductIds().contains(productId))
                .collect(Collectors.toList());
    }
}
