package com.InfoSec.dynamic_password.global.redis.service;

import com.InfoSec.dynamic_password.global.exeption.custom.RedisOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTemplateService{

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> void saveData(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" save key: %s failed", key), e);
        }
    }

    public <T> void saveDataWithTTL(String key, T value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" save with TTL key: %s failed", key), e);
        }
    }

    public <T> T getData(String key, Class<T> clazz) {
        try {
            Object data = redisTemplate.opsForValue().get(key);
            return clazz.cast(data);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" get key: %s failed", key), e);
        }
    }

    public void deleteData(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" delete key: %s failed", key), e);
        }
    }

    public Long getTTL(String key) {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" get ttl key: %s failed", key), e);
        }
    }

    public void setKeyExpiration(String key, long timeout, TimeUnit unit) {
        try {
            redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" set key expiration: %s failed", key), e);
        }
    }

    public boolean keyExists(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" key exists: %s failed", key), e);
        }
    }

    public Set<String> getKeysByPattern(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" key exists by pattern : %s failed", pattern), e);
        }
    }

    public void deleteKeysByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" key delete by pattern : %s failed", pattern), e);
        }
    }

    public void flushAll() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
        } catch (Exception e) {
            throw new RedisOperationException(" flush failed", e);
        }
    }
}
