package com.management.kbbs.dto;
// 讓用戶修改密碼的 DTO

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDTO {
    private String username;
    private String newPassword;
}
