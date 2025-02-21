package com.InfoSec.dynamic_password.domain.twoFA.controller;

import com.InfoSec.dynamic_password.domain.encrypt.service.RsaService;
import com.InfoSec.dynamic_password.domain.twoFA.service.TotpServiceTemplate;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import com.InfoSec.dynamic_password.global.utils.dto.MessageStatusResponseDto;
import com.InfoSec.dynamic_password.global.utils.dto.StatusResponseDto;
import com.InfoSec.dynamic_password.global.utils.service.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/2FA/totp")
@RequiredArgsConstructor
@Slf4j
public class TotpController {

    private final TotpServiceTemplate totpServiceTemplate;
    private final RsaService rsaService;
    private final EncryptionService encryptionService;

    @PostMapping("/secret")
    public ResponseEntity<?> generateOtpSecretKey(
            @AuthenticationPrincipal SecurityUserDto securityUserDto
    ) {
        String secretKey = totpServiceTemplate.generateOtpSecretKey(securityUserDto.getUserId());
        String pubKey = rsaService.getRsaPublicKeyByMemberId(securityUserDto.getUserId());
        String encryptKey = encryptionService.encryptWithRsaPublicKey(secretKey, pubKey);
        totpServiceTemplate.saveOtpSecretKey(securityUserDto.getUserId(), encryptKey);

        return ResponseEntity.ok(StatusResponseDto.success(encryptKey));
    }

    @PostMapping("/verify/{otp_code}")
    public ResponseEntity<?> verifyOtpCode(
            @AuthenticationPrincipal SecurityUserDto securityUserDto,
            @PathVariable("otp_code") String otpCode
    ) {
        totpServiceTemplate.verifyOtpCode(securityUserDto.getUserId(), otpCode);
        return ResponseEntity.ok(MessageStatusResponseDto.success("OTP verification successful"));
    }


}
