package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReplyRequest {
    @NotBlank(message = "Nội dung trả lời không được để trống")
    @Size(max = 1000, message = "Nội dung trả lời không được vượt quá 1000 ký tự")
    private String responseText;
}
