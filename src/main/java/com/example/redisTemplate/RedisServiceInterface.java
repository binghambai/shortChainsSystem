package com.example.redisTemplate;

public interface RedisServiceInterface {

    void putString(String key, String value);

    String getString(String key);

}
