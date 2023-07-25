package com.chello.core.exception;

import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;

/**
 * This is the custom exception class for Business exceptions
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public class ChelloBusinessException extends ChelloException {

	private static final long serialVersionUID = 1L;
	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(ChelloBusinessException.class);

	private String trace;

	/**
	 * Constructor with error code and message
	 * 
	 * @param code    the error code
	 * @param message the error message
	 */
	public ChelloBusinessException(Integer code, String message) {
		super(message);
		chelloLogger.log(ChelloLogLevel.ERROR, "ChelloBusinessException: code : " + code + " message: " + message);
		this.message = message;
		this.code = code;
	}

	/**
	 * Constructor with error trace and message
	 * 
	 * @param trace   trace
	 * @param message the error message
	 */
	public ChelloBusinessException(String message, String trace) {
		super("ChelloBusinessException: message: " + message + " : with trace: " + trace);
		chelloLogger.log(ChelloLogLevel.ERROR,
				"ChelloBusinessException: message: " + message + " : with trace: " + trace);
		this.message = message;
		this.trace = trace;
	}

	public String getTrace() {
		return trace;
	}

	/**
	 * Constructor with error message
	 * 
	 * @param message the error message
	 */
	public ChelloBusinessException(final String message) {
		super(message);
		chelloLogger.log(ChelloLogLevel.ERROR, message);
		this.message = message;
	}

	/**
	 * Constructor with error message and throwable exception object
	 * 
	 * @param message the error message
	 * @param error   the wrapped error
	 */
	public ChelloBusinessException(final String message, final Throwable error) {
		super(message, error);
		chelloLogger.log(ChelloLogLevel.ERROR, message, error);
	}

	/**
	 * Constructor with error
	 * 
	 * @param error the wrapped error
	 */
	public ChelloBusinessException(final Throwable error) {
		super(error);
		chelloLogger.log(ChelloLogLevel.ERROR, "Exception:" + error);
	}

}
