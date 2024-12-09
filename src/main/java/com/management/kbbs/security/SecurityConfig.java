package com.management.kbbs.security;

import com.management.kbbs.service.CustomUserDetailsService;
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

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);

        return http.csrf(csrf -> csrf.disable()) // 禁用 CSRF
                   .authorizeHttpRequests(auth -> auth
                           // 公開功能
//                                   .requestMatchers("/api/books/add", "/api/users/register", "/api/users/login").permitAll()
                           .requestMatchers("/api/books",
                                   "/api/books/search",
                                   "/api/users/register",
                                   "/api/users/login",
                                   "/api/loanrecords/popularBooks",
                                   "/api/comments/books/{bookId}").permitAll() // 公開端點
                           // 註冊用戶功能
                           .requestMatchers("/api/loanrecords/borrow",
                                   "/api/loanrecords/return/{id}",
                                   "/api/comments/add").hasAnyAuthority("ROLE_MEMBER", "ROLE_ADMIN")
                           // 管理員功能
                           .requestMatchers("/api/books/add",
                                   "/api/books/{id}",
                                   "/api/books/update/{id}",
                                   "/api/books/delete/{id}",
                                   "/api/users",
                                   "/api/users/{id}",
                                   "/api/users/update/{id}",
                                   "/api/users/delete/{id}",
                                   "/api/loanrecords",
                                   "/api/loanrecords/{id}",
                                   "/api/loanrecords/update/{id}",
                                   "/api/loanrecords/delete/{id}",
                                   "/api/loanrecords/activeUsers",
                                   "/api/loanrecords/unreturnedBooks",
                                   "/api/comments",
                                   "/api/comments/update/{id}",
                                   "/api/comments/delete/{id}",
                                   "/api/stats").hasAnyAuthority("ROLE_ADMIN")
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

