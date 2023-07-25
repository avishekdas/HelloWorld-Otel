package com.chello.core.model;

/**
 * Business Object for CallerContext
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public class CallerContext {

	private String caller;
	private String sourceIp;
	private Object user;
	private String userAgent;

	public String getSourceIp() {
		return sourceIp;
	}

	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

}
