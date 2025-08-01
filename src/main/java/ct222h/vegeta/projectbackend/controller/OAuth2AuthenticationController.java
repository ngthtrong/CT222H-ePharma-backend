package ct222h.vegeta.projectbackend.controller;

import ct222h.vegeta.projectbackend.dto.request.OAuth2CallbackRequest;
import ct222h.vegeta.projectbackend.dto.response.ApiResponse;
import ct222h.vegeta.projectbackend.dto.response.LoginResponse;
import ct222h.vegeta.projectbackend.dto.response.OAuth2LoginResponse;
import ct222h.vegeta.projectbackend.service.OAuth2UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/auth/oauth2")
@Tag(name = "OAuth2 Authentication", description = "API xác thực OAuth2 với Google và Facebook")
public class OAuth2AuthenticationController {

    @Autowired
    private OAuth2UserService oauth2UserService;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${oauth2.frontend.base-url:https://project-front-end-nine.vercel.app/}")
    private String frontendBaseUrl;

    @Value("${oauth2.frontend.callback-path:/auth/oauth2/success}")
    private String frontendCallbackPath;

    @Operation(
        summary = "Khởi tạo đăng nhập OAuth2",
        description = "Tạo URL authorization để chuyển hướng người dùng đến nhà cung cấp OAuth2"
    )
    @GetMapping("/login/{provider}")
    public ApiResponse<OAuth2LoginResponse> initiateOAuth2Login(
            @Parameter(description = "Nhà cung cấp OAuth2 (google hoặc facebook)", example = "google")
            @PathVariable String provider) {
        
        try {
            OAuth2LoginResponse response = oauth2UserService.getAuthorizationUrl(provider);
            return new ApiResponse<>(true, "URL đăng nhập OAuth2 đã được tạo", response);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Lỗi tạo URL đăng nhập OAuth2", null);
        }
    }

    @Operation(
        summary = "Xử lý callback OAuth2",
        description = "Xử lý authorization code từ nhà cung cấp OAuth2 và tạo JWT token"
    )
    @PostMapping("/callback/{provider}")
    public ApiResponse<LoginResponse> handleOAuth2Callback(
            @Parameter(description = "Nhà cung cấp OAuth2 (google hoặc facebook)", example = "google")
            @PathVariable String provider,
            @Parameter(description = "Thông tin callback từ OAuth2 provider")
            @Valid @RequestBody OAuth2CallbackRequest request) {
        
        try {
            LoginResponse response = oauth2UserService.processOAuth2Login(provider, request);
            return new ApiResponse<>(true, "Đăng nhập OAuth2 thành công", response);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (RuntimeException e) {
            return new ApiResponse<>(false, e.getMessage(), null);
        } catch (Exception e) {
            return new ApiResponse<>(false, "Lỗi xử lý đăng nhập OAuth2", null);
        }
    }

    @Operation(
        summary = "Xử lý callback OAuth2 GET",
        description = "Xử lý authorization code từ OAuth2 provider redirect và redirect về frontend"
    )
    @GetMapping("/callback/{provider}")
    public ResponseEntity<Void> handleOAuth2CallbackGet(
            @Parameter(description = "Nhà cung cấp OAuth2 (google hoặc facebook)", example = "google")
            @PathVariable String provider,
            @Parameter(description = "Authorization code từ OAuth2 provider")
            @RequestParam("code") String code,
            @Parameter(description = "State parameter để validate request")
            @RequestParam("state") String state,
            @Parameter(description = "Error code nếu có")
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request) {
        
        try {
            // Kiểm tra error từ OAuth2 provider
            if (error != null) {
                String errorUrl = buildFrontendErrorUrl(error, provider);
                return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(errorUrl))
                    .build();
            }
            
            // Tạo request object từ query parameters
            OAuth2CallbackRequest callbackRequest = new OAuth2CallbackRequest();
            callbackRequest.setCode(code);
            callbackRequest.setState(state);
            
            // Xử lý OAuth2 login
            LoginResponse loginResponse = oauth2UserService.processOAuth2Login(provider, callbackRequest);
            
            // Build frontend redirect URL với token và user data
            String frontendUrl = buildFrontendRedirectUrl(loginResponse, provider);
            
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendUrl))
                .build();
                
        } catch (IllegalArgumentException e) {
            String errorUrl = buildFrontendErrorUrl(e.getMessage(), provider);
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(errorUrl))
                .build();
        } catch (RuntimeException e) {
            String errorUrl = buildFrontendErrorUrl(e.getMessage(), provider);
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(errorUrl))
                .build();
        } catch (Exception e) {
            String errorUrl = buildFrontendErrorUrl("Lỗi xử lý đăng nhập OAuth2", provider);
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(errorUrl))
                .build();
        }
    }

    @Operation(
        summary = "Kiểm tra trạng thái OAuth2",
        description = "Kiểm tra các nhà cung cấp OAuth2 đã được cấu hình"
    )
    @GetMapping("/status")
    public ApiResponse<String> getOAuth2Status() {
        return new ApiResponse<>(true, "OAuth2 đã được cấu hình", 
            "Providers: google, facebook");
    }

    /**
     * Build frontend redirect URL với authentication data
     */
    private String buildFrontendRedirectUrl(LoginResponse loginResponse, String provider) {
        try {
            String userDataJson = objectMapper.writeValueAsString(loginResponse.getUser());
            String encodedUserData = URLEncoder.encode(userDataJson, StandardCharsets.UTF_8);
            
            return String.format(
                "%s%s?token=%s&user=%s&provider=%s",
                frontendBaseUrl,
                frontendCallbackPath,
                loginResponse.getAccessToken(),
                encodedUserData,
                provider
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing user data for frontend redirect", e);
        } catch (Exception e) {
            throw new RuntimeException("Error building frontend redirect URL", e);
        }
    }

    /**
     * Build frontend error URL
     */
    private String buildFrontendErrorUrl(String errorMessage, String provider) {
        try {
            String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
            return String.format(
                "%s%s?error=%s&provider=%s",
                frontendBaseUrl,
                frontendCallbackPath,
                encodedError,
                provider
            );
        } catch (Exception e) {
            // Fallback URL nếu encoding fails
            return String.format(
                "%s%s?error=OAuth2_Error&provider=%s",
                frontendBaseUrl,
                frontendCallbackPath,
                provider
            );
        }
    }
}
