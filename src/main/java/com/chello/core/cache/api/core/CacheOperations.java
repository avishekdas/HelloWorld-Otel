package com.chello.core.cache.api.core;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.chello.core.cache.api.ICacheOperations;
import com.chello.core.exception.ChelloException;
import com.chello.core.exception.ChelloTechnicalException;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;

/**
 * IcacheOperation implementation class providing the basic CRUD operations on
 * cache using the ValueOperations and HashOperations
 * 
 * @author Cognizant
 * @version 1.0
 * @since   2022-11-01
 */
@Component
public class CacheOperations<T> implements ICacheOperations<T> {

	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(CacheOperations.class);

	private RedisTemplate<String, T> redisTemplate;
	private ValueOperations<String, T> valueOperations;

	/**
	 * Parameterized Constructor
	 * @param redisTemplate
	 */
	public CacheOperations(RedisTemplate<String, T> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.valueOperations = redisTemplate.opsForValue();
	}

	/**
	 * This method is used to cache the object against a given key with expiry
	 * duration for the respective key Save or update. The difference in save and
	 * update is, if the value is there, it will update, if the value is not there,
	 * it will insert. This is the default behavior of Redis Cache
	 * 
	 * @param key
	 * @param value
	 * @param timeout-milliseconds
	 * @throws ChelloException
	 */
	@Override
	public void save(String key, T value, long... timeout) throws ChelloTechnicalException {
		try {
			if (timeout != null && timeout.length > 0) {
				valueOperations.set(key, value, timeout[0], TimeUnit.MILLISECONDS);
			} else {
				valueOperations.set(key, value);
			}
			chelloLogger.log(ChelloLogLevel.INFO, "CacheKey : " + key + ", Operation : SAVE");
		} catch (Exception e) {
			chelloLogger.log(ChelloLogLevel.ERROR, e.getMessage());
			throw new ChelloTechnicalException("SAVE_FAILED: " + e.getMessage());
		}
	}

	/**
	 * get object by key from cache method returns null if the key is not
	 * present/expired
	 * 
	 * @Param String key
	 * @return Object t
	 */
	@Override
	public T find(String key) throws ChelloTechnicalException {
		T regInfo = null;
		try {
			chelloLogger.log(ChelloLogLevel.INFO, "CacheKey : " + key + ", Operation : FIND");
			regInfo = valueOperations.get(key);

		} catch (Exception e) {
			chelloLogger.log(ChelloLogLevel.ERROR, e.getMessage());
			throw new ChelloTechnicalException("FIND_FAILED: " + e.getMessage());
		}
		return regInfo;
	}

	/**
	 * deletes the cache for given key
	 * 
	 * @Param String key
	 * 
	 */
	@Override
	public void delete(String key) throws ChelloTechnicalException {
		try {
			chelloLogger.log(ChelloLogLevel.INFO, "CacheKey : " + key + ", Operation : DELETE");
			valueOperations.getOperations().delete(key);
		} catch (Exception e) {
			chelloLogger.log(ChelloLogLevel.ERROR, e.getMessage());
			throw new ChelloTechnicalException("DELETE_FAILED: " + e.getMessage());
		}

	}

	/**
	 * This method is used to get the expiry of a given key in redis
	 * 
	 * @param key
	 * @throws ChelloException
	 * @return long
	 */
	@Override
	public Long getExpiry(String key) throws ChelloTechnicalException {
		try {
			return redisTemplate.getExpire(key);
		} catch (Exception e) {
			chelloLogger.log(ChelloLogLevel.ERROR, e.getMessage());
			throw new ChelloTechnicalException("CACHE_EXPIRY_FAILED: " + e.getMessage());
		}
	}
}
