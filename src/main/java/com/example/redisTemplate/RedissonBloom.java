package com.example.redisTemplate;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedissonBloom {

    @Autowired
    private RedissonClient redissonClient;

    public static RBloomFilter<String> fitter;

    private static final long expectedInsertions = 500000000L;

    private static final double falseProbability = 0.03;

    @PostConstruct
    private void init() {
        RBloomFilter<String> tmpFitter = redissonClient.getBloomFilter("sample");
        tmpFitter.tryInit(expectedInsertions, falseProbability);
        fitter = tmpFitter;
    }
}
