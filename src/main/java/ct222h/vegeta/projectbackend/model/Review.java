package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String productId;
    private String userId;
    private Integer rating; // 1-5 stars
    private String comment;
    private AdminReply adminReply;
    private Date createdAt = new Date();

    // Nested class for admin replies
    public static class AdminReply {
        private String responseText;
        private Date repliedAt;

        public AdminReply() {}

        public AdminReply(String responseText, Date repliedAt) {
            this.responseText = responseText;
            this.repliedAt = repliedAt;
        }

        // Getters and Setters
        public String getResponseText() { return responseText; }
        public void setResponseText(String responseText) { this.responseText = responseText; }

        public Date getRepliedAt() { return repliedAt; }
        public void setRepliedAt(Date repliedAt) { this.repliedAt = repliedAt; }
    }

    // Constructors
    public Review() {}

    public Review(String productId, String userId, Integer rating, String comment) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public AdminReply getAdminReply() { return adminReply; }
    public void setAdminReply(AdminReply adminReply) { this.adminReply = adminReply; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
