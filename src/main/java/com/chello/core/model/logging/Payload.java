package com.chello.core.model.logging;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This is Payload model class having all fields of the Payload JSON
 * format.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
@Generated("jsonschema2pojo")
@JsonPropertyOrder({ "type", "data", "pathParam", "queryParam" })
public class Payload {

	/** The type. */
	private String type;

	/** The data. */
	private String data;

	private String pathParam;

	private String queryParam;

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(String data) {
		this.data = data;
	}

	public String getPathParam() {
		return pathParam;
	}

	public void setPathParam(String pathParam) {
		this.pathParam = pathParam;
	}

	public String getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(String queryParam) {
		this.queryParam = queryParam;
	}

}
