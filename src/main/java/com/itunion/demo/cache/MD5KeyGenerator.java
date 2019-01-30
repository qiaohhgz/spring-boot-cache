package com.itunion.demo.cache;

import com.alibaba.fastjson.JSON;
import com.itunion.demo.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component("md5KeyGenerator")
public class MD5KeyGenerator implements KeyGenerator {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CacheService cacheService;

    @Override
    public Object generate(Object target, Method method, Object... objects) {
        long startTime = System.currentTimeMillis();
        String json = JSON.toJSONString(objects);
        log.info("query json > {}", json);
        String finder = cacheService.getFinger(objects);
        String key = target.getClass().getSimpleName() + ":" + method.getName() + ":" + finder;

        double timeSpan = (System.currentTimeMillis() - startTime);

        log.info("query cache key > {}, use {}ms", key, timeSpan);
        return key;
    }
}
