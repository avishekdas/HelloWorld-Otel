package com.chello.core.dataservice.api;

import java.io.Serializable;
import java.util.List;

import com.chello.core.exception.ChelloException;

/**
 * This is the base interface for all DynamoDB operations
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 *
 * @param <T>
 */
public interface IBaseRepository<T extends Serializable> {

	/**
	 * This method is used to store an entity into dynamoDB
	 * 
	 * @param entity
	 * @throws ChelloException
	 */
	public void save(T entity) throws ChelloException;

	/**
	 * This method is used to find all objects from DynamoDB
	 * 
	 * @return List
	 * @throws ChelloException
	 */
	public List<T> findAll() throws ChelloException; // you might want a generic Collection if u prefer

	/**
	 * This method is used to find the object from DynamoDB using partitionKey
	 * 
	 * @param partitionKey
	 * @return Object
	 * @throws ChelloException
	 */
	public T findById(String partitionKey) throws ChelloException;

	/**
	 * This method is used to find the object from DynamoDB using partitionKey and
	 * sortKey
	 * 
	 * @param partitionKey
	 * @param sortKey
	 * @return Object
	 * @throws ChelloException
	 */
	public T findById(String partitionKey, String sortKey) throws ChelloException;

	/**
	 * This method is used to update the object
	 * 
	 * @param entity
	 * @throws ChelloException
	 */
	public void update(T entity) throws ChelloException;

	/**
	 * This method is used to delete the respective object from DynamoDB
	 * 
	 * @param entity
	 * @throws ChelloException
	 */
	public void delete(T entity) throws ChelloException;
}
