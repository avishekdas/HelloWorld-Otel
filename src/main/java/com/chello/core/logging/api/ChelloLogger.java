package com.chello.core.logging.api;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;

import com.chello.core.exception.ChelloException;
import com.chello.core.model.Request;
import com.chello.core.model.logging.ChelloLog;
import com.chello.core.model.logging.Payload;
import com.chello.core.model.logging.Status;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;

/**
 * The Class is the wrapper implementation on log4j logger methods. This class
 * has exposed overloaded logging methods to be exposed to external systems.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public class ChelloLogger {
	
	public static final String CORRELATION_ID = "correlation-id";
	public static final String MESSAGE = "message";
	public static final String COLON = ":";
	public static final String SPACE = " ";
	public static final String COMMA = ",";
	public static final String ERROR = "ERROR";
	public static final String REQUEST = "REQUEST";
	public static final String RESPONSE = "RESPONSE";
	public static final String PARTNER_NAME = "SAVANA";
	public static final Integer STRACKTRACE_FROM_RANGE = 0;
	public static final Integer STRACKTRACE_TO_RANGE = 10;

	/**
	 * The static logger instance with singleton scope. Singleton implementation is
	 * responsible for maintaining order of async logger calls.
	 */
	private static ChelloLogger loggerInstance = null;
	
	private final static ObjectMapper objectMapper = new ObjectMapper();

	/** The default logger. */
	private static Logger logger = LogManager.getLogger(ChelloLogger.class);

	/**
	 * Private constructor to protect creation of multiple copies of this class.
	 */
	private ChelloLogger() {
	}

	/**
	 * Gets the single instance of ChelloLogger.
	 *
	 * @param logger the logger
	 * @return single instance of ChelloLogger
	 */
	public static ChelloLogger getInstance(Logger logger) {
		if (loggerInstance == null)
			loggerInstance = new ChelloLogger();

		loggerInstance.setLogger(logger);
		return loggerInstance;
	}

	/**
	 * Sets the logger.
	 *
	 * @param logger the new logger
	 */
	private void setLogger(Logger logger) {
		ChelloLogger.logger = logger;
	}

	/**
	 * log method to be consumed by other consumer classes. It will accept two input
	 * parameters, first is log level and second is chello log object
	 *
	 * @param logLevel the log level
	 * @param appLog   the app log object
	 */
	public void log(ChelloLogLevel logLevel, ChelloLog appLog) {

		CompletableFuture.runAsync(() -> {

			ObjectMapper objectMapper = new ObjectMapper();

			try {
				String logMessage = objectMapper.writeValueAsString(appLog);
				logMessage(logLevel, logMessage, null);

			} catch (JsonProcessingException e) {
				logger.error("Error while parsing ChelloLog object to JSON");
			}

		});
	}

	/**
	 * 
	 * This is overloaded log method to be consumed by other consumer classes. It
	 * will accept two input parameters, first is log level and second is log
	 * message
	 *
	 * @param logLevel the log level
	 * @param message  the message
	 */
	public void log(ChelloLogLevel logLevel, String message) {

		CompletableFuture.runAsync(() -> doLog(logLevel, message, null, null));
	}

	/**
	 * 
	 * This is overloaded log method to be consumed by other consumer classes. It
	 * will accept three input parameters, first is log level, second is log message
	 * and third is throwable exception object
	 *
	 * @param logLevel the log level
	 * @param message  the message
	 * @param error    the wrapped error
	 */
	public void log(ChelloLogLevel logLevel, String message, Throwable error) {

		CompletableFuture.runAsync(() -> doLog(logLevel, message, null, error));
	}

	/**
	 * This is overloaded log method to be consumed by other consumer classes. It
	 * will accept three input parameters, first is log level and second is message
	 * and third is correlation-id
	 *
	 * @param logLevel      the log level
	 * @param message       the message
	 * @param correlationId the correlation id
	 */
	public void log(ChelloLogLevel logLevel, String message, String correlationId) {

		CompletableFuture.runAsync(() -> doLog(logLevel, message, correlationId, null));
	}

	/**
	 * Do log for log level, message and correlation-id
	 *
	 * @param logLevel      the log level
	 * @param message       the message
	 * @param correlationId the correlation id
	 */
	private void doLog(ChelloLogLevel logLevel, String message, String correlationId, Throwable error) {

		StringBuilder logMessage = new StringBuilder();

		if (Objects.nonNull(correlationId) && !correlationId.isEmpty()) {
			logMessage.append(CORRELATION_ID + COLON + SPACE
					+ correlationId + COMMA + SPACE);
		}

		logMessage.append(MESSAGE + COLON + SPACE + message);

		logMessage(logLevel, logMessage.toString(), error);

	}

	/**
	 * Log message based on log level and message
	 *
	 * @param logLevel the log level
	 * @param message  the message
	 */
	private void logMessage(ChelloLogLevel logLevel, String message, Throwable error) {

		switch (logLevel) {
		case DEBUG:
			logger.debug(message);
			break;
		case INFO:
			logger.info(message);
			break;
		case WARN:
			if (error != null)
				logger.warn(message, error);
			else
				logger.warn(message);
			break;
		case ERROR:
			if (error != null)
				logger.error(message, error);
			else
				logger.error(message);
			break;
		case TRACE:
			logger.trace(message);
			break;
		default:
			break;
		}
	}

	/**
	 * This method returns true/false if debug is enabled as root log level or not.
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	/**
	 * This is overloaded log method to be consumed by other consumer classes. It
	 * will accept request, response and error objects and convert to chello audit
	 * log object and print in log
	 *
	 * @param logLevel      the log level
	 * @param correlationId the correlation id
	 * @param request       the request
	 * @param response      the response
	 * @param error         the ChelloException error
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void log(ChelloLogLevel logLevel, String correlationId, Request request, ResponseEntity<Object> response,
			ChelloException error) {

		CompletableFuture.runAsync(() -> {

			ChelloLog auditLog = prepareChelloLogObject(request, response, error);

			String message = new GsonBuilder().setPrettyPrinting().create().toJson(auditLog);

			StringBuilder logMessage = new StringBuilder();

			if (Objects.nonNull(correlationId) && !correlationId.isEmpty()) {
				logMessage.append(CORRELATION_ID + COLON + SPACE
						+ correlationId + COMMA + SPACE);
			}

			logMessage.append(MESSAGE + COLON + SPACE + message);

			logMessage(logLevel, logMessage.toString(), null);

			// logging for kibana
			if (null != response || null != error) {
				auditLog.setActivity("ELK-Logging");
				logMessage(logLevel, new GsonBuilder().setPrettyPrinting().create().toJson(auditLog), null);
			}

		});
	}

	/**
	 * This method is used to convert request, response and error object to chello
	 * audit log object for logging.
	 *
	 * @param request  the request object
	 * @param response the response object
	 * @param error    the error object which has exception detail
	 * @return the chello log object
	 */
	private ChelloLog prepareChelloLogObject(Request<String> request, ResponseEntity<Object> response,
			ChelloException error) {

		ChelloLog auditLog = new ChelloLog();

		auditLog.setApiURL(request.getEndPoint());
		auditLog.setCoRelationId(request.getCorelationId());
		auditLog.setCustomHeaders(request.getHeaders().toSingleValueMap());
		auditLog.setHttpMethod(request.getHttpMethod());
		auditLog.setInvocationTimeStamp(new Timestamp(new Date().getTime()));
		auditLog.setPartnerName(PARTNER_NAME);

		Payload payload = new Payload();

		if (error != null) { // set error details to audit log object

			payload.setType(ERROR);
			payload.setData(error.getMessage());

			Status errorStatus = new Status();
			errorStatus.setCode(error.getCode());
			errorStatus.setDescription(error.getMessage());

			errorStatus.setStackTrace(Arrays.toString(error.getStackTrace()));

			auditLog.setStatus(errorStatus);

		}
		if (response != null) { // set response details to audit log object

			payload.setType(RESPONSE);
			payload.setData((new GsonBuilder().setPrettyPrinting().create().toJson(response)));

			String resBody = objectToString(response.getBody());
			Status responseStatus = new Status();
			responseStatus.setCode(response.getStatusCode().value());
			responseStatus.setDescription(resBody);

			auditLog.setStatus(responseStatus);

		}

		payload.setType(REQUEST);
		payload.setData((new GsonBuilder().setPrettyPrinting().create().toJson(request.getBody())));
		payload.setPathParam(Arrays.toString(request.getPathParams()));
		payload.setQueryParam(request.getQueryParams().toString());

		auditLog.setPayload(payload);
//		auditLog.setUserAgent(request.getContext() != null ? request.getContext().getUserAgent() : null);
		auditLog.setUserId(request.getUserId());

		return auditLog;

	}
	
	/**
	 * Method to convert Object to String
	 * 
	 * @param data
	 * @return
	 */
	public static String objectToString(Object data) {

		if (data == null)
			return null;

		// if the data is already String then return the string no change ...
		// otherwise it adds quote
		if (data instanceof String)
			return data.toString();

		objectMapper.setSerializationInclusion(Include.NON_NULL);
		try {
			return objectMapper.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			System.out.println("Failed to convert object to json." + e);
		}
		return null;
	}

}
