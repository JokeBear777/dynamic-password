package com.InfoSec.dynamic_password.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequestDto {

    @NotBlank(message = "이름은 필수 입니다.")
    @Size(max = 255, message = "이름은 최대 255자까지 허용됩니다.")
    private String name;

}
