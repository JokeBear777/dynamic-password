package com.InfoSec.dynamic_password.domain.password.service;

import com.InfoSec.dynamic_password.domain.password.dto.RequestGeneratePasswordDto;
import com.InfoSec.dynamic_password.domain.password.dto.ResponseGeneratePasswordDto;
import com.InfoSec.dynamic_password.domain.password.repository.PasswordRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {

    //private final PasswordRepository passwordRepository;
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "1234567890";
    private final SecureRandom secureRandom = new SecureRandom();

    public String generatePassword(RequestGeneratePasswordDto requestGeneratePasswordDto) {

        validatePasswordRequest(requestGeneratePasswordDto);

        StringBuilder characters = new StringBuilder();

        if(requestGeneratePasswordDto.getRequiredLowerCase()) {
            characters.append(LOWERCASE);
        }
        if(requestGeneratePasswordDto.getRequiredUpperCase()) {
            characters.append(UPPERCASE);
        }
        if(requestGeneratePasswordDto.getRequiredNumbers()) {
            characters.append(NUMBERS);
        }
        if(requestGeneratePasswordDto.getRequiredSpecialCharacters()) {
            characters.append(requestGeneratePasswordDto.getAllowedSpecialCharacters());
        }

        StringBuilder password = new StringBuilder();
        int maxSize = requestGeneratePasswordDto.getMaxSize();

        for (int i = 0; i < maxSize; i++) {
            int index = secureRandom.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        log.info("Generated password: {}", password.toString());

        return password.toString();
    }

    private void validatePasswordRequest(RequestGeneratePasswordDto request) {
        if (!request.getRequiredLowerCase()
                &&!request.getRequiredUpperCase()
                &&!request.getRequiredNumbers()
                &&!request.getRequiredSpecialCharacters()
        ) {
            throw new IllegalArgumentException("적어도 하나이상의 조건은 허용되어야 합니다");
        }

        if (request.getRequiredSpecialCharacters() &&
                (request.getAllowedSpecialCharacters() == null || request.getAllowedSpecialCharacters().isEmpty())) {
            throw new IllegalArgumentException("특수문자가 포함되도록 설정했지만, 허용된 특수문자가 입력되지 않았습니다.");
        }

    }

}
