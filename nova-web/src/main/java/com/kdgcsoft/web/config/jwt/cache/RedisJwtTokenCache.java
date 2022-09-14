package com.kdgcsoft.web.config.jwt.cache;

import com.kdgcsoft.web.common.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * @author fyin
 * @date 2022年09月09日 9:22
 */
public class RedisJwtTokenCache implements JwtTokenCache {
    @Autowired
    public RedisTemplate redisTemplate;


    @Override
    public LoginUser get(String key) {
        ValueOperations<String, LoginUser> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void put(String key, LoginUser loginUser, long timeout) {
        redisTemplate.opsForValue().set(key, loginUser, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void refresh(String key, long timeout) {
        ValueOperations<String, LoginUser> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.getOperations().hasKey(key)) {
            LoginUser loginUser = valueOperations.get(key);
            redisTemplate.delete(key);
            redisTemplate.opsForValue().set(key, loginUser, timeout, TimeUnit.MILLISECONDS);
        }
    }
}
