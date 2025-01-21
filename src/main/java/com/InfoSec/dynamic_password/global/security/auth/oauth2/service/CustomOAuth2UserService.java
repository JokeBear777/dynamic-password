package com.InfoSec.dynamic_password.global.security.auth.oauth2.service;

import com.InfoSec.dynamic_password.global.security.auth.oauth2.adapter.OAuth2AttributeAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    //private final MemberService memberService;
    private final Map<String, OAuth2AttributeAdapter> attributeAdapterMap;

    public OAuth2AttributeAdapter getAttributeAdapterMap(String registrationId) {
        OAuth2AttributeAdapter adapter = attributeAdapterMap.get(registrationId);
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter found for registrationId: " + registrationId);
        }
        return attributeAdapterMap.get(registrationId);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID 사용자 이름 속성을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //사용자 속성 가져오기
        Map<String, Object> attributes =
                getAttributeAdapterMap(registrationId)
                        .convertMap(registrationId,userNameAttributeName,oAuth2User.getAttributes());

        // 사용자 권한 설정
        //일단 모든 유저에게 권한 부여, 나중에 수정예정
        // ->하드 코딩 된 부분 수정 예정
        attributes.put("isExist", true); //일단 전부 존재한다고 가정
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));


        // 반환할 OAuth2User 객체 생성
        return new DefaultOAuth2User(authorities, attributes, userNameAttributeName);
    }


}
