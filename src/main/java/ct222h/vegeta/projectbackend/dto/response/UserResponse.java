package ct222h.vegeta.projectbackend.dto.response;

public class UserResponse {
    private String userId;
    private String fullName;
    private String email;
    private String role;

    public UserResponse(String userId, String fullName, String email, String role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}
