package ct222h.vegeta.projectbackend.model;

public class OAuth2UserInfo {
    
    private String id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String picture;
    private String provider;
    private boolean emailVerified;

    // Constructors
    public OAuth2UserInfo() {}

    public OAuth2UserInfo(String id, String email, String name, String picture, String provider) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.provider = provider;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    // Helper method to get full name
    public String getFullName() {
        if (name != null && !name.trim().isEmpty()) {
            return name;
        }
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        if (firstName != null) {
            return firstName;
        }
        return email != null ? email.split("@")[0] : "User";
    }
}
