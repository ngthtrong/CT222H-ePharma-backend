package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    @NotBlank(message = "Tiêu đề thông báo không được để trống")
    @Size(max = 200, message = "Tiêu đề thông báo không được vượt quá 200 ký tự")
    private String title;
    
    @NotBlank(message = "Nội dung thông báo không được để trống")
    @Size(max = 1000, message = "Nội dung thông báo không được vượt quá 1000 ký tự")
    private String message;
    
    @NotBlank(message = "Loại thông báo không được để trống")
    private String type;
    
    private String relatedId; // Optional - ID of related object
    
    private String userId; // Optional - for individual notification, if null = broadcast to all users
}
