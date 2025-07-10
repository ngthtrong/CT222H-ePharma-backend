package ct222h.vegeta.projectbackend.dto.response;

import ct222h.vegeta.projectbackend.model.User;

import java.util.Date;
import java.util.List;

public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private String authProvider;
    private List<User.Address> addresses;
    private Date createdAt;
    private Date updatedAt;

    public UserResponse(String id, String fullName, String email, String phoneNumber,
                        String role, String authProvider, List<User.Address> addresses,
                        Date createdAt, Date updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.authProvider = authProvider;
        this.addresses = addresses;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters, setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(String authProvider) {
        this.authProvider = authProvider;
    }

    public List<User.Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<User.Address> addresses) {
        this.addresses = addresses;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
