package com.chello.insights.constants;

/**
 * The Enum InsightErrorCodes has error codes for insights.
 */
public enum InsightErrorCodes {
	
	SUCCESS(200, "Success"),
	INSIGHT_LAMBDA_NAME_ERROR(10001,"Unable to fetch insight lambda name from parameter store"),
	PERSONETICS_URL_NOT_FOUND(10002,"Personetics url not found"),
	SERVER_ERROR(10003, "Server error occurred"),
	PERSONETICS_URL_PARAM_STORE_NOT_CONFIGURED(10004,"Personetics base URL is not configured in Parameter Store"),
	VALIDATION_FAILED(10005, "Validation failed"),
	PERSONETICS_ENDPOINT_PARAM_STORE_EMPTY(10006,"Personetics end point parameter store key cant be null or empty"),
	USERID_NOT_FOUND(10007, "user id not found in request headers"),
	PERSONETICS_ENDPOINT_PARAM_STORE_NOT_CONFIGURED(10008,"Personetics end point url for endpoint is not configured in Parameter Store"),
	TRACKERNAME_NOT_FOUND(10009, "names not found in querystring"),
	FINXACT_ORGID_NOT_FOUND(10010, "finxactOrgId not found in querystring"),
	EIN_NOT_FOUND(10011, "ein not found in request headers"),
	CORRELATIONID_NOT_FOUND(10012, "Correlation id is not found in request headers"),
	PERSONETICS_INVALID_REQUEST_ERROR(10013, "Invoked withSSL method with invalid data."),
	PERSONETICS_SERVICE_EXECUTION_ERROR(10014, "Exception occurred during get personetices data Service invocation. Error: [%s] due to [%s]."),
	PERSONETICS_EXECUTION_ERROR(10015, "Issue occured while calling external service with error code: [%s] and Message: [%s]."),
	PERSONETICS_NO_DATA_FOUND(10016, "No data found"),
	EXECUTE_FLOW_ERROR(10017,"Error while executing executeFlow API"),	
	INSIGHT_SSLCONTEXT_INSTANTIATION_FAILED(10018, "Not able to instantiate SSLContext."),
	REQUEST_OBJECT_NULL(10019,"Invalid Request object"),
	REQUEST_BODY_NULL(10020,"Invalid Request body"),
	DATE_PARSE_EXCEPTION(10021,"Date Parse Exception");
	
	/**
	 * Instantiates a new insight error codes.
	 *
	 * @param code the code
	 * @param message the message
	 */
	InsightErrorCodes(int code, String message) {
		this.code = code;
		this.message = message;
	}

	/** The code. */
	private int code;
	
	/** The message. */
	private String message;

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
}
