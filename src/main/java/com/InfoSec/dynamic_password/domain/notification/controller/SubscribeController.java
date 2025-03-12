package com.InfoSec.dynamic_password.domain.notification.controller;

import com.InfoSec.dynamic_password.domain.notification.dto.AlertDto;
import com.InfoSec.dynamic_password.domain.notification.service.AlertStorageService;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import com.InfoSec.dynamic_password.global.utils.dto.StatusResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class SubscribeController {

    private final AlertStorageService alertStorageService;

    @GetMapping("/list")
    public ResponseEntity<?> getNotifications(
            @AuthenticationPrincipal SecurityUserDto securityUserDto
    ) {
        List<String> notificationKeys = alertStorageService.getNotificationKeys(securityUserDto.getUserId());
        List<AlertDto> alerts = alertStorageService.getAlerts(notificationKeys);
        return ResponseEntity.ok(StatusResponseDto.success(alerts));
    }

}
