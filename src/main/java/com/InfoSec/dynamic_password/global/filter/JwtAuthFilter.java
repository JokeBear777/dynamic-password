package com.InfoSec.dynamic_password.global.filter;

import com.InfoSec.dynamic_password.domain.member.entity.Member;
import com.InfoSec.dynamic_password.domain.member.service.MemberService;
import com.InfoSec.dynamic_password.global.security.auth.jwt.dto.SecurityUserDto;
import com.InfoSec.dynamic_password.global.security.auth.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("doFilterInternal start");

        String path = request.getServletPath();
        log.info("Request path: {}", path);
        // 모두 허용 URL 처리
        if (path.equals("/") || path.startsWith("/login") ||  path.startsWith("/error")
                || path.startsWith("/oauth2-login") || path.startsWith("/sign-up")) {
            filterChain.doFilter(request, response);
            log.info("permitAll");
            return;
        }

        String accessToken = request.getHeader("Authorization");

        if (!StringUtils.hasText(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7).trim();
        }

        if (!jwtUtil.verifyToken(accessToken)) {
            throw new JwtException("Access token is expired");
        }

        if (jwtUtil.verifyToken(accessToken)) {
            String email = jwtUtil.getUid(accessToken);
            Member findMember =  memberService.findByEmail(email)
                    .orElseThrow(IllegalStateException::new);
            SecurityUserDto userDto = SecurityUserDto.builder()
                    .userId(findMember.getMemberId())
                    .email(findMember.getEmail())
                    .mobile(findMember.getMobile())
                    .role(findMember.getMemberRole())
                    .build();

            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("verification is success");
        }

        filterChain.doFilter(request, response);
    }


    public Authentication getAuthentication(SecurityUserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                List.of(new SimpleGrantedAuthority(member.getRole().toString())));
    }
}
