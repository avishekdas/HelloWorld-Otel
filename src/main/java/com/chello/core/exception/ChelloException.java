package com.chello.core.exception;

/**
 * This is the custom exception class for Chello
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public class ChelloException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	int code;
	String message;

	/**
	 * Wraps the exception and re-throws
	 * 
	 * @param error the wrapped error
	 */
	public static void wrapAndThrow(final Throwable error) {
		if (error instanceof ChelloException) {
			throw (ChelloException) error;
		} else {
			throw new ChelloException(error);
		}
	}

	/**
	 * Default constructor
	 */
	public ChelloException() {
		super();
	}

	/**
	 * Constructor with error message
	 * 
	 * @param message the error message
	 */
	public ChelloException(final String message) {
		super(message);
	}

	/**
	 * Constructor with error code and message
	 * 
	 * @param code    the error code
	 * @param message the error message
	 */
	public ChelloException(final int code, final String message) {
		super(message);
		this.code = code;
		this.message = message;
	}

	/**
	 * Constructor with error message and throwable exception object
	 * 
	 * @param message the error message
	 * @param error   the wrapped error
	 */
	public ChelloException(final String message, final Throwable error) {
		super(message, error);
	}

	/**
	 * Constructor with error
	 * 
	 * @param error the wrapped error
	 */
	public ChelloException(final Throwable error) {
		super(error);
	}

	/**
	 * Return Code
	 * 
	 * @return int
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Return Message
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

}
