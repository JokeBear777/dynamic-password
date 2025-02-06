package com.InfoSec.dynamic_password.domain.password.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResponseDto {

    private String siteName;

    private String siteAddress;

    private String password;

    private String loginId;

    private Date updatedAt;

    private Date createdAt;
}
