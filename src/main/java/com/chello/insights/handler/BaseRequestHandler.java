package com.chello.insights.handler;

import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;

/**
 * This abstract class has common methods and properties for request handler
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public abstract class BaseRequestHandler implements IRequestHandler {

	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(BaseRequestHandler.class);

}
