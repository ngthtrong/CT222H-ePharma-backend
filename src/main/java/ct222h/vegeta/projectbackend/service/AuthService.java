package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.response.*;
import ct222h.vegeta.projectbackend.dto.request.*;
import ct222h.vegeta.projectbackend.exception.DuplicateEmailException;
import ct222h.vegeta.projectbackend.exception.InvalidCredentialsException;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import ct222h.vegeta.projectbackend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserResponse register(RegisterRequest request) {
        // Kiểm tra email trùng lặp
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("Email đã được sử dụng.");
        }

        try {
            User user = new User();
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            
            // Set optional fields if provided
            if (request.getPhoneNumber() != null) {
                user.setPhoneNumber(request.getPhoneNumber());
            }
            if (request.getAddresses() != null) {
                user.setAddresses(request.getAddresses());
            }

            userRepository.save(user);

            return new UserResponse(
                    user.getId(), 
                    user.getFullName(), 
                    user.getEmail(), 
                    user.getPhoneNumber(),
                    user.getRole(),
                    user.getAuthProvider(),
                    user.getAddresses(),
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            );
        } catch (DuplicateKeyException e) {
            // Bắt duplicate key error từ MongoDB unique index
            throw new DuplicateEmailException("Email đã được sử dụng.");
        }
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email hoặc mật khẩu không đúng."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Email hoặc mật khẩu không đúng.");
        }

        // Xử lý tài khoản cũ - nếu thiếu thông tin, dùng email làm fallback
        String displayName = user.getFullName();
        if (displayName == null || displayName.trim().isEmpty()) {
            displayName = user.getEmail().split("@")[0]; // Lấy phần trước @ làm tên hiển thị
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        UserResponse userResponse = new UserResponse(
                user.getId(), 
                displayName, // Dùng displayName đã xử lý
                user.getEmail(), 
                user.getPhoneNumber(),
                user.getRole(),
                user.getAuthProvider(),
                user.getAddresses(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        return new LoginResponse(token, userResponse);
    }

    public void logout(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token không hợp lệ");
        }
        
        String token = authHeader.substring(7);
        
        // Kiểm tra token có hợp lệ không
        if (!jwtUtil.isTokenValid(token)) {
            throw new IllegalArgumentException("Token không hợp lệ hoặc đã hết hạn");
        }
        
        // Thêm token vào blacklist
        tokenBlacklistService.blacklistToken(token);
    }
}
