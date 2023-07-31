package com.chello.insights.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.chello.core.model.Request;
import com.chello.core.model.Response;

/**
 * This interface will have contract to handle the API request. Every
 * microservice can have more than one handler depending upon number of end
 * points it is serving to
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public interface IRequestHandler {

	/**
	 * This method will handle the request
	 */
	public Response handleRequest(Request request, Context context);

}
