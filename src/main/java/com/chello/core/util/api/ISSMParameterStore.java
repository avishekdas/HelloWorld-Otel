package com.chello.core.util.api;

/**
 * Interface for Parameter Store functionalities
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public interface ISSMParameterStore {

	/**
	 * This Method is used to retrieve parameter from parameter store with respect
	 * to the provided key
	 * 
	 * @param parameterName
	 * @return String
	 */
	public String getParameterByKey(String parameterName);

	/**
	 * This Method is used to retrieve parameters from parameter store with respect
	 * to the provided key
	 * 
	 * @param parameterName
	 * @return Object
	 */
	public Object getParametersMapByGroupKey(String parameterName);

	/**
	 * 
	 * @return
	 */
	public Boolean clearCache();

	/**
	 * 
	 * @param parameterName
	 * @return
	 */
	public Boolean clearCacheForKey(String parameterName);

	/**
	 * 
	 * @param parameterName
	 * @return
	 */
	public Boolean clearCacheForGroupKey(String parameterName);

}
