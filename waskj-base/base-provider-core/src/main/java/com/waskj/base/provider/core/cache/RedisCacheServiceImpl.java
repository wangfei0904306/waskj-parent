package com.waskj.base.provider.core.cache;

import com.waskj.base.api.core.CacheService;
import com.waskj.base.core.util.SerializeUtil;
import com.waskj.base.core.util.spring.SpringContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service("redisCacheService")
public class RedisCacheServiceImpl implements CacheService {

	@SuppressWarnings("unchecked")
	public RedisTemplate<String, Serializable> getRedisTemplate(String cacheName) {
		return (RedisTemplate<String, Serializable>) SpringContextHolder.getApplicationContext().getBean(cacheName);
	}

	@Override
	public Serializable getByKey(String cacheName, Serializable key) {
		// TODO Auto-generated method stub
		Serializable o = getRedisTemplate(cacheName).execute(new RedisCallback<Serializable>() {
			@Override
			public Serializable doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] k = key.toString().getBytes();
				byte[] value = connection.get(k);
				if (value != null) {
					return SerializeUtil.deserialize(value);
				}
				return null;
			}
		});
		return o;
	}

	@Override
	public Serializable getAndResetExpireByKey(String cacheName, Serializable key,long expire) {
		Serializable result = this.getByKey(cacheName,key);
		getRedisTemplate(cacheName).execute(new RedisCallback<Serializable>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] k = key.toString().getBytes();
				return connection.expire(k,expire);
			}
		});
		return result;
	}

	@Override
	public boolean removeByKey(String cacheName, Serializable key) {
		// TODO Auto-generated method stub
		boolean b = getRedisTemplate(cacheName).execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] k = key.toString().getBytes();
				long num = connection.del(k);
				if (num > 0) {
					return true;
				}
				return false;
			}
		});
		return b;
	}

	@Override
	public boolean putByKey(String cacheName, final Serializable key, Serializable value) {
		// TODO Auto-generated method stub
		boolean b = getRedisTemplate(cacheName).execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] k = key.toString().getBytes();
				byte[] v = SerializeUtil.serialize(value);
				connection.set(k, v);
				return true;
			}
		});
		return b;
	}

	@Override
	public List getAll(String cacheName) {
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
		Set<String> keys = getRedisTemplate(cacheName).keys("*");
		return keys.size();
	}

	@Override
	public List getKeys(String cacheName) {
		// TODO Auto-generated method stub
		List list = new ArrayList();
		Set<String> keys = getRedisTemplate(cacheName).keys("*");
		list.addAll(keys);
		return list;
	}

	@Override
	public List getValues(String cacheName) {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public boolean putExpireByKey(String cacheName, Serializable key, Serializable value, long expire) {
		// TODO Auto-generated method stub
		boolean b = getRedisTemplate(cacheName).execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] k = key.toString().getBytes();
				byte[] v = SerializeUtil.serialize(value);
				connection.pSetEx(k, expire, v);
				return true;
			}
		});
		return b;
	}
}
