package com.waskj.base.core.util.cache;

import com.waskj.base.core.util.SerializeUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class SpringRedisMapCache implements Cache {

	private RedisTemplate<String, Object> redisTemplate;
	private String name;

	private Long expire;

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getExpire() {
		if (expire == null || expire < 1) {
			expire = (long) (24*60*60);
		}
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		// TODO Auto-generated method stub
		return this.redisTemplate;
	}

	@Override
	public ValueWrapper get(Object key) {
		// TODO Auto-generated method stub
		byte[] keyf = redisTemplate.getStringSerializer().serialize(this.name);
		byte[] keyp = ((String) key).getBytes();
		Object object = redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> value = connection.hMGet(keyf, keyp);
				if (value != null && value.size() > 0) {
					byte[] o = value.get(0);
					if (o != null) {
						return SerializeUtil.deserialize(o);
					}
				}
				return null;
			}
		});
		return (object != null ? new SimpleValueWrapper(object) : null);
	}

	@Override
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyp = ((String) key).getBytes();
				Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
				map.put(keyp, SerializeUtil.serialize((Serializable) value));
				connection.hMSet(name.getBytes(), map);
				connection.expire(name.getBytes(), expire);
				return null;
			}
		});
	}

	@Override
	public void evict(Object key) {
		// TODO Auto-generated method stub
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				BoundHashOperations<String, byte[], byte[]> boundHashOperations = redisTemplate.boundHashOps(name);
				byte[] keyp = ((String) key).getBytes();
				return boundHashOperations.delete(keyp);
			}
		});
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.del(name.getBytes());
				return "ok";
			}
		});
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		// TODO Auto-generated method stub
		byte[] keyf = redisTemplate.getStringSerializer().serialize(this.name);
		byte[] keyp = ((String) key).getBytes();
		T object = redisTemplate.execute(new RedisCallback<T>() {
			@SuppressWarnings("unchecked")
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				List<byte[]> value = connection.hMGet(keyf, keyp);
				if (value != null && value.size() > 0) {
					return (T) SerializeUtil.deserialize(value.get(0));
				}
				return null;
			}
		});
		return object;
	}

	@Override
	public <T> T get(Object o, Callable<T> callable) {
		throw new UnsupportedOperationException("暂不支持");
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		// TODO Auto-generated method stub
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] keyp = ((String) key).getBytes();
				Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
				map.put(keyp, SerializeUtil.serialize((Serializable) value));
				connection.hMSet(name.getBytes(), map);
				connection.expire(name.getBytes(), expire);
				return null;
			}
		});
		return new SimpleValueWrapper(value);
	}

}
