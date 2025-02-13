package com.InfoSec.dynamic_password.domain.encrypt.service;

import com.InfoSec.dynamic_password.domain.encrypt.entity.AesSymmetricKeyEntity;
import com.InfoSec.dynamic_password.domain.encrypt.repository.AesSymmetricKeyRepository;
import com.InfoSec.dynamic_password.domain.member.entity.Member;
import com.InfoSec.dynamic_password.domain.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

@Service
@Slf4j
@AllArgsConstructor
public class AesService {

    private final AesSymmetricKeyRepository aesSymmetricKeyRepository;
    private final MemberRepository memberRepository;

    public String generateAes() {

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256);//aes-256

            SecretKey secretKey = keyGenerator.generateKey();
            log.info("Generate AesSymmetricKey");
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());

        } catch (Exception e) {
            throw new RuntimeException("AES key generation fail",e);
        }


    }

    public void save(String encryptedAes, Long memberId) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member with memberId " + memberId + " not found"));

        aesSymmetricKeyRepository.findByMember(member).ifPresent(existingKey -> {
            throw new RuntimeException("AesSymmetricKey already exists for memberId: " + memberId);
        });

        aesSymmetricKeyRepository.save(
                AesSymmetricKeyEntity.builder()
                        .member(member)
                        .symmetricKey(encryptedAes)
                        .build()
        );
    }




}
