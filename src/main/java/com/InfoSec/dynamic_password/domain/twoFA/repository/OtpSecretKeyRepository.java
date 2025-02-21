package com.InfoSec.dynamic_password.domain.twoFA.repository;

import com.InfoSec.dynamic_password.domain.twoFA.entity.OtpSecretKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface OtpSecretKeyRepository extends JpaRepository<OtpSecretKey, Integer>{
    @Query("SELECT p FROM OtpSecretKey p WHERE p.member.memberId = :memberId")
    Optional<OtpSecretKey> findByMemberId(@Param("memberId") Long memberId);
}
