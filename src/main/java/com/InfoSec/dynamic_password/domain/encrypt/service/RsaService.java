package com.InfoSec.dynamic_password.domain.encrypt.service;

import com.InfoSec.dynamic_password.domain.encrypt.dto.SaveRsaPublicDto;
import com.InfoSec.dynamic_password.domain.encrypt.entity.RsaPublicKeyEntity;
import com.InfoSec.dynamic_password.domain.encrypt.repository.RsaPublicKeyRepository;
import com.InfoSec.dynamic_password.domain.member.entity.Member;
import com.InfoSec.dynamic_password.domain.member.repository.MemberRepository;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import com.InfoSec.dynamic_password.global.utils.validator.RsaKeyValidator;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
@Slf4j
@AllArgsConstructor
public class RsaService {

    private final RsaPublicKeyRepository rsaPublicKeyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveRsaPublic(SaveRsaPublicDto saveRsaPublicDto, SecurityUserDto securityUserDto) {
        String publicKey = saveRsaPublicDto.getPublicKey();

        if (StringUtils.isBlank(publicKey)) {
            throw new IllegalArgumentException("Public key is empty");
        }

        if (!RsaKeyValidator.isValidPublicKey(publicKey)) {
            throw new IllegalArgumentException("Invalid public key format");
        }

        Member member = memberRepository.findByEmail(securityUserDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Member with email " + securityUserDto.getEmail() + " not found"));

        if (rsaPublicKeyRepository.existsByMemberId(member.getMemberId())) {
            throw new IllegalStateException("Member with ID " + member.getMemberId() + " already has a registered public key");
        }

        RsaPublicKeyEntity pubKey = RsaPublicKeyEntity.builder()
                .publicKey(publicKey)
                .member(member)
                .build();

        rsaPublicKeyRepository.save(pubKey);

        log.info("RSA public key saved for member ID: {}", member.getMemberId());
    }

    public String getRsaPublicKeyByMemberId(Long memberId) {
        RsaPublicKeyEntity rsaPublicKeyEntity = rsaPublicKeyRepository.findByMemberId(memberId)
                .orElseThrow(()-> new NotFoundException("PublicKey with " + memberId + " not found"));

        return rsaPublicKeyEntity.getPublicKey();
    }


}
