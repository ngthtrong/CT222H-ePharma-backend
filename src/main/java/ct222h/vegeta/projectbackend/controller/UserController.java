package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.service.UserService;
import ct222h.vegeta.projectbackend.dto.UserCreateRequest;
import ct222h.vegeta.projectbackend.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getMyProfile(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = new UserResponse(user.getId(), user.getFullName(), user.getEmail(), user.getRole());
        return new ApiResponse<>(true, "Lấy thông tin thành công", response);
    }
}