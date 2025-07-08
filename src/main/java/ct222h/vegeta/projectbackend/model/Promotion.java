package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "promotions")
public class Promotion {
    @Id
    private String id;
    private String name;
    private String description;
    private String bannerImageUrl;
    private Date startDate;
    private Date endDate;
    private Boolean isActive = true;
    private List<String> applicableProductIds;
    private String promoImageOverlay;
    private String promoIcon;

    // Constructors
    public Promotion() {}

    public Promotion(String name, String description, Date startDate, Date endDate, List<String> applicableProductIds) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.applicableProductIds = applicableProductIds;
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

    // Helper method to check if promotion is currently active
    public boolean isCurrentlyActive() {
        Date now = new Date();
        return isActive && startDate.before(now) && endDate.after(now);
    }
}
