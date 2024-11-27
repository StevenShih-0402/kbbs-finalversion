package com.management.kbbs.service;

import com.management.kbbs.dto.MemberDTO;
import com.management.kbbs.entity.Member;
import com.management.kbbs.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private MemberRepository userRepository;

    // 創建用戶
    public MemberDTO createUser(MemberDTO userDTO) {
        Member user = new Member();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());

        Member savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    // 查詢所有用戶
    public List<MemberDTO> getAllUsers() {
        List<Member> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 根據ID查詢單一用戶
    public MemberDTO getUserById(Long id) {
        Optional<Member> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return convertToDTO(userOptional.get());
        } else {
            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
        }
    }

    // 更新用戶
    public MemberDTO updateUser(Long id, MemberDTO userDTO) {
        Optional<Member> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            Member user = userOptional.get();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());

            Member updatedUser = userRepository.save(user);
            return convertToDTO(updatedUser);
        } else {
            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
        }
    }

    // 刪除用戶
    public void deleteUser(Long id) {
        Optional<Member> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);  // 或者可以自訂異常處理
        }
    }

    // 將 User 轉換為 UserDTO
    private MemberDTO convertToDTO(Member user) {
        MemberDTO userDTO = new MemberDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        return userDTO;
    }
}
