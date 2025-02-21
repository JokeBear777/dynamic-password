package com.InfoSec.dynamic_password.global.utils.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;


@Slf4j
public class TotpUtil {
    private static final Duration TIME_STEP = Duration.ofSeconds(30); // 30초마다 새로운 OTP
    private static final int OTP_LENGTH = 6;  // 6자리 OTP
    private static final String HMAC_ALGORITHM = "HmacSHA1";


    public static String generateTotp(String Base64PlainSecretKey) {
        byte[] decodeKey = Base64.getDecoder().decode(Base64PlainSecretKey);
        try{
            SecretKeySpec keySpec = new SecretKeySpec(decodeKey, HMAC_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(keySpec);

            long timeCounter = Instant.now().getEpochSecond() / TIME_STEP.getSeconds();
            byte[] timeBytes =  ByteBuffer.allocate(8).putLong(timeCounter).array();

            byte[] hash = mac.doFinal(timeBytes);

            int offset = hash[hash.length - 1] & 0xF;
            int binary = ((hash[offset] & 0x7F) << 24) |
                    ((hash[offset + 1] & 0xFF) << 16) |
                    ((hash[offset + 2] & 0xFF) << 8) |
                    (hash[offset + 3] & 0xFF);

            int otp = binary % (int) Math.pow(10, OTP_LENGTH);
            log.info("generate OTP Secret Key : {}",  otp);
            return String.format("%06d", otp);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate OTP",e);
        }


    }
}
