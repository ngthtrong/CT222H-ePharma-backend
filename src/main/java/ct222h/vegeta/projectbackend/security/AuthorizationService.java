package ct222h.vegeta.projectbackend.security;

import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    
    @Autowired
    private UserRepository userRepository;
    
    public void checkAdminRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Yêu cầu đăng nhập");
        }
        
        String email = authentication.getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Người dùng không tồn tại"));
        
        if (!"ADMIN".equals(user.getRole())) {
            throw new SecurityException("Chỉ admin mới có quyền truy cập");
        }
    }
    
    public void checkUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Yêu cầu đăng nhập");
        }
        
        String email = authentication.getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Người dùng không tồn tại"));
        
        if (!"USER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            throw new SecurityException("Không có quyền truy cập");
        }
    }
    
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        String email = authentication.getName();
        var user = userRepository.findByEmail(email).orElse(null);
        
        return user != null && "ADMIN".equals(user.getRole());
    }
    
    public boolean isUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        String email = authentication.getName();
        var user = userRepository.findByEmail(email).orElse(null);
        
        return user != null && ("USER".equals(user.getRole()) || "ADMIN".equals(user.getRole()));
    }
    
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        String email = authentication.getName();
        var user = userRepository.findByEmail(email).orElse(null);
        
        return user != null ? user.getRole() : null;
    }
    
    /**
     * Get current authenticated user's ID from security context
     * @return User ID string
     * @throws SecurityException if user is not authenticated or not found
     */
    public String getUserIdFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Yêu cầu đăng nhập");
        }
        
        String email = authentication.getName();
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new SecurityException("Người dùng không tồn tại"));
        
        return user.getId();
    }
    
    /**
     * Get current authenticated user's email from security context
     * @return User email string
     * @throws SecurityException if user is not authenticated
     */
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Yêu cầu đăng nhập");
        }
        
        return authentication.getName();
    }
    
    /**
     * Check if current user has permission to access specific user's data
     * Admin can access any user's data, regular users can only access their own data
     * @param targetUserId The user ID to check permission for
     * @throws SecurityException if access is denied
     */
    public void checkUserDataAccess(String targetUserId) {
        String currentUserId = getUserIdFromPrincipal();
        
        // If current user is admin, allow access to any user's data
        if (isAdmin()) {
            return;
        }
        
        // Otherwise, only allow access to own data
        if (!currentUserId.equals(targetUserId)) {
            throw new SecurityException("Không có quyền truy cập dữ liệu của người dùng khác");
        }
    }
}
