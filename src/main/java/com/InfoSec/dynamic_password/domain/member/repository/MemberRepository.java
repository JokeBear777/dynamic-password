package com.InfoSec.dynamic_password.domain.member.repository;

import com.InfoSec.dynamic_password.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String Email);
}
