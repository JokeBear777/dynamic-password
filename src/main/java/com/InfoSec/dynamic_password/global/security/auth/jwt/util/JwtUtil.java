package com.InfoSec.dynamic_password.global.security.auth.jwt.util;

import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.GeneratedToken;
import com.InfoSec.dynamic_password.global.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    //private final RefreshTokenService tokenService; //애플리케이션의 의도에 맞게 리프레시 토큰은 넣지 않음, 추후 넣을 수도 있음
    private Key secretKey;

    @PostConstruct
    protected void init() {
        secretKey = jwtProperties.getSecretKey();
    }

    public GeneratedToken generateToken(String email, String role) {
        String accessToken = generateAccessToken(email, role);

        return new GeneratedToken(accessToken);
    }

    public String generateAccessToken(String email, String role) {
        long tokenPeriod = jwtProperties.getExpired();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", "ROLE_" + role);

        Date now = new Date();
        return
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(secretKey,SignatureAlgorithm.HS256)
                        .compact();
    }

    public boolean verifyToken(String token) {
        try{
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(token);

            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUid(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody().get("role",String.class);
    }


}
