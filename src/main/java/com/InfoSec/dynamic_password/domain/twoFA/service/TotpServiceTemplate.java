package com.InfoSec.dynamic_password.domain.twoFA.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@Slf4j
public abstract class TotpServiceTemplate {

    private static final int SECRET_KEY_LENGTH =  20;

    public String generateOtpSecretKey(Long memberId) {
        SecureRandom random = new SecureRandom();
        byte[] otp = new byte[SECRET_KEY_LENGTH];
        random.nextBytes(otp);
        log.info("Generate OTP Secret Key");
        return Base64.getEncoder().encodeToString(otp);
    }

    public abstract void saveOtpSecretKey(Long memberId, String otpSecretKey);
    public abstract void verifyOtpCode(Long memberId, String otpCode);
}
