package com.management.kbbs.controller;

import com.management.kbbs.dto.UserDTO;
import com.management.kbbs.dto.UserLoginDTO;
import com.management.kbbs.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 創建用戶
//    @PostMapping
//    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
//        UserDTO createdUser = userService.createUser(userDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
//    }

    // 註冊
    @PostMapping("/public/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.registerUser(userDTO));
    }

    // 登入
    @PostMapping("/public/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(userService.loginUser(userLoginDTO));
    }

    // 登出
//    @PostMapping("/logout")
//    public ResponseEntity<String> logoutUser(
//            @RequestParam String username,
//            @RequestHeader("Authorization") String authorizationHeader) {
//
//        // 從 Authorization Header 中提取 Token（格式為 "Bearer <token>"）
//        String token = authorizationHeader.replace("Bearer ", "").trim();
//
//        // 調用 Service 執行登出邏輯
//        userService.logoutUser(username, token);
//        return ResponseEntity.ok("Successfully logged out");
//    }

    // 查詢所有用戶
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 根據ID查詢單一用戶
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // 更新用戶
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // 刪除用戶
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
