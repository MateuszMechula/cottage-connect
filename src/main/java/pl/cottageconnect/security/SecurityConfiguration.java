package pl.cottageconnect.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {
    public static final String ROLE_OWNER = "OWNER";
    public static final String ROLE_CUSTOMER = "CUSTOMER";

    private final JwtAuthFilter authFilter;
    private final CottageConnectUserDetailsService cottageConnectUserDetailsService;

    @Bean
    public UserDetailsService userDetailsService() {
        return cottageConnectUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/authenticate").permitAll()
                        .requestMatchers("/api/v1/users/account/details").hasAnyAuthority(ROLE_OWNER, ROLE_CUSTOMER)
                        .requestMatchers("/api/v1/users").authenticated()
                        .requestMatchers("/api/v1/villages/**", "/api/v1/village-posts/**").hasAuthority(ROLE_OWNER)
                        .requestMatchers("/api/v1/cottages/**").hasAuthority(ROLE_OWNER)
                        .requestMatchers("/api/v1/commentables/{commentableId}/comments", "/api/v1/comments/**")
                        .hasAnyAuthority(ROLE_OWNER, ROLE_CUSTOMER)
                        .requestMatchers("/api/v1/likes/**").hasAnyAuthority(ROLE_OWNER, ROLE_CUSTOMER)
                        .requestMatchers("/api/v1/photos/**").hasAnyAuthority(ROLE_OWNER, ROLE_CUSTOMER)
                        .requestMatchers("/api/v1/reservations/**").hasAnyAuthority(ROLE_OWNER, ROLE_CUSTOMER)
                        .requestMatchers("/images/**").hasAnyAuthority(ROLE_OWNER, ROLE_CUSTOMER)
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

