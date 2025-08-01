package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.constants.NotificationConstants;
import ct222h.vegeta.projectbackend.dto.request.NotificationRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.NotificationResponse;
import ct222h.vegeta.projectbackend.model.Notification;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import ct222h.vegeta.projectbackend.security.AuthorizationService;
import ct222h.vegeta.projectbackend.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class NotificationController {

    private final NotificationService notificationService;
    
    @Autowired
    private AuthorizationService authorizationService;
    
    @Autowired
    private UserRepository userRepository;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // USER ENDPOINTS - Require USER or ADMIN role

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserNotifications(Principal principal) {
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            // Get user ID from principal (this would need to be implemented based on your auth system)
            String userId = getUserIdFromPrincipal(principal);
            
            List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
            List<NotificationResponse> responses = notifications.stream()
                    .map(this::convertToNotificationResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(true, NotificationConstants.SUCCESS_GET_NOTIFICATIONS, responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy danh sách thông báo", null));
        }
    }

    @PatchMapping("/notifications/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markNotificationAsRead(
            @PathVariable String id, 
            Principal principal) {
        
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String userId = getUserIdFromPrincipal(principal);
            Notification updated = notificationService.markAsRead(id, userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, NotificationConstants.SUCCESS_MARK_READ, convertToNotificationResponse(updated)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/notifications/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadNotificationCount(Principal principal) {
        authorizationService.checkUserRole(); // USER or ADMIN can access
        
        try {
            String userId = getUserIdFromPrincipal(principal);
            long count = notificationService.getUnreadNotificationCount(userId);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy số thông báo chưa đọc thành công", count));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy số thông báo chưa đọc", null));
        }
    }

    // ADMIN ENDPOINTS - Require ADMIN role

    @PostMapping("/admin/notifications")
    public ResponseEntity<ApiResponse<Object>> sendNotification(@Valid @RequestBody NotificationRequest request) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            if (request.getUserId() != null) {
                // Send to specific user
                Notification notification = new Notification(
                        request.getUserId(), 
                        request.getTitle(), 
                        request.getMessage(), 
                        request.getType(), 
                        request.getRelatedId()
                );
                Notification created = notificationService.createNotification(notification);
                
                return ResponseEntity.status(201).body(new ApiResponse<>(true, NotificationConstants.SUCCESS_SEND_NOTIFICATION, convertToNotificationResponse(created)));
            } else {
                // Broadcast to all users
                List<Notification> notifications = notificationService.sendBroadcastNotification(
                        request.getTitle(), 
                        request.getMessage(), 
                        request.getType(), 
                        request.getRelatedId()
                );
                
                return ResponseEntity.status(201).body(new ApiResponse<>(true, NotificationConstants.SUCCESS_SEND_NOTIFICATION, notifications.size() + " thông báo đã được gửi"));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @GetMapping("/admin/notifications")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAllNotifications() {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            List<Notification> notifications = notificationService.getAllNotifications();
            List<NotificationResponse> responses = notifications.stream()
                    .map(this::convertToNotificationResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponse<>(true, NotificationConstants.SUCCESS_GET_NOTIFICATIONS, responses));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Lỗi khi lấy danh sách thông báo", null));
        }
    }

    @DeleteMapping("/admin/notifications/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable String id) {
        authorizationService.checkAdminRole(); // Only ADMIN can access
        
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok(new ApiResponse<>(true, NotificationConstants.SUCCESS_DELETE_NOTIFICATION, null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    // HELPER METHODS

    private NotificationResponse convertToNotificationResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notificationService.getUserNameById(notification.getUserId()),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getRelatedId(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }

    private String getUserIdFromPrincipal(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}
