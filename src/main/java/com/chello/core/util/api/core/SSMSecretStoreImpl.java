package com.chello.core.util.api.core;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.model.AWSSecretsManagerException;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.util.api.ISSMSecretStore;
import com.google.gson.Gson;

/**
 * Implementation class for AWS Secrets Manager Service.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */

@Component
public class SSMSecretStoreImpl implements ISSMSecretStore {

	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(SSMSecretStoreImpl.class);

	@Autowired
	private AWSSecretsManager secretsManagerClient;

	// Local cache map to store secrets key-value
	private static Map<String, String> localCacheSecretsKeyMap = new HashMap<String, String>();

	/**
	 * Checks local cache map before connecting to AWS Secrets Manager to get the
	 * value by secretGroupName and returns the json string containing all the
	 * secret key-values in it.
	 * 
	 * @param secretGroupName Secret Name
	 * @return secretsValue Json string containing all the secret key-values in it
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getSecretsValue(String secretGroupName) {
		Map<String, String> secretsMap = null;
		if (secretGroupName == null || secretGroupName.trim().length() == 0) {
			chelloLogger.log(ChelloLogLevel.ERROR, "INVALID_INPUT");
		} else {
			final String secretGroupNameFinal = secretGroupName.toLowerCase();
			try {
				String secretsValue = localCacheSecretsKeyMap.get(secretGroupNameFinal);
				if (secretsValue != null) {
					chelloLogger.log(ChelloLogLevel.INFO,
							"Returning value from local cache for secret: " + secretGroupName);
				} else {
					chelloLogger.log(ChelloLogLevel.INFO,
							"Returning value from Secrets Manager for secret: " + secretGroupName);
					secretsValue = secretsManagerClient
							.getSecretValue(new GetSecretValueRequest().withSecretId(secretGroupNameFinal))
							.getSecretString();
					localCacheSecretsKeyMap.put(secretGroupNameFinal, secretsValue);
				}
				Gson gson = new Gson();
				secretsMap = gson.fromJson(secretsValue, Map.class);
				if (secretsMap != null) {
					secretsMap.forEach((secretKey, secretValue) -> {
						String cacheKey = secretGroupNameFinal + "_" + secretKey;
						localCacheSecretsKeyMap.put(cacheKey, secretValue);
					});
				}
			} catch (AWSSecretsManagerException e) {
				String errorMessage = String.format("SECRET_NOT_FOUND_EXCEPTION", secretGroupNameFinal,
						e.getMessage(), e.getCause());
				chelloLogger.log(ChelloLogLevel.ERROR, errorMessage);
				// throw new ChelloException(errorMessage, e);
				secretsMap = null;
			} catch (Exception e) {
				String errorMessage = String.format("SECRET_SERVICE_EXCEPTION", secretGroupNameFinal,
						e.getMessage(), e.getCause());
				chelloLogger.log(ChelloLogLevel.ERROR, errorMessage);
				// throw new ChelloException(errorMessage, e);
				secretsMap = null;
			}
		}
		return secretsMap;
	}

	/**
	 * Checks local cache map before connecting to AWS Secrets Manager to get the
	 * value by secretGroupName & secretKey and returns the secret value.
	 * 
	 * @param secretGroupName Secret Name
	 * @param secretKey       Secret Key name inside Secrets
	 * @return secretsValue
	 */
	@Override
	public String getSecretsValue(String secretGroupName, String secretKey) {
		String secretsValue = null;
		if ((secretGroupName == null || secretGroupName.trim().length() == 0)
				|| (secretKey == null || secretKey.trim().length() == 0)) {
			chelloLogger.log(ChelloLogLevel.ERROR, "INVALID_INPUT");
		} else {
			secretGroupName = secretGroupName.toLowerCase();
			secretKey = secretKey.toLowerCase();
			String cacheKey = secretGroupName + "_" + secretKey;
			try {
				secretsValue = localCacheSecretsKeyMap.get(cacheKey);
				if (secretsValue != null) {
					chelloLogger.log(ChelloLogLevel.INFO,
							"Returning value from local cache for secret key: " + secretGroupName + ":" + secretKey);
				} else {
					chelloLogger.log(ChelloLogLevel.INFO, "Returning value from Secrets Manager for secret key: "
							+ secretGroupName + ":" + secretKey);
					String secretsResult = secretsManagerClient
							.getSecretValue(new GetSecretValueRequest().withSecretId(secretGroupName))
							.getSecretString();
					JSONObject jsonObj;
					if (secretsResult != null) {
						jsonObj = new JSONObject(secretsResult);
						secretsValue = (jsonObj.get(secretKey) == null) ? null : (String) jsonObj.get(secretKey);
					}
					if (secretsValue != null)
						localCacheSecretsKeyMap.put(cacheKey, secretsValue);
				}
			} catch (AWSSecretsManagerException e) {
				String errorMessage = String.format("SECRET_NOT_FOUND_EXCEPTION", secretGroupName,
						e.getMessage(), e.getCause());
				chelloLogger.log(ChelloLogLevel.ERROR, errorMessage);
				// throw new ChelloException(errorMessage, e);
				secretsValue = null;
			} catch (JSONException e) {
				String errorMessage = String.format("SECRET_KEY_NOT_FOUND_EXCEPTION", secretGroupName,
						secretKey, e.getMessage(), e.getCause());
				chelloLogger.log(ChelloLogLevel.ERROR, errorMessage);
				// throw new ChelloException(errorMessage, e);
				secretsValue = null;
			} catch (Exception e) {
				String errorMessage = String.format("SECRET_SERVICE_EXCEPTION", secretGroupName,
						e.getMessage(), e.getCause());
				chelloLogger.log(ChelloLogLevel.ERROR, errorMessage);
				// throw new ChelloException(errorMessage, e);
				secretsValue = null;
			}
		}
		return secretsValue;
	}

	/**
	 * Clears the local cache map.
	 * 
	 * @return status Cache cleared successfully
	 */
	@Override
	public Boolean clearCache() {// throws ChelloException{
		localCacheSecretsKeyMap.clear();
		chelloLogger.log(ChelloLogLevel.INFO, "Removed values from local cache for all secrets.");
		return true;
	}

	/**
	 * Clears the local cache map by parameter name.
	 * 
	 * @param parameterName
	 * @return status Cache cleared successfully
	 */
	@Override
	public Boolean clearCacheForKey(String parameterName) {// throws ChelloException{
		localCacheSecretsKeyMap.remove(parameterName);
		chelloLogger.log(ChelloLogLevel.INFO, "Removed value from local cache for secret: " + parameterName);
		return true;
	}

}