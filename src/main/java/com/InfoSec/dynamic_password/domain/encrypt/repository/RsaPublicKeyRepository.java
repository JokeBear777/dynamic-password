package com.InfoSec.dynamic_password.domain.encrypt.repository;

import com.InfoSec.dynamic_password.domain.encrypt.entity.RsaPublicKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RsaPublicKeyRepository extends JpaRepository<RsaPublicKeyEntity, Long> {

    @Query("SELECT p from RsaPublicKeyEntity p WHERE p.member.memberId = :memberId")
    Optional<RsaPublicKeyEntity> findByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT COUNT(p) > 0 FROM RsaPublicKeyEntity p WHERE p.member.memberId = :memberId")
    Boolean existsByMemberId(@Param("memberId") Long memberId);

}
