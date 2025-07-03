package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.request.*;
import ct222h.vegeta.projectbackend.dto.response.*;
import ct222h.vegeta.projectbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse response = authService.register(request);
        return new ApiResponse<>(true, "Đăng ký thành công", response);
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return new ApiResponse<>(true, "Đăng nhập thành công", response);
    }
}
