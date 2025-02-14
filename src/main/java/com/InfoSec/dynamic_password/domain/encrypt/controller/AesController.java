package com.InfoSec.dynamic_password.domain.encrypt.controller;

import com.InfoSec.dynamic_password.domain.encrypt.service.AesService;
import com.InfoSec.dynamic_password.domain.encrypt.service.RsaService;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import com.InfoSec.dynamic_password.global.utils.dto.StatusResponseDto;
import com.InfoSec.dynamic_password.global.utils.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/encrypt/aes")
@RequiredArgsConstructor
public class AesController {

    private final AesService aesService;
    private final EncryptionService encryptionService;
    private final RsaService rsaService;

    @GetMapping("/generate")
    public ResponseEntity<?> generateAes(
            @AuthenticationPrincipal SecurityUserDto securityUserDto
    ) {
        String aes = aesService.generateAes();
        String rsaPublicKey = rsaService.getRsaPublicKeyByMemberId(securityUserDto.getUserId());
        String encryptedAes = encryptionService.encryptWithRsaPublicKey(aes, rsaPublicKey);
        aesService.save(encryptedAes, securityUserDto.getUserId());
        return ResponseEntity.ok(StatusResponseDto.success(encryptedAes));
    }

}
