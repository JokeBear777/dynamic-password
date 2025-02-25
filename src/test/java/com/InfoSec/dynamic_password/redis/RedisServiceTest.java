package com.InfoSec.dynamic_password.redis;

import com.InfoSec.dynamic_password.global.redis.service.RedisTemplateService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RedisServiceTest {

    @Autowired
    private RedisTemplateService redisTemplateService;

    @Test
    public void TestRedisOperation() {
        redisTemplateService.saveData("testKey", "testValue");
        String value = redisTemplateService.getData("testKey", String.class);

        Assertions.assertEquals("testValue", value);
    }

}
