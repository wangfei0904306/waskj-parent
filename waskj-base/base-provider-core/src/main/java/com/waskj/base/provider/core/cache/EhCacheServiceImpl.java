package com.waskj.base.provider.core.cache;

import com.waskj.base.api.core.CacheService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service("ehCacheService")
public class EhCacheServiceImpl<T extends Serializable, K extends Serializable> implements CacheService<T, K> {

	public CacheManager manager;

	@SuppressWarnings("unchecked")
	@Override
	public T getByKey(String cacheName, K key) {
		// TODO Auto-generated method stub
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			Element element = cache.get(key);
			if (element != null) {
				return (T) element.getObjectValue();
			}
		}
		return null;
	}

	@Override
	public T getAndResetExpireByKey(String cacheName, K key,long expire) {
		// TODO implement this
		return null;
	}

	@Override
	public boolean removeByKey(String cacheName, K key) {
		// TODO Auto-generated method stub
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			return cache.remove(key);
		}
		return false;
	}

	@Override
	public boolean putByKey(String cacheName, K key, T value) {
		// TODO Auto-generated method stub
		boolean b = false;
		Cache cache = manager.getCache(cacheName);
		if (cache != null) {
			cache.put(new Element(key, value));
			b = true;
		}
		return b;
	}

	@Override
	public List<T> getAll(String cacheName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeAll(String cacheName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer getSize(String cacheName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<K> getKeys(String cacheName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> getValues(String cacheName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean putExpireByKey(String cacheName, K key, T value, long expire) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setManager(CacheManager manager) {
		this.manager = manager;
	}
}
