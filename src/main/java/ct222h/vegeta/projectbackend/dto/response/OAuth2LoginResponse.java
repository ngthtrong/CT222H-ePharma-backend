package ct222h.vegeta.projectbackend.dto.response;

public class OAuth2LoginResponse {
    
    private String authorizationUrl;
    private String state;
    private String provider;

    // Constructors
    public OAuth2LoginResponse() {}

    public OAuth2LoginResponse(String authorizationUrl, String state, String provider) {
        this.authorizationUrl = authorizationUrl;
        this.state = state;
        this.provider = provider;
    }

    // Getters and setters
    public String getAuthorizationUrl() {
        return authorizationUrl;
    }

    public void setAuthorizationUrl(String authorizationUrl) {
        this.authorizationUrl = authorizationUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
