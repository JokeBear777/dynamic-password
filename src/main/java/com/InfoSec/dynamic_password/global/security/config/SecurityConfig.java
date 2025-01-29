package com.InfoSec.dynamic_password.global.security.config;

import com.InfoSec.dynamic_password.global.filter.JwtAuthFilter;
import com.InfoSec.dynamic_password.global.filter.JwtExceptionFilter;
import com.InfoSec.dynamic_password.global.security.auth.oauth2.handler.OAuth2LoginFailureHandler;
import com.InfoSec.dynamic_password.global.security.auth.oauth2.handler.OAuth2LoginSuccessHandler;
import com.InfoSec.dynamic_password.global.security.auth.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private final CustomOAuth2UserService customOauth2UserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtExceptionFilter jwtExceptionFilter;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/favicon.ico",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()            // 정적 리소스 & Swagger UI
                        .requestMatchers("/oauth2/**", "/auth/**").permitAll()   // OAuth2 인증 엔드포인트
                        .requestMatchers("/", "/login/**","sign-up").permitAll()
                        //.requestMatchers("/sign-up/**").permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2Login(outh -> outh
                        //.loginPage("/oauth2-login")
                        .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패시 처리할 핸들러를 지정해준다.
                        .successHandler(oAuth2LoginSuccessHandler) // OAuth2 로그인 성공시 처리할 핸들러를 지정해준다.
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOauth2UserService) // 사용자 정보 서비스 설정
                        )
                );

        return http
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
                .build();
    }

}
