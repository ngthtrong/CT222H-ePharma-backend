package ct222h.vegeta.projectbackend.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterRequest {
    
    @Pattern(regexp = "PENDING|PROCESSING|SHIPPED|COMPLETED|CANCELLED", 
             message = "Trạng thái đơn hàng không hợp lệ")
    private String status;
    
    private String userId;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    
    private String search; // Search by order code or recipient name
    
    // Pagination parameters
    private Integer page = 0;
    private Integer size = 20;
    
    // Sorting parameters
    private String sortBy = "createdAt"; // createdAt, totalAmount, status
    private String sortDirection = "desc"; // asc, desc
}
