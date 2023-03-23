package com.example.redisTemplate;

public interface RedisServiceInterface {

    void putString(String key, String value, long timeout);

    String getString(String key);

}
