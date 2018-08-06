package com.itunion.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableCaching
@Configuration
public class CacheConfig {
    // 项目的缓存管理类
    @Bean
    public CacheManager cacheManager() {

        SimpleCacheManager manager = new SimpleCacheManager();

        List<Cache> caches = new ArrayList<>();
        // 使用 Map 存储数据
        caches.add(new ConcurrentMapCache("help_category"));
        caches.add(new ConcurrentMapCache("help_topic"));

        // 使用 Caffeine 存储，Caffeine 在 spring boot 2.0 之后取代了优秀的 guava cache
        // expireAfterWrite 3 秒内数据没有更新就删除缓存数据， 可以理解为是数据会在缓存中存在 3 秒钟
        caches.add(new CaffeineCache("HelpTopicController", Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build()));
        caches.add(new CaffeineCache("HelpCategoryController", Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build()));

        manager.setCaches(caches);
        return manager;
    }
}
