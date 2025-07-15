package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateRequest {
    
    @NotBlank(message = "Trạng thái đơn hàng không được để trống")
    @Pattern(regexp = "PENDING|PROCESSING|SHIPPED|COMPLETED|CANCELLED", 
             message = "Trạng thái đơn hàng không hợp lệ")
    private String status;
    
    private String notes;
}
