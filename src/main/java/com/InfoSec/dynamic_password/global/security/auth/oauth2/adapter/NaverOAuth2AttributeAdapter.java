package com.InfoSec.dynamic_password.global.security.auth.oauth2.adapter;

import com.InfoSec.dynamic_password.global.annotation.OAuth2Provider;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@OAuth2Provider("naver")
public class NaverOAuth2AttributeAdapter implements OAuth2AttributeAdapter {
    private Map<String, Object> attributes; // 사용자 속성 정보를 담는 Map
    private String attributeKey; // 사용자 속성의 키 값
    private String email; // 이메일 정보
    private String name; // 이름 정보
    private String provider; // 제공자 정보
    private String mobile; //폰 번호 정보

    @Override
    public Map<String, Object> convertMap(
            String provider,
            String attributeKey,
            Map<String, Object> attributes){

        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        this.email = (String) response.get("email");
        this.attributes = response;
        this.provider = provider;
        this.attributeKey = attributeKey;
        this.name = (String) response.get("name");
        this.mobile = (String) response.get("mobile");

        Map<String,Object> convertAttributes = new HashMap<>();
        convertAttributes.put("id", attributeKey);
        convertAttributes.put("key", attributeKey);
        convertAttributes.put("provider", provider);
        convertAttributes.put("name", name);
        convertAttributes.put("mobile", mobile);

        return convertAttributes;
    }
}
