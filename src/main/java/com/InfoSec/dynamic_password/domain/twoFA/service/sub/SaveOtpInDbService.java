package com.InfoSec.dynamic_password.domain.twoFA.service.sub;

import com.InfoSec.dynamic_password.domain.member.entity.Member;
import com.InfoSec.dynamic_password.domain.member.repository.MemberRepository;
import com.InfoSec.dynamic_password.domain.notification.service.EmailService;
import com.InfoSec.dynamic_password.domain.twoFA.entity.OtpSecretKey;
import com.InfoSec.dynamic_password.domain.twoFA.repository.OtpSecretKeyRepository;
import com.InfoSec.dynamic_password.domain.twoFA.service.TotpServiceTemplate;
import com.InfoSec.dynamic_password.global.redis.service.RedisTemplateService;
import com.InfoSec.dynamic_password.global.utils.service.AesUtil;
import com.InfoSec.dynamic_password.global.utils.service.TotpUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Primary
@AllArgsConstructor
@Slf4j
public class SaveOtpInDbService extends TotpServiceTemplate {

    private final MemberRepository memberRepository;
    private final OtpSecretKeyRepository otpSecretKeyRepository;
    private final RedisTemplateService redisTemplateService;
    private final EmailService emailService;

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

        String blackListKey = "otp:blackList:" + memberId;
        if (redisTemplateService.keyExists(blackListKey)) {
            throw new AccessDeniedException("Member ID = " + memberId + " cannot perform OTP authentication for 30 minutes.");
        }

        OtpSecretKey otpSecretKey = otpSecretKeyRepository.findByMemberId(memberId)
                .orElseThrow(()-> new NotFoundException("Otp Secret Key with " + memberId + " not found"));
        String encryptedSecretKey = otpSecretKey.getOtpSecretKey();
        String decryptedSecretKey = AesUtil.decrypt(encryptedSecretKey,System.getenv("OTP_AES_KEY"));
        //log.info("[Test] member ID {}'s secretCode = {}",memberId ,decryptedSecretKey);
        String generatedOtpCode = TotpUtil.generateTotp(decryptedSecretKey);


        String redisKey = "otp:failures:" + memberId;
        int failCount = Optional.ofNullable(redisTemplateService.getData(redisKey, Integer.class))
                .orElse(0);
        if (!generatedOtpCode.equals(clientOtpCode)) {
            int newFailCount = failCount + 1;
            //log.info("[TEST] new fail count = {}", newFailCount);
            if(newFailCount >= 5) {
                emailService.sendOtpFailEmail(memberId);
                redisTemplateService.saveDataWithTTL(blackListKey, 1, 1800, TimeUnit.SECONDS);
                throw new RuntimeException("OTP authentication failed over 5 times in 10 minutes." +
                        " Authentication is blocked for 30 minutes.");
            }
            //long remainTime = redisTemplateService.getTTL(redisKey);
            if(newFailCount <= 1) {
                redisTemplateService.saveDataWithTTL(redisKey, newFailCount, 600, TimeUnit.SECONDS);
            }
            if(1 < newFailCount) {
                redisTemplateService.saveData(redisKey, newFailCount);
            }

            throw new RuntimeException("Otp Secret Key is incorrect");
        }

        log.info("Otp Code Verified");
        String otpSuccessKey = "otp:success:" + memberId;
        redisTemplateService.saveDataWithTTL(otpSuccessKey, 1, 1800, TimeUnit.SECONDS);
    }
}
