package com.InfoSec.dynamic_password.domain.password.controller;

import com.InfoSec.dynamic_password.domain.password.dto.RequestGeneratePasswordDto;
import com.InfoSec.dynamic_password.domain.password.service.PasswordService;
import com.InfoSec.dynamic_password.global.utils.dto.StatusResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Password API", description = "비밀번호 관련 api")
@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/generate")
    public ResponseEntity<?> generatePassword(
            //@AuthenticationPrincipal SecurityUserDto securityUserDto
            @Valid @RequestBody RequestGeneratePasswordDto requestGeneratePasswordDto
    ) {
        String password = passwordService.generatePassword(requestGeneratePasswordDto);
        return ResponseEntity.ok(StatusResponseDto.success(password));
    }

}
