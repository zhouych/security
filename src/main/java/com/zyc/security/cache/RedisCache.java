package com.zyc.security.cache;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.zyc.baselibs.SpringContextUtils;
import com.zyc.baselibs.commons.StringUtils;

/**
 * 使用redis来实现mybatis的二级缓存
 * @author zhouyancheng
 *
 */
public class RedisCache implements Cache {

    private static final Logger logger = Logger.getLogger(RedisCache.class);
    
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    private final String id;
    
    @SuppressWarnings("rawtypes")
	private RedisTemplate redisTemplate;

    private static final long EXPIRE_TIME_IN_MINUTES = 300;

    public RedisCache(String id) {
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("The redis cache 'id' is null.");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
    @SuppressWarnings({ "unchecked" })
	public void putObject(Object key, Object value) {
		this.getRedisTemplate().opsForValue().set(key, value, EXPIRE_TIME_IN_MINUTES, TimeUnit.HOURS);
        logger.debug("[RedisCache.putObject(...)] - Put data[key=" + key + "; value=" + value + "] to redis cache.");
    }

    public Object getObject(Object key) {
        Object value = this.getRedisTemplate().opsForValue().get(key);
        logger.debug("[RedisCache.getObject(...)] - Get data[key=" + key + "; value=" + value + "] from redis cache.");
        return value;
    }

	@SuppressWarnings("unchecked")
	public Object removeObject(Object key) {
        this.getRedisTemplate().delete(key);
        logger.debug("[RedisCache.removeObject(...)] - Remove data[key=" + key + "] from redis cache.");
        return null;
    }
  
	public void clear() {
    	((RedisTemplate<?, ?>) this.getRedisTemplate()).execute(new RedisCallback<Object>() {
    		public Object doInRedis(RedisConnection connection) throws DataAccessException {
    			connection.flushDb();
    			return null;
    		}
		});
        logger.debug("[RedisCache.clear(...)] - Clear all data from redis cache.");
    }
    
    public int getSize() {
        return 0;
    }
    
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
    
    @SuppressWarnings("rawtypes")
	private RedisTemplate getRedisTemplate() {
        return null == redisTemplate ?  redisTemplate = (RedisTemplate) SpringContextUtils.getBean("cacheRedisTemplate") : redisTemplate;
    }

}
