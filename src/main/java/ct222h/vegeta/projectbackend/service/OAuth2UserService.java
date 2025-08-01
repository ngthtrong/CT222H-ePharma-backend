package ct222h.vegeta.projectbackend.service;

import ct222h.vegeta.projectbackend.dto.request.OAuth2CallbackRequest;
import ct222h.vegeta.projectbackend.dto.response.LoginResponse;
import ct222h.vegeta.projectbackend.dto.response.OAuth2LoginResponse;
import ct222h.vegeta.projectbackend.dto.response.UserResponse;
import ct222h.vegeta.projectbackend.model.OAuth2UserInfo;
import ct222h.vegeta.projectbackend.model.User;
import ct222h.vegeta.projectbackend.repository.UserRepository;
import ct222h.vegeta.projectbackend.security.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.UUID;

@Service
public class OAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret:}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id:}")
    private String facebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret:}")
    private String facebookClientSecret;

    @Value("${app.oauth2.redirect-uri:http://localhost:8080/api/v1/auth/oauth2/callback}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generate OAuth2 authorization URL
     */
    public OAuth2LoginResponse getAuthorizationUrl(String provider) {
        String state = UUID.randomUUID().toString();
        String authUrl;

        switch (provider.toLowerCase()) {
            case "google":
                authUrl = String.format(
                    "https://accounts.google.com/o/oauth2/v2/auth?" +
                    "client_id=%s&" +
                    "redirect_uri=%s/%s&" +
                    "scope=openid email profile&" +
                    "response_type=code&" +
                    "state=%s",
                    googleClientId, redirectUri, provider, state
                );
                break;
            case "facebook":
                authUrl = String.format(
                    "https://www.facebook.com/v18.0/dialog/oauth?" +
                    "client_id=%s&" +
                    "redirect_uri=%s/%s&" +
                    "scope=email,public_profile&" +
                    "response_type=code&" +
                    "state=%s",
                    facebookClientId, redirectUri, provider, state
                );
                break;
            default:
                throw new IllegalArgumentException("Nhà cung cấp OAuth2 không được hỗ trợ: " + provider);
        }

        return new OAuth2LoginResponse(authUrl, state, provider);
    }

    /**
     * Process OAuth2 login callback
     */
    public LoginResponse processOAuth2Login(String provider, OAuth2CallbackRequest request) {
        try {
            // Check for error in callback
            if (request.getError() != null) {
                throw new RuntimeException("OAuth2 error: " + request.getError() + " - " + request.getErrorDescription());
            }

            // Exchange authorization code for access token
            String accessToken = exchangeCodeForToken(provider, request.getCode());

            // Get user info from provider
            OAuth2UserInfo userInfo = getUserInfo(provider, accessToken);

            // Find or create user
            User user = findOrCreateUser(userInfo, provider);

            // Generate JWT token
            String jwtToken = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

            // Create user response
            UserResponse userResponse = new UserResponse(
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

            return new LoginResponse(jwtToken, userResponse);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xử lý đăng nhập OAuth2: " + e.getMessage(), e);
        }
    }

    /**
     * Exchange authorization code for access token
     */
    private String exchangeCodeForToken(String provider, String code) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("grant_type", "authorization_code");
            params.add("redirect_uri", redirectUri + "/" + provider);

            String tokenEndpoint;
            switch (provider.toLowerCase()) {
                case "google":
                    tokenEndpoint = "https://oauth2.googleapis.com/token";
                    params.add("client_id", googleClientId);
                    params.add("client_secret", googleClientSecret);
                    break;
                case "facebook":
                    tokenEndpoint = "https://graph.facebook.com/v18.0/oauth/access_token";
                    params.add("client_id", facebookClientId);
                    params.add("client_secret", facebookClientSecret);
                    break;
                default:
                    throw new IllegalArgumentException("Provider không được hỗ trợ: " + provider);
            }

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, request, String.class);

            JsonNode tokenResponse = objectMapper.readTree(response.getBody());
            return tokenResponse.get("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi trao đổi code cho access token: " + e.getMessage(), e);
        }
    }

    /**
     * Get user info from OAuth2 provider
     */
    private OAuth2UserInfo getUserInfo(String provider, String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            String userInfoEndpoint;
            switch (provider.toLowerCase()) {
                case "google":
                    userInfoEndpoint = "https://www.googleapis.com/oauth2/v2/userinfo";
                    break;
                case "facebook":
                    userInfoEndpoint = "https://graph.facebook.com/v18.0/me?fields=id,name,email,first_name,last_name,picture";
                    break;
                default:
                    throw new IllegalArgumentException("Provider không được hỗ trợ: " + provider);
            }

            ResponseEntity<String> response = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, entity, String.class);
            JsonNode userNode = objectMapper.readTree(response.getBody());

            OAuth2UserInfo userInfo = new OAuth2UserInfo();
            userInfo.setProvider(provider.toLowerCase());

            if ("google".equals(provider.toLowerCase())) {
                userInfo.setId(userNode.get("id").asText());
                userInfo.setEmail(userNode.get("email").asText());
                userInfo.setName(userNode.get("name").asText());
                userInfo.setFirstName(userNode.has("given_name") ? userNode.get("given_name").asText() : null);
                userInfo.setLastName(userNode.has("family_name") ? userNode.get("family_name").asText() : null);
                userInfo.setPicture(userNode.has("picture") ? userNode.get("picture").asText() : null);
                userInfo.setEmailVerified(userNode.has("verified_email") ? userNode.get("verified_email").asBoolean() : false);
            } else if ("facebook".equals(provider.toLowerCase())) {
                userInfo.setId(userNode.get("id").asText());
                userInfo.setEmail(userNode.has("email") ? userNode.get("email").asText() : null);
                userInfo.setName(userNode.get("name").asText());
                userInfo.setFirstName(userNode.has("first_name") ? userNode.get("first_name").asText() : null);
                userInfo.setLastName(userNode.has("last_name") ? userNode.get("last_name").asText() : null);
                if (userNode.has("picture") && userNode.get("picture").has("data")) {
                    userInfo.setPicture(userNode.get("picture").get("data").get("url").asText());
                }
            }

            return userInfo;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi lấy thông tin người dùng: " + e.getMessage(), e);
        }
    }

    /**
     * Find existing user or create new OAuth2 user
     */
    private User findOrCreateUser(OAuth2UserInfo userInfo, String provider) {
        if (userInfo.getEmail() == null || userInfo.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email không được cung cấp bởi " + provider);
        }

        return userRepository.findByEmail(userInfo.getEmail())
            .map(existingUser -> updateExistingUser(existingUser, userInfo, provider))
            .orElseGet(() -> createNewOAuth2User(userInfo, provider));
    }

    /**
     * Update existing user with OAuth2 info
     */
    private User updateExistingUser(User existingUser, OAuth2UserInfo userInfo, String provider) {
        // Nếu user đã tồn tại với provider khác, cập nhật thông tin
        if ("local".equals(existingUser.getAuthProvider())) {
            existingUser.setAuthProvider(provider.toLowerCase());
        }
        
        // Cập nhật thông tin nếu cần
        if (existingUser.getFullName() == null || existingUser.getFullName().trim().isEmpty()) {
            existingUser.setFullName(userInfo.getFullName());
        }
        
        existingUser.setUpdatedAt(new Date());
        return userRepository.save(existingUser);
    }

    /**
     * Create new OAuth2 user
     */
    private User createNewOAuth2User(OAuth2UserInfo userInfo, String provider) {
        User user = new User();
        user.setEmail(userInfo.getEmail());
        user.setFullName(userInfo.getFullName());
        user.setAuthProvider(provider.toLowerCase());
        user.setRole("USER");
        // OAuth2 users don't have password
        user.setPassword("");
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        return userRepository.save(user);
    }
}
