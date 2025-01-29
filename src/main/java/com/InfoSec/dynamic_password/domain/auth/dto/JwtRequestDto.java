package com.InfoSec.dynamic_password.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequestDto {

    @NotBlank(message = "이메일은 필수 입니다.")
    @Size(max = 255, message = "이메일은 최대 255자까지 허용됩니다.")
    String email;

}
