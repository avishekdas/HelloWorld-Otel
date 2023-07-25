package com.chello.core.logging.api;

import org.apache.logging.log4j.LogManager;

/**
 * A factory for creating ChelloLogger objects.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public class ChelloLoggerFactory {

	/**
	 * This is private constructor to protect creation of multiple copies of this
	 * object.
	 */
	private ChelloLoggerFactory() {
	}

	/**
	 * Gets the single instance of ChelloLoggerFactory based on caller class
	 * information.
	 *
	 * @param clazzName the caller class name
	 * @return single instance of ChelloLoggerFactory
	 */
	public static ChelloLogger getInstance(Class<?> clazzName) {

		return ChelloLogger.getInstance(LogManager.getLogger(clazzName));
	}

}
