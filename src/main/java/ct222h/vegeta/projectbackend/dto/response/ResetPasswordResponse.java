package ct222h.vegeta.projectbackend.dto.response;

public class ResetPasswordResponse {
    
    private String message;
    private boolean success;

    public ResetPasswordResponse() {}

    public ResetPasswordResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
