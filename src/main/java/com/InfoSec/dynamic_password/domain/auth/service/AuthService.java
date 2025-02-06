package com.InfoSec.dynamic_password.domain.auth.service;

import com.InfoSec.dynamic_password.domain.auth.dto.JwtRequestDto;
import com.InfoSec.dynamic_password.domain.member.entity.Member;
import com.InfoSec.dynamic_password.domain.member.service.MemberService;
import com.InfoSec.dynamic_password.domain.member.type.MemberRole;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.GeneratedToken;
import com.InfoSec.dynamic_password.global.security.auth.jwt.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    public GeneratedToken generateJwt(JwtRequestDto jwtRequestDto) {

        String email = jwtRequestDto.getEmail();

        Optional<Member> findMember = memberService.findByEmail(email);

        if (findMember.isPresent()) {
            GeneratedToken token = jwtUtil.generateToken(email, findMember.get().getMemberRole().toString());
            log.info("Generated token: {}", token);
            return token;
        }

        GeneratedToken token = jwtUtil.generateToken(email, MemberRole.GUEST.toString());
        log.info("Generated guest token: {}", token);
        return token;
    };




}
