package com.InfoSec.dynamic_password.domain.notification.service;

import com.InfoSec.dynamic_password.domain.notification.dto.AlertDto;
import com.InfoSec.dynamic_password.global.redis.service.RedisHashService;
import com.InfoSec.dynamic_password.global.utils.service.ByteUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class AlertStorageService {
    private final RedisHashService redisHashService;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveOTPAlertToHash(Long memberId, String ip) {
        try {
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
            String key = "user:alerts:" + memberId + ":" + currentTime;
            String hashKey = "otp_alert:" + System.nanoTime(); // UUID 대신 nanoTime() 사용

            AlertDto alertDto = new AlertDto(
                    "OTP_FAIL",
                    Long.parseLong(currentTime),
                    "OTP authentication failed over 5 times in 10 minutes",
                    "false"
                   //, Map.of("ip", ip)
            );

            String json = objectMapper.writeValueAsString(alertDto);
            redisHashService.saveHashData(key, hashKey, json);

            if (redisHashService.hashKeyExists(key)) {
                redisHashService.setHashKeyExpiration(key, 7, TimeUnit.DAYS);
            }

            log.info("[Test] Save Hash Data : Key={}, Value={}", key, json);
        } catch (JsonProcessingException e) {
            log.error("Otp Alert Save to Hash Fail: {}", e.getMessage(), e);
            throw new RuntimeException("Otp Alert Save to Hash Fail", e);
        }
    }

    public List<String> getNotificationKeys(Long memberId) {
        String key = "user:alerts:" + memberId;
        log.info("[Test] Get Notification Keys : Key={}", key);
        return redisHashService.scanKeysByPattern(key);
    }

    public List<AlertDto> getAlerts(List<String> notificationKeys) {
        List<AlertDto> alertDtos = new ArrayList<>();

        if (notificationKeys.isEmpty()) {
            log.info("[Test] notification keys is empty");
            return alertDtos;
        }

        List<Object> rawResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (String key : notificationKeys) {
                connection.hGetAll(key.getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });

        if (rawResults == null || rawResults.isEmpty()) {
            log.error("Redis query returned null or empty result");
            throw new RuntimeException("No Alerts found");
        }

        for (Object result : rawResults) {
            if (result instanceof Map) {
                Map<?, ?> hashFields = (Map<?, ?>) result;
                for (Object value : hashFields.values()) {
                    log.info("value = {}", value.getClass().getSimpleName());
                    if (value instanceof String) {
                        try {
                            AlertDto alertDto = objectMapper.readValue((String)value, AlertDto.class);
                            if(alertDto.getRead().equals("false")) {
                                alertDtos.add(alertDto);
                            }
                        } catch (Exception e) {
                            log.error("Failed to convert Redis hash field to AlertDto: Value={}", value, e);
                        }
                    }
                }
            }
            else {
                log.info("Result is of type {}", result.getClass().getName());
            }
        }

        if (!alertDtos.isEmpty()) {
            markAsRead(notificationKeys, rawResults);
        }
        return alertDtos;
    }

    private void markAsRead(List<String> notificationKeys,List<Object> rawResults) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 0; i < rawResults.size(); i++) {
                String key = notificationKeys.get(i);
                Object result = rawResults.get(i);

                if (result instanceof Map) {
                    Map<?, ?> hashFields = (Map<?, ?>) result;

                    for(Map.Entry<?,?> entry : hashFields.entrySet()) {
                        String field = entry.getKey().toString();
                        String jsonValue = entry.getValue().toString();

                        if (jsonValue.trim().startsWith("{")) {
                            try {
                                AlertDto alertDto = objectMapper.readValue(jsonValue, AlertDto.class);
                                alertDto.setRead("true");
                                String updatedJson = objectMapper.writeValueAsString(alertDto);

                                connection.hSet(
                                        key.getBytes(StandardCharsets.UTF_8),
                                        field.getBytes(StandardCharsets.UTF_8),
                                        updatedJson.getBytes(StandardCharsets.UTF_8)
                                );
                            } catch (Exception e) {
                                log.error("Failed to convert Redis hash field to AlertDto: Value={}", jsonValue, e);
                            }
                        }
                    }

                }
                else {
                    log.warn("Skipping invalid result for key={}, result type={}", key, result.getClass().getName());
                }

            }
            return null;

        });
    }
}
