package com.InfoSec.dynamic_password.domain.password.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDto {

    @Size(max = 255, message = "비밀번호는 최대 255자까지 허용됩니다.")
    private String password;

    @Size(max = 255, message = "사이트 이름은 최대 255자까지 허용됩니다.")
    private String siteName;

    @Size(max = 255, message = "사이트 주소는 최대 255자까지 허용됩니다.")
    private String siteAddress;
}
