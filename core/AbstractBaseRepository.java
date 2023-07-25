package com.chello.core.dataservice.api.core;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

/**
 * This abstract class has the common methods for fetching data from DB
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public abstract class AbstractBaseRepository<T extends Serializable>
		implements com.chello.core.dataservice.api.IBaseRepository<T> {

	@Autowired
	protected DynamoDBMapper dataMapper;

	private Class<T> classToSet;

	@SuppressWarnings("unchecked")
	protected AbstractBaseRepository() {
		this.classToSet = ((Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0]);
	}

	/**
	 * Method to save
	 * 
	 * @param Entity
	 */
	public void save(T entity) {
		dataMapper.save(entity);
	}

	/**
	 * Method to find all
	 * 
	 * @return List
	 */
	public List<T> findAll() {
		return dataMapper.scan(classToSet, new DynamoDBScanExpression());
	}

	/**
	 * Method to find by id
	 * 
	 * @param id
	 * @return Entity
	 */
	public T findById(String id) {
		return dataMapper.load(classToSet, id);
	}

	/**
	 * Method to delete entity
	 * 
	 * @param Entity
	 */
	public void delete(T entity) {
		dataMapper.delete(entity);
	}

	/**
	 * Method to find by id
	 * 
	 * @param partitionKey
	 * @param sortKey
	 * @return Entity
	 */
	public T findById(String partitionKey, String sortKey) {
		return dataMapper.load(classToSet, partitionKey, sortKey);
	}

	/**
	 * Method to update
	 * 
	 * @param Entity
	 */
	public void update(T entity) {
		save(entity);
	}
}
