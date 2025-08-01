package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.constants.NotificationConstants;
import ct222h.vegeta.projectbackend.model.Notification;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.NotificationRepository;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> getNotificationById(String id) {
        return notificationRepository.findById(id);
    }

    public Notification createNotification(Notification notification) {
        // Validate notification type
        List<String> validTypes = Arrays.asList(
                NotificationConstants.TYPE_ORDER_STATUS,
                NotificationConstants.TYPE_PROMOTION,
                NotificationConstants.TYPE_SYSTEM,
                NotificationConstants.TYPE_GENERAL
        );
        
        if (!validTypes.contains(notification.getType())) {
            throw new RuntimeException(NotificationConstants.ERROR_INVALID_NOTIFICATION_TYPE);
        }

        // If userId is specified, validate user exists
        if (notification.getUserId() != null && !userRepository.existsById(notification.getUserId())) {
            throw new RuntimeException(NotificationConstants.ERROR_USER_NOT_FOUND);
        }

        notification.setCreatedAt(new Date());
        notification.setIsRead(false);
        
        return notificationRepository.save(notification);
    }

    public List<Notification> sendBroadcastNotification(String title, String message, String type, String relatedId) {
        List<User> allUsers = userRepository.findAll();
        List<Notification> notifications = allUsers.stream()
                .map(user -> {
                    Notification notification = new Notification(user.getId(), title, message, type, relatedId);
                    notification.setCreatedAt(new Date());
                    notification.setIsRead(false);
                    return notification;
                })
                .toList();

        return notificationRepository.saveAll(notifications);
    }

    public Notification markAsRead(String notificationId, String userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException(NotificationConstants.ERROR_NOTIFICATION_NOT_FOUND));

        // Check if the notification belongs to the user
        if (!notification.getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền truy cập thông báo này");
        }

        notification.markAsRead();
        return notificationRepository.save(notification);
    }

    public void deleteNotification(String id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException(NotificationConstants.ERROR_NOTIFICATION_NOT_FOUND);
        }
        notificationRepository.deleteById(id);
    }

    // Helper methods
    public String getUserNameById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(User::getFullName).orElse("Unknown User");
    }

    public long getUnreadNotificationCount(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findUnreadNotificationsByUserId(userId);
        return unreadNotifications.size();
    }
}
