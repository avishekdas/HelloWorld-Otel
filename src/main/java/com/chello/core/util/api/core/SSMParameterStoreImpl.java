package com.chello.core.util.api.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.amazonaws.services.simplesystemsmanagement.model.GetParametersByPathRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;
import com.amazonaws.services.simplesystemsmanagement.model.ParameterNotFoundException;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.util.api.ISSMParameterStore;

/**
 * Implementation class for AWS SSM Parameter Store Service.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */

@Component
public class SSMParameterStoreImpl implements ISSMParameterStore {

	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(SSMParameterStoreImpl.class);
	public static final Regions REGION = Regions.fromName(System.getenv("AWS_REGION"));
	
	@Autowired
	private AWSSimpleSystemsManagement simpleSystemsManagementClient;

	// SSM Parameter Store default prefix value
	private static final String prefixStart = "/";

	// Local cache map to store parameter key-value
	private static Map<String, String> localCacheStringMap = new HashMap<String, String>();

	// Local cache map to store a group of parameter key-value
	private static Map<String, Map<String, String>> localCacheGroupMap = new HashMap<String, Map<String, String>>();

	/**
	 * Checks local cache map before connecting to AWS Parameter Store to get the
	 * value by parameterName and returns the parameter value.
	 * 
	 * @param parameterName Parameter Name
	 * @return paramValue Parameter value
	 */
	@Override
	public String getParameterByKey(String parameterName) {// throws ChelloException{
		String paramValue = null;
		if (parameterName == null || parameterName.trim().length() == 0) {
			chelloLogger.log(ChelloLogLevel.ERROR, "INVALID_INPUT");
		} else {
			parameterName = parameterName.toLowerCase();
			paramValue = localCacheStringMap.get(parameterName);
			try {
				if (paramValue != null) {
					chelloLogger.log(ChelloLogLevel.INFO,
							"Returning value from local cache for parameter: " + parameterName);
				} else {
					chelloLogger.log(ChelloLogLevel.INFO, "Returning value from ssm for parameter: " + parameterName);
					if (simpleSystemsManagementClient == null) {
						chelloLogger.log(ChelloLogLevel.INFO,
								"simpleSystemsManagementClient is null; hence initializing it.");
						simpleSystemsManagementClient = AWSSimpleSystemsManagementClientBuilder.standard()
								.withRegion(REGION).build();
					}
					GetParameterResult param = simpleSystemsManagementClient
							.getParameter(new GetParameterRequest().withName(parameterName));
					if (param != null && null != param.getParameter()) {
						paramValue = param.getParameter().getValue();
					} else {
						chelloLogger.log(ChelloLogLevel.INFO,
								"Either param or param.getParameter() is null. param: " + param);
					}
					localCacheStringMap.put(parameterName, paramValue);
				}
			} catch (ParameterNotFoundException e) {
				String errorMessage = String.format("PARAMETER_NOT_FOUND_EXCEPTION", parameterName,
						e.getMessage(), e.getCause());
				chelloLogger.log(ChelloLogLevel.ERROR, errorMessage);
				// throw new ChelloException(errorMessage, e);
				paramValue = null;
			} catch (Exception e) {
				String errorMessage = String.format("PARAMETER_SERVICE_EXCEPTION", parameterName,
						e.getMessage(), e.getCause());
				chelloLogger.log(ChelloLogLevel.ERROR, errorMessage);
				// throw new ChelloException(errorMessage, e);
				paramValue = null;
			}
		}
		chelloLogger.log(ChelloLogLevel.INFO,
				"Returning value for parameter: " + parameterName + ", value:" + paramValue);
		return paramValue;
	}

	/**
	 * Checks local cache map before connecting to AWS Parameter Store to get the
	 * value by parameterName and returns the parameter value. This method groups
	 * all the keys prefixed with the parameter name.
	 * 
	 * @param parameterName Parameter Name
	 * @return paramValue Map of Parameter key-values prefixed by parameterName
	 */
	@Override
	public Map<String, String> getParametersMapByGroupKey(String parameterName) {// throws ChelloException{
		Map<String, String> paramValue = null;
		if (parameterName == null || parameterName.trim().length() == 0) {
			chelloLogger.log(ChelloLogLevel.ERROR, "INVALID_INPUT");
		} else {
			parameterName = parameterName.toLowerCase();
			paramValue = localCacheGroupMap.get(parameterName);
			if (paramValue != null) {
				chelloLogger.log(ChelloLogLevel.INFO,
						"Returning value from local cache for group parameter: " + parameterName);
			} else {
				chelloLogger.log(ChelloLogLevel.INFO, "Returning value from ssm for group parameter: " + parameterName);
				if (simpleSystemsManagementClient == null) {
					chelloLogger.log(ChelloLogLevel.INFO,
							"simpleSystemsManagementClient is null; hence initializing it.");
					simpleSystemsManagementClient = AWSSimpleSystemsManagementClientBuilder.standard()
							.withRegion(REGION).build();
				}
				List<Parameter> paramValueList = simpleSystemsManagementClient.getParametersByPath(
						new GetParametersByPathRequest().withPath(prefixStart + parameterName).withRecursive(true))
						.getParameters();
				if (paramValueList.size() > 0) {
					paramValue = buildParametersMapObject(paramValueList);
					localCacheGroupMap.put(parameterName, paramValue);
				} else {
					chelloLogger.log(ChelloLogLevel.INFO, "paramValueList is empty.");
				}
			}
		}
		return paramValue;
	}

	/**
	 * Clears the local cache map.
	 * 
	 * @return status Cache cleared successfully
	 */
	@Override
	public Boolean clearCache() {// throws ChelloException{
		localCacheStringMap.clear();
		localCacheGroupMap.clear();
		chelloLogger.log(ChelloLogLevel.INFO, "Removed values from local cache for all parameters.");
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
		localCacheStringMap.remove(parameterName);
		chelloLogger.log(ChelloLogLevel.INFO, "Removed value from local cache for parameter: " + parameterName);
		return true;
	}

	/**
	 * Clears the local cache map by parameter name for a group of keys.
	 * 
	 * @param parameterName
	 * @return status Cache cleared successfully
	 */
	@Override
	public Boolean clearCacheForGroupKey(String parameterName) {// throws ChelloException{
		localCacheGroupMap.remove(parameterName);
		chelloLogger.log(ChelloLogLevel.INFO, "Removed value from local cache for group parameter: " + parameterName);
		return true;
	}

	/**
	 * Creates the hashmap object containing all the prefix related group
	 * key/values.
	 * 
	 * @param paramValueList List of Parameter Object
	 * @return paramsMap Map containing parameter only key-values
	 */
	private Map<String, String> buildParametersMapObject(List<Parameter> paramValueList) {
		Map<String, String> paramsMap = new HashMap<String, String>();
		paramValueList.forEach(parameter -> {
			String keyName = parameter.getName();
			keyName = keyName.replaceFirst(prefixStart, "");
			keyName = keyName.replaceAll(prefixStart, "_");
			paramsMap.put(keyName, parameter.getValue());
		});
		return paramsMap;
	}
}
