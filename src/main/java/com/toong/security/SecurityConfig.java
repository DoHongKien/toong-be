package com.toong.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoint
                        .requestMatchers("/api/v1/admin/auth/**").permitAll()
                        // Public Tour endpoints
                        .requestMatchers("/api/v1/tours/**").permitAll()
                        // Public Adventure Pass endpoints
                        .requestMatchers("/api/v1/adventure-passes/**").permitAll()
                        // Public Menu endpoints
                        .requestMatchers("/api/v1/menus/**").permitAll()
                        // Public Banner, Blog, FAQ, Contact
                        .requestMatchers("/api/v1/banners/**").permitAll()
                        .requestMatchers("/api/v1/blog-posts/**").permitAll()
                        .requestMatchers("/api/v1/faqs/**").permitAll()
                        .requestMatchers("/api/v1/contact/**").permitAll()
                        .requestMatchers("/api/v1/media/preview").permitAll()
                        // Migration endpoint — internal use only, no JWT required
                        .requestMatchers("/api/v1/migrate/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
