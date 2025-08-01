package ct222h.vegeta.projectbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OAuth2Config {

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret:}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id:}")
    private String facebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret:}")
    private String facebookClientSecret;

    @Bean
    @ConditionalOnProperty(
        value = "oauth2.enabled", 
        havingValue = "true", 
        matchIfMissing = true
    )
    public ClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = new ArrayList<>();

        // Add Google registration if credentials are available
        if (StringUtils.hasText(googleClientId) && StringUtils.hasText(googleClientSecret)) {
            registrations.add(googleClientRegistration());
        }

        // Add Facebook registration if credentials are available
        if (StringUtils.hasText(facebookClientId) && StringUtils.hasText(facebookClientSecret)) {
            registrations.add(facebookClientRegistration());
        }

        // If no OAuth2 providers are configured, return empty repository
        if (registrations.isEmpty()) {
            // Create a dummy registration to avoid Spring boot auto-configuration errors
            return new InMemoryClientRegistrationRepository();
        }

        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8081/login/oauth2/code/google")
                .scope("openid", "profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName("sub")
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

    private ClientRegistration facebookClientRegistration() {
        return ClientRegistration.withRegistrationId("facebook")
                .clientId(facebookClientId)
                .clientSecret(facebookClientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8081/login/oauth2/code/facebook")
                .scope("email", "public_profile")
                .authorizationUri("https://www.facebook.com/v3.0/dialog/oauth")
                .tokenUri("https://graph.facebook.com/v3.0/oauth/access_token")
                .userInfoUri("https://graph.facebook.com/me?fields=id,name,email")
                .userNameAttributeName("id")
                .clientName("Facebook")
                .build();
    }
}
