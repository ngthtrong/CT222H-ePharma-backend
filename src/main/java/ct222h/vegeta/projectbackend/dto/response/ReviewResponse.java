package ct222h.vegeta.projectbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String productId;
    private String productName;
    private String userId;
    private String userName;
    private Integer rating;
    private String comment;
    private AdminReplyResponse adminReply;
    private Date createdAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminReplyResponse {
        private String responseText;
        private Date repliedAt;
    }
}
