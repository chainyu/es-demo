package com.example.caffeine.service;

import com.example.caffeine.config.CaffeineCacheConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class CacheServiceImpl implements CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Override
    @Cacheable(cacheManager = CaffeineCacheConfig.CACHE_MANAGER_NAME, value = CaffeineCacheConfig.CACHENAME_DEFAULT, key = "#key", sync = true)
    public Map<String, Object> findData(String key) {
        LOGGER.debug("===> load data key[{}]", key);
        System.out.println("===> "+Thread.currentThread().getId()+"/load data key = [" + key + "]");

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();
        map.put(key, System.currentTimeMillis()%1000000L);

        System.out.println("===> "+Thread.currentThread().getId()+"/load data end key = [" + key + "/ "+map.get(key) +"]");
        return map;
    }
}
