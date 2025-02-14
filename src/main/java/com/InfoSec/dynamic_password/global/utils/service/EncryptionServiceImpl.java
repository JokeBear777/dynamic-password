package com.InfoSec.dynamic_password.global.utils.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

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

    @Override
    public String encryptWithRsaPublicKey(String plainText, String base64PublicKey)  {
        try {
            PublicKey publicKey = base64Decode(base64PublicKey);

            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception e) {
            throw new RuntimeException(e + "EncryptWithRsaPublicKey Error");
        }
    }

    @Override
    public PublicKey base64Decode(String base64PublicKey) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(base64PublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (Exception e) {
            throw new RuntimeException(e + "Base64Decode Error");
        }
    }

}
