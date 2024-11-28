package com.management.kbbs.service;

import com.management.kbbs.dto.UserDTO;
import com.management.kbbs.entity.User;
import com.management.kbbs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 創建用戶
    public UserDTO createUser(UserDTO userDTO) {
//        User user = new User();
//        user.setName(userDTO.getName());
//        user.setEmail(userDTO.getEmail());
//        user.setPhone(userDTO.getPhone());
//
//        User savedUser = userRepository.save(user);
//        return convertToDTO(savedUser);
        return null;
    }

    // 查詢所有用戶
    public List<UserDTO> getAllUsers() {
//        List<User> users = userRepository.findAll();
//        return users.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
        return null;
    }

    // 根據ID查詢單一用戶
    public UserDTO getUserById(Long id) {
//        Optional<User> userOptional = userRepository.findById(id);
//        if (userOptional.isPresent()) {
//            return convertToDTO(userOptional.get());
//        } else {
//            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
//        }
        return null;
    }

    // 更新用戶
    public UserDTO updateUser(Long id, UserDTO userDTO) {
//        Optional<User> userOptional = userRepository.findById(id);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            user.setName(userDTO.getName());
//            user.setEmail(userDTO.getEmail());
//            user.setPhone(userDTO.getPhone());
//
//            User updatedUser = userRepository.save(user);
//            return convertToDTO(updatedUser);
//        } else {
//            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
//        }
        return null;
    }

    // 刪除用戶
    public void deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
        }
    }

    // 將 User 轉換為 UserDTO
    private UserDTO convertToDTO(User user) {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(user.getId());
//        userDTO.setName(user.getName());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setPhone(user.getPhone());
//        return userDTO;
        return null;
    }
}
