package com.InfoSec.dynamic_password.domain.encrypt.repository;

import com.InfoSec.dynamic_password.domain.encrypt.entity.AesSymmetricKeyEntity;
import com.InfoSec.dynamic_password.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AesSymmetricKeyRepository extends JpaRepository<AesSymmetricKeyEntity, Long> {

    Optional<AesSymmetricKeyEntity> findByMember(Member member);

}
