package com.management.kbbs.service;

import com.management.kbbs.dto.UserChangePasswordDTO;
import com.management.kbbs.dto.UserDTO;
import com.management.kbbs.dto.UserLoginDTO;
import com.management.kbbs.entity.User;
import com.management.kbbs.repository.UserRepository;

import com.management.kbbs.security.JwtTokenProvider;
import com.management.kbbs.security.TokenStore;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;


    // 註冊新的用戶
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        // 密碼加密
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encryptedPassword);

        User savedUser = userRepository.save(setNewUser(userDTO));
        return convertToDTO(savedUser);
    }

    // 用戶登入
    @Transactional
    public Map<String, String> loginUser(UserLoginDTO userLoginDTO) {
        // 確認用戶存在
        User user = userRepository.findByName(userLoginDTO.getUsername())
                                  .orElseThrow(() -> new RuntimeException("User not found"));

        // 驗證密碼
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 生成 Token
        String token = jwtTokenProvider.createToken(user.getName(), user.getPermission());

        // 將 Token 儲存至 Redis，Key 為 Token，Value 為用戶名，並設定過期時間
        tokenStore.storeToken(user.getName(),  token, 1L, TimeUnit.HOURS);

        // 返回 JSON 結構
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("token", token);

        return response;
    }

    // 用戶登出
    @Transactional
    public String logoutUser(String token) {
        // 假設 Token 格式是 "Bearer <actual_token>"
        if (token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);

            // 從 Redis 中刪除該 Token
            redisTemplate.delete(actualToken);

            return "Logout successful";
        } else {
            return "Invalid token format";
        }
    }

    // 查詢所有用戶
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
    }

    // 根據ID查詢單一用戶
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new RuntimeException("User not found with id: " + id));  // 或者可以自訂異常處理
        return convertToDTO(user);
    }

    // 更新用戶
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existUser = userRepository.findById(id)
                                       .orElseThrow(() -> new RuntimeException("User not found with id: " + id));  // 或者可以自訂異常處理

        editUser(existUser, userDTO);

        User updatedUser = userRepository.save(existUser);
        return convertToDTO(updatedUser);
    }

    // 刪除用戶
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
        }
        userRepository.deleteById(id);
    }

    // 讓用戶修改個人的密碼
    @Transactional
    public String changePasswordByUser(String token, UserChangePasswordDTO userChangePasswordDTO){
        // 獲取當前登入使用者的名稱
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByName(userChangePasswordDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found with User: " + userChangePasswordDTO.getUsername()));

        user.setPassword(passwordEncoder.encode(userChangePasswordDTO.getNewPassword()));
        userRepository.save(user);

        // 修改密碼後自動讓使用者登出，假設 Token 格式是 "Bearer <actual_token>"
        if (token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);

            // 從 Redis 中刪除該 Token
            redisTemplate.delete(actualToken);
        }

        return "密碼修改成功！";
    }








    // 將 User 轉換為 UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setPassword(user.getPassword());
        userDTO.setPermission(user.getPermission());
        userDTO.setCreateAt(user.getCreatedAt());
        return userDTO;
    }

    // 創建(註冊)用戶的資料轉換
    private User setNewUser(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setPassword(userDTO.getPassword());

        return user;
    }

    // 更新用戶的資料轉換
    private void editUser(User existUser, UserDTO userDTO){
        existUser.setName(userDTO.getName());
        existUser.setEmail(userDTO.getEmail());
        existUser.setPhone(userDTO.getPhone());
        existUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        existUser.setPermission(userDTO.getPermission());
    }
}
