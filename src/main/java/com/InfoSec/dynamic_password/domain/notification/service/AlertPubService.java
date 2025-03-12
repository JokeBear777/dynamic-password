package com.InfoSec.dynamic_password.domain.notification.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AlertPubService {

    //fcm관련 메서드만 넣자

    private final RedisTemplate<String, String> redisTemplate;




}
