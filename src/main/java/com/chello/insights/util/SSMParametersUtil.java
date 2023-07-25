package com.chello.insights.util;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chello.core.util.api.ISSMParameterStore;

/**
 * This class used to load SSM parameter store value.
 */
@Component
public class SSMParametersUtil {

	/** The issm parameter store. */
	@Autowired
	private ISSMParameterStore issmParameterStore;

	/**
	 * This method is used to get parameter using key.
	 *
	 * @param key the key
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getParameterValue(String key) {
		HashMap<String, String> insightsSSMParametrs = (HashMap<String, String>) issmParameterStore
				.getParametersMapByGroupKey("/insights");
		return insightsSSMParametrs.get(key);
	}
	
	/**
	 * Gets the parameter by key.
	 *
	 * @param key the key
	 * @return value
	 */
	public String getParameterByKey(String key) {
		return issmParameterStore.getParameterByKey(key);
	}

}
