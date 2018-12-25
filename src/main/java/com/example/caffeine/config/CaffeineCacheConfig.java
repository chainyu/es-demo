package com.example.caffeine.config;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.sun.corba.se.impl.orbutil.graph.Graph;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author qianyu.zhang
 * @date 2018/7/27 17:18
 * @since JDK 1.8
 **/
@Configuration
@EnableCaching
public class CaffeineCacheConfig {

    public static final String CACHE_MANAGER_NAME = "caffeineCacheManager";


    public static final String CACHENAME_DEFAULT = "default";

    /** 缓存数量(条数).*/
    public static final int DEFAULT_MAXSIZE = 500;
    /** 缓存时间(秒).*/
    public static final int DEFAULT_TTL = 5;

    /**定义cache名称、过期时间（秒）、最大size
     * 每个cache缺省5秒超时、最多缓存500条数据，需要修改可以在构造方法的参数中指定。
     */
    public enum Caches{

        defaultContent(CACHENAME_DEFAULT);

        Caches() {
        }

        Caches(String cacheName) {
            this.name = cacheName;
        }

        private Caches(String name, int maxSize, int ttl) {
        	this.name = name;
			this.maxSize = maxSize;
			this.ttl = ttl;
		}



        /**
         * 最大数量
         */
        private int maxSize = DEFAULT_MAXSIZE;

        /**
         * 过期时间（秒）
         */
        private int ttl = DEFAULT_TTL;

        /**
         * 缓存名称
         */
        private String name = CACHENAME_DEFAULT;

        public int getMaxSize() {
            return maxSize;
        }
        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }
        public int getTtl() {
            return ttl;
        }
        public void setTtl(int ttl) {
            this.ttl = ttl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    /**
     * 创建基于caffeine的Cache Manager
     * 用@Primary 告诉spring 在犹豫的时候优先选择哪一个具体的实现
     * @return
     */
    @Bean(name = CACHE_MANAGER_NAME)
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();

        //把各个cache注册到cacheManager中，CaffeineCache实现了org.springframework.cache.Cache接口
        ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
        for(Caches c : Caches.values()){
            //caches.add(new CaffeineCache(c.getName(), Caffeine.newBuilder().recordStats().expireAfterWrite(c.getTtl(), TimeUnit.SECONDS).maximumSize(c.getMaxSize()).build()));
            caches.add(new CaffeineCache(c.getName(), Caffeine.newBuilder().recordStats().refreshAfterWrite(c.getTtl(), TimeUnit.SECONDS).maximumSize(c.getMaxSize()).removalListener((k, graph, cause) ->
                    System.out.printf("执行移除监听器- Key %s was removed (%s)%n", k, cause)).build(cacheLoader())));

        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    /**
     *  构造refreshAfterWrite类型的cache必须申明这个bean
     * @return
     */
    @Bean
    public CacheLoader<Object, Object> cacheLoader() {

        CacheLoader<Object, Object> cacheLoader = new CacheLoader<Object, Object>() {
            @Nullable
            @Override
            public Object load(@Nonnull Object o) throws Exception {
                System.out.println("===> "+Thread.currentThread().getId()+"/caffeine load = [" + o + "]");
                return null;
            }
        };
        return cacheLoader;
    }

    public static String generateKey(Object object){
        return DigestUtils.md5Hex(object != null ? JSON.toJSONString(object) : null);
    }

}
