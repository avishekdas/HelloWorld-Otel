package com.chello.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.http.HttpHeaders;

import com.appdynamics.serverless.tracers.aws.api.Transaction;

/**
 * Business Object for custom Chello Request
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public class Request<T> {

	private String endPoint;
	private String httpMethod;
	private HttpHeaders headers;
	private T body;
	private String userId;
	private String corelationId;
	private String ein;
	private String[] pathParams;
	private Map<String, String> queryParams = new HashedMap<>();
	private CallerContext context;
	private String email;
	private List<String> groups = new ArrayList<String>();
	private Transaction transaction;
	private Map<String, String> identifyingProperties;
	private String callType;
	private Map<String, Object> requestContext = new HashedMap<>();

	public CallerContext getContext() {
		return context;
	}

	public void setContext(CallerContext context) {
		this.context = context;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCorelationId() {
		return corelationId;
	}

	public void setCorelationId(String corelationId) {
		this.corelationId = corelationId;
	}

	public String getEin() {
		return ein;
	}

	public void setEin(String ein) {
		this.ein = ein;
	}

	public String[] getPathParams() {
		return pathParams;
	}

	public void setPathParams(String[] pathParams) {
		if (null == pathParams || pathParams.length <= 0) {
			this.pathParams = null;
			return;
		}
		this.pathParams = Arrays.copyOf(pathParams, pathParams.length);
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}

	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams.clear();
		if (null == queryParams || queryParams.isEmpty()) {
			return;
		}
		this.queryParams.putAll(queryParams);
	}

	public void addQueryParams(Map<String, String> queryParams) {
		if (null == queryParams || queryParams.isEmpty()) {
			return;
		}
		this.queryParams.putAll(queryParams);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups.clear();

		if (null == groups || groups.length() == 0)
			return;

		String[] groupArray = groups.split(",");
		if (null == groupArray)
			return;

		for (int count = 0; count < groupArray.length; count++) {
			this.groups.add(groupArray[count]);

		}
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public Map<String, String> getIdentifyingProperties() {
		return identifyingProperties;
	}

	public void setIdentifyingProperties(Map<String, String> identifyingProperties) {
		this.identifyingProperties = identifyingProperties;
	}

	public String getCallType() {
		return callType;
	}

	public void setCallType(String callType) {
		this.callType = callType;
	}

	public Map<String, Object> getRequestContext() {
		return requestContext;
	}

	public void setRequestContext(Map<String, Object> requestContext) {
		this.requestContext.clear();
		if (null == requestContext || requestContext.isEmpty()) {
			return;
		}
		this.requestContext.putAll(requestContext);
	}

	public void addRequestContext(Map<String, Object> requestContext) {
		if (null == requestContext || requestContext.isEmpty()) {
			return;
		}
		this.requestContext.putAll(requestContext);
	}
}
