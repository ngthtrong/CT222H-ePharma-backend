package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class OAuth2CallbackRequest {
    
    @NotBlank(message = "Authorization code không được để trống")
    private String code;
    
    private String state;
    private String error;
    private String errorDescription;

    // Constructors
    public OAuth2CallbackRequest() {}

    public OAuth2CallbackRequest(String code, String state) {
        this.code = code;
        this.state = state;
    }

    // Getters and setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }
}
