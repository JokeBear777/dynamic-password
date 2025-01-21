package com.InfoSec.dynamic_password.global.security.auth.jwt.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityUserDto {
    private Long userId;
    private String email;
    private String mobile;
    private String role;
}
