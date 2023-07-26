package com.chello.core.cache.api;

import com.chello.core.exception.ChelloTechnicalException;

/**
 * End point for the cache operations
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public interface ICacheOperations<T> {

	public void save(String key, T value, long... timeout) throws ChelloTechnicalException;

	public T find(String id) throws ChelloTechnicalException;

	public void delete(String key) throws ChelloTechnicalException;

	public Long getExpiry(String key) throws ChelloTechnicalException;
}
