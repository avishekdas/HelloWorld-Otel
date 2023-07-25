package com.chello.core.model.logging;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

import javax.annotation.processing.Generated;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This is ChelloLog model class having all fields to be printed in log as a
 * JSON format.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
@Generated("jsonschema2pojo")
@JsonPropertyOrder({ "activity", "userId", "partnerName", "httpMethod", "apiURL", "invocationTimeStamp", "coRelationId",
		"userAgent", "customHeaders", "payload", "status" })
public class ChelloLog implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The activity to determine if it is ELK logging or not */
	private String activity;

	/** The user id. */
	private String userId;

	/** The partner name. */
	private String partnerName;

	/** The api URL. */
	private String apiURL;

	/** The invocation time stamp. */
	private Timestamp invocationTimeStamp;

	/** The co relation id. */
	private String coRelationId;

	/** The user agent. */
	private String userAgent;

	/** The custom headers. */
	private Map<String, String> customHeaders;

	/** The payload. */
	private Payload payload;

	/** The status. */
	private Status status;

	/** The http method. */
	private String httpMethod;

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the partner name.
	 *
	 * @return the partner name
	 */
	public String getPartnerName() {
		return partnerName;
	}

	/**
	 * Sets the partner name.
	 *
	 * @param partnerName the new partner name
	 */
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	/**
	 * Gets the custom headers.
	 *
	 * @return the custom headers
	 */
	public Map<String, String> getCustomHeaders() {
		return customHeaders;
	}

	/**
	 * Sets the custom headers.
	 *
	 * @param customHeaders the custom headers
	 */
	public void setCustomHeaders(Map<String, String> customHeaders) {
		this.customHeaders = customHeaders;
	}

	/**
	 * Gets the api URL.
	 *
	 * @return the api URL
	 */
	public String getApiURL() {
		return apiURL;
	}

	/**
	 * Sets the api URL.
	 *
	 * @param apiURL the new api URL
	 */
	public void setApiURL(String apiURL) {
		this.apiURL = apiURL;
	}

	/**
	 * Gets the invocation time stamp.
	 *
	 * @return the invocation time stamp
	 */
	public Timestamp getInvocationTimeStamp() {
		return invocationTimeStamp;
	}

	/**
	 * Sets the invocation time stamp.
	 *
	 * @param invocationTimeStamp the new invocation time stamp
	 */
	public void setInvocationTimeStamp(Timestamp invocationTimeStamp) {
		this.invocationTimeStamp = invocationTimeStamp;
	}

	/**
	 * Gets the co relation id.
	 *
	 * @return the co relation id
	 */
	public String getCoRelationId() {
		return coRelationId;
	}

	/**
	 * Sets the co relation id.
	 *
	 * @param coRelationId the new co relation id
	 */
	public void setCoRelationId(String coRelationId) {
		this.coRelationId = coRelationId;
	}

	/**
	 * Gets the user agent.
	 *
	 * @return the user agent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * Sets the user agent.
	 *
	 * @param userAgent the new user agent
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * Gets the payload.
	 *
	 * @return the payload
	 */
	public Payload getPayload() {
		return payload;
	}

	/**
	 * Sets the payload.
	 *
	 * @param payload the new payload
	 */
	public void setPayload(Payload payload) {
		this.payload = payload;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Gets the http method.
	 *
	 * @return the http method
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * Sets the http method.
	 *
	 * @param httpMethod the new http method
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * Gets the activity.
	 *
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}

	/**
	 * Sets the activity.
	 *
	 * @param activity the new activity
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}

}
