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

    private final RedisTemplate<String, String> redisTemplate;

    public void saveData(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        }
        catch (DataAccessException e) {
            throw new RedisOperationException("Redis save failed",e);
        }
    }

    public void saveDataWithTTL(String key, String value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        }
        catch (DataAccessException e) {
            throw new RedisOperationException("Redis save failed",e);
        }
    }

    public String getData(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        }
        catch (Exception e) {
            throw new RedisOperationException("Redis get failed",e);
        }
    }

    public void deleteData(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RedisOperationException("Redis delete failed", e);
        }
    }

    public Long getTTL(String key) {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            throw new RedisOperationException("Redis get TTL failed", e);
        }
    }

    public void setKeyExpiration(String key, long timeout, TimeUnit unit) {
        try {
            redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            throw new RedisOperationException("Redis set TTL failed", e);
        }
    }

    public boolean keyExists(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            throw new RedisOperationException("Redis key existence check failed", e);
        }
    }

    public Set<String> getKeysByPattern(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            throw new RedisOperationException("Redis key search failed", e);
        }
    }

    public void deleteKeysByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            throw new RedisOperationException("Redis key deletion by pattern failed", e);
        }
    }

    public void flushAll() {
        try {
            Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushAll();
        }
        catch (Exception e) {
            throw new RedisOperationException("Redis flush failed",e);
        }
    }
}
