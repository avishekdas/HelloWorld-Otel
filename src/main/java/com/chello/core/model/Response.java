package com.chello.core.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Business Object for custom Chello Response
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public class Response<T> implements Serializable {

	private int httpStatusCode;
	private int responseCode;
	@SuppressWarnings("unchecked")
	private T responseBody=(T) "{}";
	private String codeDescription;
	private Map<String, String> headers = new HashMap<String, String>();

	public Response() {
	}

	public Response(int responseCode, T responseBody, Map<String, String> headers) {
		this.responseCode = responseCode;
		this.responseBody = responseBody;
		if(null == headers || headers.isEmpty())
			return;
		this.headers.putAll(headers);
	}

	public int getResponseCode() {
		return responseCode;
	}	

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public T getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(T responseBody) {
		this.responseBody = responseBody;
	}

	public String getCodeDescription() {
		return codeDescription;
	}

	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		if(null == headers || headers.isEmpty())
			return;
		this.headers.putAll(headers);
	}		


	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

}
