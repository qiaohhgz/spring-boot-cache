package com.itunion.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class CacheController {

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping
    public Object cacheNames() {
        return cacheManager.getCacheNames();
    }

    @RequestMapping("/{name}")
    public Object cache(@PathVariable("name") String name) {
        return cacheManager.getCache(name);
    }
}
