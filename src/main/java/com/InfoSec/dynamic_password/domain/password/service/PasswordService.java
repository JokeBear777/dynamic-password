package com.InfoSec.dynamic_password.domain.password.service;

import com.InfoSec.dynamic_password.domain.password.dto.*;
import com.InfoSec.dynamic_password.domain.password.entity.Password;
import com.InfoSec.dynamic_password.domain.password.repository.PasswordRepository;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import com.InfoSec.dynamic_password.global.utils.service.DomainExtractor;
import com.InfoSec.dynamic_password.global.utils.service.EncryptionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordService {

    private final EncryptionService encryptionService;
    private final PasswordRepository passwordRepository;
    private final DomainExtractor domainExtractor;

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "1234567890";
    private final SecureRandom secureRandom = new SecureRandom();

    public String generatePassword(RequestGeneratePasswordDto requestGeneratePasswordDto) {

        validatePasswordRequest(requestGeneratePasswordDto);

        StringBuilder characters = new StringBuilder();

        if(requestGeneratePasswordDto.getRequireLowerCase()) {
            characters.append(LOWERCASE);
        }
        if(requestGeneratePasswordDto.getRequireUpperCase()) {
            characters.append(UPPERCASE);
        }
        if(requestGeneratePasswordDto.getRequireNumbers()) {
            characters.append(NUMBERS);
        }
        if(requestGeneratePasswordDto.getRequireSpecialCharacters()) {
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
        if (!request.getRequireLowerCase()
                &&!request.getRequireUpperCase()
                &&!request.getRequireNumbers()
                &&!request.getRequireSpecialCharacters()
        ) {
            throw new IllegalArgumentException("적어도 하나이상의 조건은 허용되어야 합니다");
        }

        if (request.getRequireSpecialCharacters() &&
                (request.getAllowedSpecialCharacters() == null || request.getAllowedSpecialCharacters().isEmpty())) {
            throw new IllegalArgumentException("특수문자가 포함되도록 설정했지만, 허용된 특수문자가 입력되지 않았습니다.");
        }

    }

    @Transactional
    public void savePassword(
            RequestSavePasswordDto requestSavePasswordDto,
            SecurityUserDto securityUserDto)
    {
        Long memberId = securityUserDto.getUserId();
        String siteName = requestSavePasswordDto.getSiteName();
        String siteAddress = null;

        try {
            siteAddress = domainExtractor.extractDomain(requestSavePasswordDto.getSiteAddress());
        } catch (URISyntaxException e) {
            log.error("도메인 추출 중 오류 발생: {}", e.getMessage());
            return;
        }

        String password;
        String loginId;
        
        //공개키로 암호화, 공개키로 전달할예정
        try {
            password = encryptionService.encrypt(requestSavePasswordDto.getPassword());
            loginId = encryptionService.encrypt(requestSavePasswordDto.getLoginId());
        } catch (Exception e) {
            log.error("비밀번호 암호화 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("암호화 실패: " + e.getMessage());
        }

        passwordRepository.save(
                Password.builder()
                        .memberId(memberId)
                        .siteName(siteName)
                        .siteAddress(siteAddress)
                        .password(password)
                        .loginId(loginId)
                        .build()
        );
        log.info("비밀번호 저장 완료");
    }

    @Transactional
    public void deletePassword(Long passwordId, SecurityUserDto securityUserDto) {
        Password password = passwordRepository.findById(passwordId)
                .orElseThrow(() -> new NotFoundException("저장된 비밀번호가 없습니다"));

        if (!password.getMemberId().equals(securityUserDto.getUserId())) {
            throw new AccessDeniedException("다른 사용자의 비밀번호를 삭제할 수 없습니다.");
        }

        passwordRepository.deleteById(passwordId);
    }

    @Transactional
    public void patchUpdatePassword(
            Long passwordId,
            SecurityUserDto securityUserDto,
            UpdatePasswordDto updatePasswordDto
    ) {
        Password password = passwordRepository.findById(passwordId)
                .orElseThrow(() -> new NotFoundException("저장된 비밀번호가 없습니다"));

        if (!password.getMemberId().equals(securityUserDto.getUserId())) {
            throw new AccessDeniedException("다른 사용자의 비밀번호를 수정할 수 없습니다.");
        }

        String encryptedPassword;

        if (updatePasswordDto.getSiteName() != null) {
            password.setSiteName(
                    updatePasswordDto.getSiteName()
            );
        }
        if (updatePasswordDto.getPassword() != null) {
            try {
                encryptedPassword = encryptionService.encrypt(updatePasswordDto.getPassword());
            } catch (Exception e) {
                throw new RuntimeException("암호화 실패: " + e.getMessage());
            }

            password.setPassword(encryptedPassword);
        }
        if (updatePasswordDto.getSiteAddress() != null) {
            password.setSiteAddress(
                    updatePasswordDto.getSiteAddress()
            );
        }

        passwordRepository.save(password);
    }

    @Transactional(readOnly = true)
    public List<PasswordListResponseDto> getPasswordListByMemberId(SecurityUserDto securityUserDto) {
        Long memberId = securityUserDto.getUserId();
        List<Password> passwordList = Optional.ofNullable(passwordRepository.findByMemberId(memberId))
                .orElse(Collections.emptyList());
        List<PasswordListResponseDto> passwordListResponseDtoList = new ArrayList<>();
        passwordList.forEach(password -> {
            PasswordListResponseDto passwordListResponseDto = new PasswordListResponseDto(
              password.getPasswordId(),
              password.getSiteName(),
              password.getSiteAddress(),
              password.getLoginId(),
              password.getUpdatedAt(),
              password.getCreatedAt()
            );

            passwordListResponseDtoList.add(passwordListResponseDto);
        });

        return passwordListResponseDtoList;
    }

}
