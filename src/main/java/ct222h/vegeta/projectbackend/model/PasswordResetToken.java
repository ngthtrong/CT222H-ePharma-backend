package ct222h.vegeta.projectbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Document(collection = "password_reset_tokens")
public class PasswordResetToken {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    @Indexed(unique = true)
    private String token;
    
    private LocalDateTime expiryDate;
    
    private boolean used;
    
    private LocalDateTime createdAt;

    public PasswordResetToken() {
        this.createdAt = LocalDateTime.now();
        this.used = false;
    }

    public PasswordResetToken(String userId, String token, LocalDateTime expiryDate) {
        this();
        this.userId = userId;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Utility methods
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    public boolean isValid() {
        return !isUsed() && !isExpired();
    }
}
