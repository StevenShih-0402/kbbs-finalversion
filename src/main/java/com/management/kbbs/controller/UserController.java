package com.management.kbbs.controller;

import com.management.kbbs.dto.UserChangePasswordDTO;
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
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PostMapping("/member/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.logoutUser(token));
    }

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

    // 用戶修改密碼
    @PreAuthorize("hasRole('ROLE_MEMBER')")
    @PatchMapping("/member/changepassword")
    public ResponseEntity<String> changePasswordByUser(@RequestHeader("Authorization") String token, @RequestBody UserChangePasswordDTO userChangePasswordDTO) {
        String feedback = userService.changePasswordByUser(token, userChangePasswordDTO);
        return ResponseEntity.ok(feedback);
    }
}
