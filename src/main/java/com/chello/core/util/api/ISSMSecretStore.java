package com.chello.core.util.api;

import java.util.Map;

/**
 * Interface for Secret Manager functionalities
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public interface ISSMSecretStore {

	/**
	 * 
	 * @param secretsName
	 * @return
	 */
	public Map<String, String> getSecretsValue(String secretsName);

	/**
	 * 
	 * @param secretsName
	 * @param keyName
	 * @return
	 */
	public String getSecretsValue(String secretsName, String keyName);

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
}
