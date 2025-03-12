package com.management.kbbs.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String permission;
    private LocalDateTime createAt;
}
