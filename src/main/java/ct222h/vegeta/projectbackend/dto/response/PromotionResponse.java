package ct222h.vegeta.projectbackend.dto.response;

import java.util.Date;
import java.util.List;

public class PromotionResponse {
    private String id;
    private String name;
    private String description;
    private String bannerImageUrl;
    private Date startDate;
    private Date endDate;
    private Boolean isActive;
    private List<String> applicableProductIds;
    private String promoImageOverlay;
    private String promoIcon;
    private Boolean isCurrentlyActive;
    
    public PromotionResponse() {}
    
    public PromotionResponse(String id, String name, String description, String bannerImageUrl,
                           Date startDate, Date endDate, Boolean isActive, List<String> applicableProductIds,
                           String promoImageOverlay, String promoIcon, Boolean isCurrentlyActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bannerImageUrl = bannerImageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.applicableProductIds = applicableProductIds;
        this.promoImageOverlay = promoImageOverlay;
        this.promoIcon = promoIcon;
        this.isCurrentlyActive = isCurrentlyActive;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }
    
    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    
    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public List<String> getApplicableProductIds() { return applicableProductIds; }
    public void setApplicableProductIds(List<String> applicableProductIds) { this.applicableProductIds = applicableProductIds; }
    
    public String getPromoImageOverlay() { return promoImageOverlay; }
    public void setPromoImageOverlay(String promoImageOverlay) { this.promoImageOverlay = promoImageOverlay; }
    
    public String getPromoIcon() { return promoIcon; }
    public void setPromoIcon(String promoIcon) { this.promoIcon = promoIcon; }
    
    public Boolean getIsCurrentlyActive() { return isCurrentlyActive; }
    public void setIsCurrentlyActive(Boolean isCurrentlyActive) { this.isCurrentlyActive = isCurrentlyActive; }
}
