package com.InfoSec.dynamic_password.password;

import com.InfoSec.dynamic_password.domain.member.service.MemberService;
import com.InfoSec.dynamic_password.domain.password.dto.RequestGeneratePasswordDto;
import com.InfoSec.dynamic_password.domain.password.service.PasswordService;
import com.InfoSec.dynamic_password.global.security.auth.jwt.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.internal.matchers.text.ValuePrinter.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PasswordTests {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock  // ✅ JwtUtil을 Mock 처리하여 실제 초기화 방지
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("비밀번호 생성 로직 단위 테스트")
    public void testGeneratePassword() {
        // Given
        RequestGeneratePasswordDto requestDto = new RequestGeneratePasswordDto();
        requestDto.setMaxSize(12);
        requestDto.setRequiredUpperCase(true);
        requestDto.setRequiredLowerCase(true);
        requestDto.setRequiredNumbers(true);
        requestDto.setRequiredSpecialCharacters(true);
        requestDto.setAllowedSpecialCharacters("!@#$%^&*");

        // When
        String password = passwordService.generatePassword(requestDto);

        // Then
        assertNotNull(password);
        System.out.println(password);
    }
}