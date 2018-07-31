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
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager manager = new SimpleCacheManager();

        List<Cache> caches = new ArrayList<>();
        caches.add(new ConcurrentMapCache("help_category"));
        caches.add(new ConcurrentMapCache("help_topic"));

        caches.add(new CaffeineCache("HelpTopicController", Caffeine.newBuilder()
                .expireAfterWrite(3, TimeUnit.SECONDS)
                .build()));

        manager.setCaches(caches);
        return manager;
    }
}
