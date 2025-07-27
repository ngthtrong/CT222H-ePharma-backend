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
}
