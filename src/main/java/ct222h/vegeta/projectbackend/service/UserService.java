package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Tạo user mới
    public User createUser(UserCreateRequest request) {
        // Kiểm tra email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + request.getEmail());
        }
        
        User user = new User(request.getName(), request.getEmail(), 
                           request.getAge(), request.getPhone());
        return userRepository.save(user);
    }
    
    // Lấy tất cả users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Lấy user theo ID
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }
    
    // Lấy user theo email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Tìm kiếm user theo tên
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }
    
    // Cập nhật user
    public User updateUser(String id, UserCreateRequest request) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Không tìm thấy user với ID: " + id);
        }
        
        User user = existingUser.get();
        
        // Kiểm tra email mới có trùng với user khác không
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại: " + request.getEmail());
        }
        
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        user.setPhone(request.getPhone());
        
        return userRepository.save(user);
    }
    
    // Xóa user
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy user với ID: " + id);
        }
        userRepository.deleteById(id);
    }
    
    // Đếm tổng số users
    public long countUsers() {
        return userRepository.count();
    }
}
