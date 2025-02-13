package com.InfoSec.dynamic_password.domain.encrypt.controller;

import com.InfoSec.dynamic_password.domain.encrypt.dto.SaveRsaPublicDto;
import com.InfoSec.dynamic_password.domain.encrypt.service.RsaService;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import com.InfoSec.dynamic_password.global.utils.dto.StatusResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/encrypt/rsa")
@RequiredArgsConstructor
public class RsaController {

    private final RsaService rsaService;

    @PostMapping()
    public ResponseEntity<?> saveRsaPublic(
            @Valid @RequestBody SaveRsaPublicDto saveRsaPublicDto,
            @AuthenticationPrincipal SecurityUserDto securityUserDto
    ) {
        rsaService.saveRsaPublic(saveRsaPublicDto,securityUserDto);
        return ResponseEntity.ok(StatusResponseDto.success());
    }
}
