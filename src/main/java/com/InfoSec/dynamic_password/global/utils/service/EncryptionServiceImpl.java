package com.InfoSec.dynamic_password.global.utils.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EncryptionServiceImpl implements EncryptionService {

    @Override
    public String encrypt(String plainText)  {
        try {
            if (plainText == null || plainText.isEmpty()) {
                throw new IllegalArgumentException("암호화할 문자열이 없습니다.");
            }
            return plainText;
        } catch (Exception e) {
            log.error("암호화 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("암호화중 오류 발생");
        }
    }

    @Override
    public String decrypt(String encryptedText)  {
        if (encryptedText == null || encryptedText.isEmpty()) {
            throw new IllegalArgumentException("디코딩할 문자열이 없습니다.");
        }
        return encryptedText;
    }
}
