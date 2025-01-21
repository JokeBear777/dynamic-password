package com.InfoSec.dynamic_password.domain.member.service;

import com.InfoSec.dynamic_password.domain.member.Entity.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {


    public Optional<Member> findByEmail(String email) {
        return Optional.empty();
    }
}
