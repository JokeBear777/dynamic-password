package com.InfoSec.dynamic_password.domain.twoFA.service.sub;

import com.InfoSec.dynamic_password.domain.member.entity.Member;
import com.InfoSec.dynamic_password.domain.member.repository.MemberRepository;
import com.InfoSec.dynamic_password.domain.twoFA.entity.OtpSecretKey;
import com.InfoSec.dynamic_password.domain.twoFA.repository.OtpSecretKeyRepository;
import com.InfoSec.dynamic_password.domain.twoFA.service.TotpServiceTemplate;
import com.InfoSec.dynamic_password.global.utils.service.AesUtil;
import com.InfoSec.dynamic_password.global.utils.service.TotpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Service
@Primary
@AllArgsConstructor
@Slf4j
public class SaveOtpInDbService extends TotpServiceTemplate {

    private final MemberRepository memberRepository;
    private final OtpSecretKeyRepository otpSecretKeyRepository;

    @Override
    public void saveOtpSecretKey(Long memberId, String plainOtpSecretKey) {
        //log.info("[Test] generate OtpSecretKey = {}", plainOtpSecretKey);
        String encryptedOtpSecretKey =AesUtil.encrypt(plainOtpSecretKey,System.getenv("OTP_AES_KEY"));
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(()-> new NotFoundException("Cannot find member with id: " + memberId));

        OtpSecretKey otpSecretKey = OtpSecretKey.builder()
                .otpSecretKey(encryptedOtpSecretKey)
                .member(member)
                .build();

        otpSecretKeyRepository.save(otpSecretKey);
    }

    @Override
    public void verifyOtpCode(Long memberId, String clientOtpCode) {
        if (memberRepository.findByMemberId(memberId).isEmpty()) {
            throw new NotFoundException("Cannot find member with id: " + memberId);
        }

        OtpSecretKey otpSecretKey = otpSecretKeyRepository.findByMemberId(memberId)
                .orElseThrow(()-> new NotFoundException("Otp Secret Key with " + memberId + " not found"));
        String encryptedSecretKey = otpSecretKey.getOtpSecretKey();
        String decryptedSecretKey = AesUtil.decrypt(encryptedSecretKey,System.getenv("OTP_AES_KEY"));
        //log.info("[Test] member ID {}'s secretCode = {}",memberId ,decryptedSecretKey);
        String generatedOtpCode = TotpUtil.generateTotp(decryptedSecretKey);
        
        if (!generatedOtpCode.equals(clientOtpCode)) {
            //레디스에 인증 실패 회수 추가 -> 만약 일정횟수 이상 실패시 일정 시간 블랙리스트 등록
            throw new RuntimeException("Otp Secret Key is incorrect");
        }

        log.info("Otp Code Verified");
        //레디스에 otp 인증 추가
    }
}
