package com.waskj.base.api.core;

import java.io.Serializable;
import java.util.List;

public interface CacheService<T extends Serializable, K extends Serializable> {

	T getByKey(String cacheName, K key);

	T getAndResetExpireByKey(String cacheName, K key, long expire);

	boolean removeByKey(String cacheName, K key);

	boolean putByKey(String cacheName, K key, T value);
	
	boolean putExpireByKey(String cacheName, K key, T value, long expire);

	List<T> getAll(String cacheName);

	boolean removeAll(String cacheName);

	Integer getSize(String cacheName);

	List<K> getKeys(String cacheName);

	List<T> getValues(String cacheName);

}
