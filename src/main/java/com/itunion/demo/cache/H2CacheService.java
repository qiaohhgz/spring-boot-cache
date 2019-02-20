package com.itunion.demo.cache;

import com.alibaba.fastjson.JSON;
import com.itunion.demo.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component("h2CacheService")
public class H2CacheService implements CacheService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate h2JdbcTemplate;

    @Override
    public String getFinger(Object query) {
        String json = JSON.toJSONString(query);
        return MD5Util.MD5(json);
    }

    @PostConstruct
    public void autoDeleteExpireData() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        // 查询缓存数量
                        Map<String, Object> map = h2JdbcTemplate.queryForMap("select count(*) as total from map");
                        long total = (long) map.get("total");

                        // 清除过期的缓存数据
                        long date = System.currentTimeMillis() / 1000;
                        int rows = h2JdbcTemplate.update("delete from map where expire <= ?", date);
                        logger.info("total caches {} , delete expire date rows {}", total, rows);

                        // 等待一会
                        Thread.sleep(10 * 1000);
                    } catch (Exception e) {
                        logger.warn("auto delete expire data thread error " + e.getMessage());
                    }
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public String getValue(String key) {
        String sql = "select value, expire from map where key = ? limit 1";
        List<Map<String, Object>> list = h2JdbcTemplate.queryForList(sql, key);
        if (list == null || list.isEmpty()) {
            return null;
        }
        Map<String, Object> data = list.get(0);
        Long expire = (Long) data.get("expire");
        long date = System.currentTimeMillis() / 1000;
        if (expire != null && expire <= date) {
            h2JdbcTemplate.update("delete from map where key = ?", key);
            return null;
        }
        return (String) data.get("value");
    }

    @Override
    public boolean hasKey(String key) {
        String sql = "select value, expire from map where key = ? limit 1";
        List<Map<String, Object>> list = h2JdbcTemplate.queryForList(sql, key);
        if (list == null || list.isEmpty()) {
            return false;
        }
        Map<String, Object> data = list.get(0);
        Long expire = (Long) data.get("expire");
        long date = System.currentTimeMillis() / 1000;
        if (expire != null && expire <= date) {
            logger.info("key {} 已过期", key);
            h2JdbcTemplate.update("delete from map where key = ?", key);
            return false;
        }
        return true;
    }

    @Override
    public void setValue(String key, String val) {
        if (hasKey(key)) {
            String sql = "update map set value = ? where key = ?";
            h2JdbcTemplate.update(sql, val, key);
        } else {
            String sql = "insert into map (key, value) values(?,?)";
            h2JdbcTemplate.update(sql, key, val);
        }
    }

    @Override
    public void setValue(String key, String val, long seconds) {
        long expire = System.currentTimeMillis() / 1000 + seconds;
        if (hasKey(key)) {
            String sql = "update map set value = ?, expire = ? where key = ?";
            h2JdbcTemplate.update(sql, val, expire, key);
        } else {
            String sql = "insert into map values(?,?,?)";
            h2JdbcTemplate.update(sql, key, val, expire);
        }
    }
}
