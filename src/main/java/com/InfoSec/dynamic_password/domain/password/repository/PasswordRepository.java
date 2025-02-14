package com.InfoSec.dynamic_password.domain.password.repository;

import com.InfoSec.dynamic_password.domain.member.entity.Member;
import com.InfoSec.dynamic_password.domain.password.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    @Query("SELECT p FROM Password p WHERE p.member.memberId = :memberId")
    List<Password> findByMemberId(@Param("memberId") Long memberId);
}
