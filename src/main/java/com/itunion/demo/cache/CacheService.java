package com.itunion.demo.cache;

public interface CacheService {
    String getFinger(Object query);

    String getValue(String key);

    boolean hasKey(String key);

    void setValue(String key, String val);

    void setValue(String key, String val, long seconds);
}
