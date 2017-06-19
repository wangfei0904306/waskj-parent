package com.waskj.base.shiro.core.cache;

import com.waskj.base.api.core.CacheService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Created by poet on 2016/12/19.
 */
public class ShiroRedisCache<K extends Serializable,V extends Serializable> implements Cache<K,V> {

    private Logger logger = LoggerFactory.getLogger(ShiroRedisCache.class);

    // cache key prefix
    private String keyPrefix;

    // cache name
    private String cacheName;

    // reset expire time when action get
    private boolean resetExpireWhenGet = true;

    // cache expire time
    private long expireTime = 30 * 60 * 1000;

    // cache service
    private CacheService cacheService;

    //region override methods
    @Override
    public V get(K k) throws CacheException {
        V ret = null;
        if( null == k )
            return ret;
        this.getKey(k);

        try {
            if( this.resetExpireWhenGet )
                ret = (V)cacheService.getAndResetExpireByKey(cacheName,getKey(k), expireTime / 1000);
            else
                ret = (V)cacheService.getByKey(cacheName, getKey(k));

        } catch (Exception e){
            throw new CacheException(e);
        }

        return ret;
    }

    @Override
    public V put(K key, V value) throws CacheException {
        logger.debug("根据key从存储 key [" + getKey(key) + "]");
        try {
            cacheService.putExpireByKey(cacheName, this.getKey(key),value, expireTime);
            return value;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        logger.debug("从redis中删除 key [" + getKey(key) + "]");
        try {
            V previous = this.get(key);
            cacheService.removeByKey(cacheName, this.getKey(key));
            return previous;
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public void clear() throws CacheException {
        logger.debug("从redis中删除所有元素");
        cacheService.removeAll(cacheName);
    }

    @Override
    public int size() {
        try {
            Integer longSize = cacheService.getSize(cacheName);
            return longSize.intValue();
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Set<K> keys() {
        try {
            Set<String> keys = (Set<String>) cacheService.getKeys(cacheName);
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            } else {
                Set<K> newKeys = new HashSet<K>();
                for (String key : keys) {
                    newKeys.add((K) key);
                }
                return newKeys;
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Set<Object> values = (Set<Object>) cacheService.getValues(cacheName);
            if (!CollectionUtils.isEmpty(values)) {
                List<V> result = new ArrayList<V>(values.size());
                for (Object value : values) {
                    result.add((V) value);
                }
                return Collections.unmodifiableList(result);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }
    //endregion

    //region getters and setters
    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isResetExpireWhenGet() {
        return resetExpireWhenGet;
    }

    public void setResetExpireWhenGet(boolean resetExpireWhenGet) {
        this.resetExpireWhenGet = resetExpireWhenGet;
    }

    public CacheService getCacheService() {
        return cacheService;
    }

    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    //endregions

    private String getKey(K key){
        StringBuilder sb = new StringBuilder();
        if(StringUtils.hasText(this.keyPrefix)){
            sb.append(this.keyPrefix);
        }
        sb.append(key);
        return sb.toString();
    }
}
