package com.chello.core.exception;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;

/**
 * This is customer response handler class which invokes when any error occurs
 * when RestTemplate raise any exception. This class also specific which type of
 * error series it should handle like 4xx, 5xx etc.
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
public class CustomResponseErrorHandler implements ResponseErrorHandler {

	/** The Constant chelloLogger. */
	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(CustomResponseErrorHandler.class);

	/**
	 * Checks for error and take action only for error series which are mentioned in
	 * this method.
	 *
	 * @param clientHttpResponse the client http response
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
		HttpStatus status = clientHttpResponse.getStatusCode();
		return status.is4xxClientError() || status.is5xxServerError();
	}

	/**
	 * Handle error if any exception occured by rest template method.
	 *
	 * @param response the response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		// extract response body from error response
		String responseAsString = toString(response.getBody());

		// convert error response to json object to look for specific json key to
		// extract message
		JSONObject errorJsonObject = new JSONObject(responseAsString);

		// this will set error message as plain text whatever received from http
		// response
		String errorMessage = responseAsString;

		// this is specific to savana. Savana passes error message inside "Errors"
		// object as JSON Array
		// so this logic will join all array messages into one line
		if (errorJsonObject.has("Errors")) {
			JSONArray errorArray = errorJsonObject.getJSONArray("Errors");
			errorMessage = errorArray.join(",");
			errorMessage = errorMessage.replace("\"", "");
		}

		// if error series is 4xx then raise business exception
		if (response.getStatusCode().is4xxClientError())
			throw new ChelloBusinessException(response.getRawStatusCode(), errorMessage);

		// if error series is 4xx then raise technical exception
		else if (response.getStatusCode().is5xxServerError())
			throw new ChelloTechnicalException(response.getRawStatusCode(), errorMessage);

	}

	/**
	 * Read input stream and convert into single string.
	 *
	 * @param inputStream the input stream
	 * @return the string
	 */
	private String toString(InputStream inputStream) {
		try {
			return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			chelloLogger.log(ChelloLogLevel.ERROR, "Error while reading inputstream from error", e);
			return null;
		}
	}

}