package com.InfoSec.dynamic_password.global.redis.service;

import com.InfoSec.dynamic_password.global.exeption.custom.RedisOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisHashService {

    private final RedisTemplate<String, Object> redisTemplate;

    public <T> void saveHashData(String key, String hashKey, T value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" save hash key: %s, field: %s failed", key, hashKey), e);
        }
    }

    public <T> T getHashData(String key, String hashKey, Class<T> clazz) {
        try {
            Object data = redisTemplate.opsForHash().get(key, hashKey);
            return clazz.cast(data);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" get hash key: %s, field: %s failed", key, hashKey), e);
        }
    }

    public void deleteHashField(String key, String hashKey) {
        try {
            redisTemplate.opsForHash().delete(key, hashKey);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" delete hash field key: %s, field: %s failed", key, hashKey), e);
        }
    }

    public void deleteHash(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" delete hash key: %s failed", key), e);
        }
    }

    public Map<Object, Object> getAllHashFields(String key) {
        try {
            return redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" get all hash fields for key: %s failed", key), e);
        }
    }

    public boolean hashFieldExists(String key, String hashKey) {
        try {
            return redisTemplate.opsForHash().hasKey(key, hashKey);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" hash field exists check key: %s, field: %s failed", key, hashKey), e);
        }
    }

    public long getHashSize(String key) {
        try {
            return redisTemplate.opsForHash().size(key);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" get hash size for key: %s failed", key), e);
        }
    }

    public void setHashKeyExpiration(String key, long timeout, TimeUnit unit) {
        try {
            redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" set expiration for hash key: %s failed", key), e);
        }
    }

    public Long getHashTTL(String key) {
        try {
            return redisTemplate.getExpire(key);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" get ttl for hash key: %s failed", key), e);
        }
    }

    public boolean hashKeyExists(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" hash key exists check key: %s failed", key), e);
        }
    }

    public Set<String> getHashKeysByPattern(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" get hash keys by pattern: %s failed", pattern), e);
        }
    }

    public void deleteHashKeysByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
            throw new RedisOperationException(String.format(" delete hash keys by pattern: %s failed", pattern), e);
        }
    }

    public void flushAll() {
        try {
            redisTemplate.getConnectionFactory().getConnection().flushAll();
        } catch (Exception e) {
            throw new RedisOperationException(" flush failed", e);
        }
    }

    public List<String> scanKeysByPattern(String pattern) {
        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(
                ScanOptions.scanOptions().match(pattern + "*").count(100).build())) {
            List<String> keys = new ArrayList<>();
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next()));
            }
            return keys;
        } catch (Exception e) {
            throw new RedisOperationException(String.format("scan keys by pattern: %s failed", pattern), e);
        }
    }
}

