package com.chello.core.model.logging;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This is Status model class having all fields to be printed in log as a JSON
 * format.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
@Generated("jsonschema2pojo")
@JsonPropertyOrder({ "code", "description", "stackTrace" })
public class Status {

	/** The code. */
	private Integer code;

	/** The description. */
	private String description;

	/** The stack trace. */
	private String stackTrace;

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the stack trace.
	 *
	 * @return the stack trace
	 */
	public String getStackTrace() {
		return stackTrace;
	}

	/**
	 * Sets the stack trace.
	 *
	 * @param stackTrace the new stack trace
	 */
	public void setStackTrace(String stackTrace) {
		this.stackTrace = stackTrace;
	}

}
