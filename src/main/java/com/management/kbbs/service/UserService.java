package com.management.kbbs.service;

import com.management.kbbs.dto.UserDTO;
import com.management.kbbs.entity.User;
import com.management.kbbs.repository.UserRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 創建用戶
    public UserDTO createUser(UserDTO userDTO) {
        User savedUser = userRepository.save(setNewUser(userDTO));
        return convertToDTO(savedUser);
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
//        if (userOptional.isPresent()) {
//            return convertToDTO(userOptional.get());
//        } else {
//            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
//        }
    }

    // 更新用戶
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existUser = userRepository.findById(id)
                                       .orElseThrow(() -> new RuntimeException("User not found with id: " + id));  // 或者可以自訂異常處理

        editUser(existUser, userDTO);

        User updatedUser = userRepository.save(existUser);
        return convertToDTO(updatedUser);

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
    }

    // 刪除用戶
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
        }
        userRepository.deleteById(id);
    }






    // 將 User 轉換為 UserDTO
    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setCreateAt(user.getCreatedAt());
        return userDTO;
    }

    // 創建用戶的資料轉換
    private User setNewUser(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        return user;
    }

    // 更新用戶的資料轉換
    private void editUser(User existUser, UserDTO userDTO){
        existUser.setName(userDTO.getName());
        existUser.setEmail(userDTO.getEmail());
        existUser.setPhone(userDTO.getPhone());
    }
}
