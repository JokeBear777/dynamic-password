package com.InfoSec.dynamic_password.domain.password.controller;

import com.InfoSec.dynamic_password.domain.password.dto.PasswordListResponseDto;
import com.InfoSec.dynamic_password.domain.password.dto.RequestGeneratePasswordDto;
import com.InfoSec.dynamic_password.domain.password.dto.RequestSavePasswordDto;
import com.InfoSec.dynamic_password.domain.password.dto.UpdatePasswordDto;
import com.InfoSec.dynamic_password.domain.password.service.PasswordService;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import com.InfoSec.dynamic_password.global.utils.dto.StatusResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Password API", description = "비밀번호 관련 api")
@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/generate")
    public ResponseEntity<?> generatePassword(
            @Valid @RequestBody RequestGeneratePasswordDto requestGeneratePasswordDto
    ) {
        String password = passwordService.generatePassword(requestGeneratePasswordDto);
        return ResponseEntity.ok(StatusResponseDto.success(password));
    }


    @PostMapping("/save")
    public ResponseEntity<?> savePassword (
            @Valid @RequestBody RequestSavePasswordDto requestSavePasswordDto,
            @AuthenticationPrincipal SecurityUserDto securityUserDto
    ) {

        passwordService.savePassword(requestSavePasswordDto, securityUserDto);

        return ResponseEntity.ok(StatusResponseDto.success());
    }

    @DeleteMapping("/{password_id}")
    public ResponseEntity<?> deletePassword (
            @PathVariable("password_id") Long password_id,
            @AuthenticationPrincipal SecurityUserDto securityUserDto
    ) {

        passwordService.deletePassword(password_id, securityUserDto);

        return ResponseEntity.ok(StatusResponseDto.success());
    }

    @PatchMapping("/{password_id}")
    public ResponseEntity<?> patchUpdatePassword (
            @PathVariable("password_id") Long password_id,
            @AuthenticationPrincipal SecurityUserDto securityUserDto,
            @Valid @RequestBody UpdatePasswordDto updatePasswordDto
    ) {
        passwordService.patchUpdatePassword(password_id, securityUserDto,updatePasswordDto);
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    @GetMapping("/{member_id}")
    public ResponseEntity<?> GetPasswordListByMemberId (
            @Valid @PathVariable("member_id") Long member_id,
            @AuthenticationPrincipal SecurityUserDto securityUserDto
    ) {
        List<PasswordListResponseDto> passwordListResponseDtoList = passwordService
                .getPasswordListByMemberId(securityUserDto);
        return ResponseEntity.ok(StatusResponseDto.success(passwordListResponseDtoList));
    }


}
