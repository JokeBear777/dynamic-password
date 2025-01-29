package com.InfoSec.dynamic_password.domain.auth.dto;

import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.GeneratedToken;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponseDto {
    GeneratedToken generatedToken;
    Long Expired;
}
