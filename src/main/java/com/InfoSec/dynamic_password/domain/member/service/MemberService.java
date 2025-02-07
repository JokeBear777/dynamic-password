package com.InfoSec.dynamic_password.domain.member.service;

import com.InfoSec.dynamic_password.domain.member.entity.Member;
import com.InfoSec.dynamic_password.domain.member.dto.SignUpRequestDto;
import com.InfoSec.dynamic_password.domain.member.repository.MemberRepository;
import com.InfoSec.dynamic_password.domain.member.type.MemberRole;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void signUp(
                       SignUpRequestDto signUpRequestDto
    ) {
        Member member = Member.builder()
                .email(signUpRequestDto.getEmail())
                .name(signUpRequestDto.getName())
                .mobile(signUpRequestDto.getMobile())
                .memberRole(MemberRole.USER)
                .build();

        memberRepository.save(member);
    }

    public void deActiveAccount(SecurityUserDto securityUserDto) {
        Member member = memberRepository.findByEmail(securityUserDto.getEmail())
                .orElseThrow(() -> new NotFoundException("Member not found"));

        // 멤버가 클럽에 가입되어잇는지 확인, 추후 추가한다
        //
        //

        member.setMemberRole(MemberRole.INACTIVE);
    }
}
