package ct222h.vegeta.projectbackend.config;


import ct222h.vegeta.projectbackend.security.JwtAuthenticationFilter;
import ct222h.vegeta.projectbackend.security.JwtBlacklistFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtBlacklistFilter jwtBlacklistFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Swagger documentation - PUBLIC
                        .requestMatchers("/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html").permitAll()
                        
                        // Actuator endpoints - PUBLIC
                        .requestMatchers("/actuator/**").permitAll()
                        
                        // Authentication endpoints - PUBLIC
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                        
                        // Authentication endpoints - USER (authenticated)
                        .requestMatchers("/api/v1/auth/logout").authenticated()
                        
                        // Categories endpoints - PUBLIC
                        .requestMatchers("/api/v1/categories/**").permitAll()
                        
                        // Products endpoints - PUBLIC
                        .requestMatchers("/api/v1/products/**").permitAll()
                        
                        // Cart endpoints - PUBLIC/USER (no authentication required, handled by headers)
                        .requestMatchers("/api/v1/cart/**").permitAll()
                        
                        // User endpoints - USER (authenticated)
                        .requestMatchers("/api/v1/users/me/**").authenticated()
                        
                        // Order endpoints - USER (authenticated)
                        .requestMatchers("/api/v1/orders/**").authenticated()
                        
                        // Admin endpoints - ADMIN (will be checked in controllers)
                        .requestMatchers("/api/v1/admin/**").authenticated()
                        
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtBlacklistFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form.disable())
                .logout(logout -> logout.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
