package ct222h.vegeta.projectbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private String id;
    private String userId;
    private String userName;
    private String title;
    private String message;
    private String type;
    private String relatedId;
    private Boolean isRead;
    private Date createdAt;
}
