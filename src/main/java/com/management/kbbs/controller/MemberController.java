package com.management.kbbs.controller;

import com.management.kbbs.dto.MemberDTO;
import com.management.kbbs.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private MemberService userService;

    // 創建用戶
    @PostMapping
    public ResponseEntity<MemberDTO> createUser(@RequestBody MemberDTO userDTO) {
        MemberDTO createdUser = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // 查詢所有用戶
    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllUsers() {
        List<MemberDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 根據ID查詢單一用戶
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getUserById(@PathVariable Long id) {
        MemberDTO userDTO = userService.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    // 更新用戶
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO> updateUser(@PathVariable Long id, @RequestBody MemberDTO userDTO) {
        MemberDTO updatedUser = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // 刪除用戶
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
