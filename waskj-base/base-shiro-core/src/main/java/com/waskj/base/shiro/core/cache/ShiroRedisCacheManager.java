package com.waskj.base.shiro.core.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by poet on 2016/12/19.
 */
public class ShiroRedisCacheManager implements CacheManager {

    private static final Logger logger = LoggerFactory
            .getLogger(ShiroRedisCacheManager.class);


    private Cache cache;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Cache getCache() {
        return cache;
    }

}
