package com.InfoSec.dynamic_password.domain.auth.controller;

import com.InfoSec.dynamic_password.domain.auth.dto.JwtRequestDto;
import com.InfoSec.dynamic_password.domain.auth.dto.JwtResponseDto;
import com.InfoSec.dynamic_password.domain.auth.service.AuthService;
import com.InfoSec.dynamic_password.global.utils.dto.StatusResponseDto;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.GeneratedToken;
import com.InfoSec.dynamic_password.global.security.config.JwtProperties;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/generate-jwt")
    public ResponseEntity<?> generateJwt(
        @Valid @RequestBody JwtRequestDto requestDto
    ) {
        GeneratedToken token = authService.generateJwt(requestDto);

        JwtResponseDto jwtResponseDto = new JwtResponseDto(token, jwtProperties.getExpired());

        return ResponseEntity.ok(StatusResponseDto.success(jwtResponseDto));
    }
}
