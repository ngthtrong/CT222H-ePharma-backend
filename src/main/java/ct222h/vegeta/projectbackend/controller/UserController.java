package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.service.UserService;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Cho phép CORS từ mọi nguồn
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // API tạo user mới
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserCreateRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(ApiResponse.success("Tạo user thành công", user));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi khi tạo user: " + e.getMessage()));
        }
    }
    
    // API tạo nhiều users cho test
    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<List<User>>> createMultipleUsers(@RequestBody List<UserCreateRequest> requests) {
        try {
            List<User> createdUsers = requests.stream()
                    .map(request -> {
                        try {
                            return userService.createUser(request);
                        } catch (Exception e) {
                            // Skip users with errors (like duplicate email)
                            return null;
                        }
                    })
                    .filter(user -> user != null)
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success(
                    "Tạo thành công " + createdUsers.size() + "/" + requests.size() + " users", 
                    createdUsers));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi khi tạo users: " + e.getMessage()));
        }
    }
    
    // API lấy tất cả users
    @GetMapping
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success(users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi lấy danh sách users: " + e.getMessage()));
        }
    }
    
    // API lấy user theo ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable String id) {
        try {
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success(user.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi lấy user: " + e.getMessage()));
        }
    }
    
    // API tìm kiếm user theo tên
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<User>>> searchUsers(@RequestParam String name) {
        try {
            List<User> users = userService.searchUsersByName(name);
            return ResponseEntity.ok(ApiResponse.success(users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi tìm kiếm users: " + e.getMessage()));
        }
    }
    
    // API cập nhật user
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<User>> updateUser(@PathVariable String id, @RequestBody UserCreateRequest request) {
        try {
            User updatedUser = userService.updateUser(id, request);
            return ResponseEntity.ok(ApiResponse.success("Cập nhật user thành công", updatedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi khi cập nhật user: " + e.getMessage()));
        }
    }
    
    // API xóa user
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("Xóa user thành công", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi khi xóa user: " + e.getMessage()));
        }
    }
    
    // API đếm số lượng users
    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countUsers() {
        try {
            long count = userService.countUsers();
            return ResponseEntity.ok(ApiResponse.success("Tổng số users: " + count, count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Lỗi khi đếm users: " + e.getMessage()));
        }
    }
    
    // API tạo dữ liệu mẫu để test
    @PostMapping("/sample-data")
    public ResponseEntity<ApiResponse<List<User>>> createSampleData() {
        try {
            List<UserCreateRequest> sampleUsers = List.of(
                new UserCreateRequest("Nguyễn Văn A", "a@example.com", 25, "0123456789"),
                new UserCreateRequest("Trần Thị B", "b@example.com", 30, "0987654321"),
                new UserCreateRequest("Lê Văn C", "c@example.com", 28, "0111222333"),
                new UserCreateRequest("Phạm Thị D", "d@example.com", 22, "0444555666"),
                new UserCreateRequest("Hoàng Văn E", "e@example.com", 35, "0777888999")
            );
            
            List<User> createdUsers = sampleUsers.stream()
                    .map(request -> {
                        try {
                            return userService.createUser(request);
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(user -> user != null)
                    .toList();
            
            return ResponseEntity.ok(ApiResponse.success(
                    "Tạo thành công " + createdUsers.size() + " users mẫu", 
                    createdUsers));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Lỗi khi tạo dữ liệu mẫu: " + e.getMessage()));
        }
    }
}
