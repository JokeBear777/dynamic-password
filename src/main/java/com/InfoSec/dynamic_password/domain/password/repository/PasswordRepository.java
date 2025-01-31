package com.InfoSec.dynamic_password.domain.password.repository;

import com.InfoSec.dynamic_password.domain.member.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

//엔티티 만들면 수정필요
public interface PasswordRepository extends JpaRepository<Member, Long> {
}
