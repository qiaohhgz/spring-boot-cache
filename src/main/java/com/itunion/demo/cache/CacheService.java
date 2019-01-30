package com.itunion.demo.cache;

import com.alibaba.fastjson.JSON;
import com.itunion.demo.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String getFinger(Object query) {
        String json = JSON.toJSONString(query);
        return MD5Util.MD5(json);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setValue(String key, String val) {
        redisTemplate.opsForValue().set(key, val);
    }

    public void setValue(String key, String val, long seconds) {
        redisTemplate.opsForValue().set(key, val, seconds, TimeUnit.SECONDS);
    }
}
