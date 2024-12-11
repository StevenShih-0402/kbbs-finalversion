package com.management.kbbs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtTokenProvider);

        return http.csrf(csrf -> csrf.disable()) // 禁用 CSRF
                   .authorizeHttpRequests(auth -> auth
                           .requestMatchers("/api-docs",
                                   "/swagger-ui/**",
                                   "/api/books/public/**",
                                   "/api/users/public/**",
                                   "/api/loanrecords/public/**",
                                   "/api/comments/public/**").permitAll()
                           .requestMatchers("/api/books/member/**",
                                   "/api/users/member/**",
                                   "/api/loanrecords/member/**",
                                   "/api/comments/member/**").hasAuthority("ROLE_MEMBER")
                           .requestMatchers("/api/books/admin/**",
                                   "/api/users/admin/**",
                                   "/api/loanrecords/admin/**",
                                   "/api/comments/admin/**",
                                   "/api/stats/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()  // 其他端點需身份驗證，否則回傳 403 Forbidden
                   )
                   .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // 添加 JWT 過濾器
                   .build(); // 返回構建的 SecurityFilterChain
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}