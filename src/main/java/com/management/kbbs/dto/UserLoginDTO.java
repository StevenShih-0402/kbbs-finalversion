package com.management.kbbs.dto;
// 填入用戶登入帳密的 DTO

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
    private String username;     // 用戶名稱
    private String password;     // 用戶密碼
}