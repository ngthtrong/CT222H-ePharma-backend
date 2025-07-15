package ct222h.vegeta.projectbackend.dto.response;

public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private UserResponse user;
    
    // Thêm thông tin cần thiết cho frontend
    private String username;  // Email được dùng làm username
    private String displayName; // Tên hiển thị
    
    public LoginResponse(String accessToken, UserResponse user) {
        this.accessToken = accessToken;
        this.user = user;
        this.username = user.getEmail(); // Email là username
        this.displayName = user.getFullName(); // Tên đầy đủ để hiển thị
    }

    // Getters and Setters
    public String getAccessToken() { 
        return accessToken; 
    }
    
    public void setAccessToken(String accessToken) { 
        this.accessToken = accessToken; 
    }
    
    public String getTokenType() { 
        return tokenType; 
    }
    
    public void setTokenType(String tokenType) { 
        this.tokenType = tokenType; 
    }
    
    public UserResponse getUser() { 
        return user; 
    }
    
    public void setUser(UserResponse user) { 
        this.user = user; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    public String getDisplayName() { 
        return displayName; 
    }
    
    public void setDisplayName(String displayName) { 
        this.displayName = displayName; 
    }
}
