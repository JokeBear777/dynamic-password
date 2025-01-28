package com.InfoSec.dynamic_password.global.security.config;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.xml.bind.DatatypeConverter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {
    private String issuer;
    private String secretKey;
    private Long expired;

    public Key getSecretKey() {
        byte[] keyBytes = DatatypeConverter.parseBase64Binary(secretKey);
        if (keyBytes == null || keyBytes.length <32) {
            throw new IllegalArgumentException("Secret key length is incorrect");
        }
        return new SecretKeySpec(keyBytes, SignatureAlgorithm   .HS256.getJcaName());
    }
}
