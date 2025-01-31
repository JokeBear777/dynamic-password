package com.InfoSec.dynamic_password.domain.password.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGeneratePasswordDto {

    String password;

    Date createdAt;
}
