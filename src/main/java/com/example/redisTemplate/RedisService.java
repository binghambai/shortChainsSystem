package com.example.redisTemplate;

import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService{
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean setString(String key, String value, Long timeout) {
        try {
            String keySer = new String(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key)));
            String valueSer = new String(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(value)));
            redisTemplate.opsForValue().set(keySer, valueSer, timeout, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getString(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public boolean bloomExist(String key) {
        RBloomFilter<String> fitter = RedissonBloom.fitter;
        return fitter.contains(new String(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key))));
    }

    public void addBloom(String key) {
        RBloomFilter<String> fitter = RedissonBloom.fitter;
        fitter.add(new String(Objects.requireNonNull(redisTemplate.getStringSerializer().serialize(key))));
    }
}
