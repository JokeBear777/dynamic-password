package com.InfoSec.dynamic_password.domain.encrypt.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveRsaPublicDto {

    @NotBlank(message = "공개키를 입력하십시오")
    private String publicKey;
}
