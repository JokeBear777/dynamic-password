package com.InfoSec.dynamic_password.domain.password.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestGeneratePasswordDto {

    @NotBlank(message = "비밀번호 최대 사이즈는 필수 입니다.")
    private Integer maxSize;

    @NotBlank(message = "대문자 허용여부는 필수입니다.")
    private Boolean requireUpperCase;

    @NotBlank(message = "소문자 허용여부는 필수입니다.")
    private Boolean requireLowerCase;

    @NotBlank(message = "숫자 허용여부는 필수입니다.")
    private Boolean requireNumbers;

    @NotBlank(message = "특수문자 허용여부는 필수입니다.")
    private Boolean requireSpecialCharacters;

    @Size(max = 255, message = "특수문자 종류는 최대 255자까지 허용됩니다.")
    private String allowedSpecialCharacters;

    @AssertTrue(message = "특수문자가 허용되면, 가능한 특수문자 종류 입력은 필수입니다.")
    public boolean isAllowedSpecialCharactersValid() {
        return !requireSpecialCharacters
                || (allowedSpecialCharacters != null && !allowedSpecialCharacters.isEmpty());
    }
}
