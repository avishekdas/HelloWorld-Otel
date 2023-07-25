package com.chello.insights.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.model.Request;
import com.chello.core.model.Response;
import com.chello.core.util.api.ISSMParameterStore;
import com.chello.insights.config.ChelloConfig;
import com.chello.insights.constants.InsightConstants;
import com.chello.insights.constants.InsightErrorCodes;

/**
 * This is handler class for all insights lambda handlers, This class will
 * return respective handler based on endpoint and http method type provided as
 * parameter.
 */
@Component
public class InsightsRequestHandler implements RequestHandler<Map<String, Object>, String> {

	@Autowired
	private ISSMParameterStore issmParameterStore;

	static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
			ChelloConfig.class.getPackage().getName());

	/**
	 * No argument Constructor to initialize beans.
	 */
	/*
	 * public InsightsRequestHandler() { chelloLogger.log(ChelloLogLevel.DEBUG,
	 * "Initializing InsightsRequestHandler");
	 * ctx.getAutowireCapableBeanFactory().autowireBean(this); }
	 */

	/** The chelloLogger to log the request and responses for tracking. */
	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(InsightsRequestHandler.class);

	public String handleRequest(Map<String, Object> eventMap, Context context) {
		String apiResponse;
		Request<?> request = new Request<>();
		chelloLogger.log(ChelloLogLevel.INFO, "AbstractBaseLambda: Enterted in handleRequest() eventMap : " + eventMap);

		Response<String> response = new Response<>();
		chelloLogger.log(ChelloLogLevel.INFO, "Enter handleRequest : HandleGetInsights" + request,
				request.getCorelationId());
		try {
			initDynamoDbClient();
			String output = getItem();
			response.setResponseBody(output);
			response.setHttpStatusCode(200);
			response.setResponseCode(InsightErrorCodes.SUCCESS.getCode());

		} catch (Exception e) {
			chelloLogger.log(ChelloLogLevel.ERROR, e.getMessage() + " due to: " + e.getCause() + e,
					request.getCorelationId());
		}
		apiResponse = response.getResponseBody();
		chelloLogger.log(ChelloLogLevel.INFO, "AbstractBaseLambda: Exit from handleRequest() apiResponse: ",
				request.getCorelationId());

		return apiResponse;
	}

	public String handleRequest2(Map<String, Object> event, Context context) {

		String topicArn = issmParameterStore.getParameterByKey("/baselambda/sns_user_login");

		return String.format("Success. Called outbound services %s times.", topicArn);
	}

	final private int BOUND = 4;

	public String handleRequest1(Map<String, Object> event, Context context) {
		Random rand = new Random();
		int randomCalls = rand.nextInt(BOUND) + 1;
		for (int i = 0; i < randomCalls; i++) {
			String url = ((i % 2) == 0) ? "https://postman-echo.com/get" : "https://www.google.com/";
			outboundCall(url);
			LibFile.callSecretService();
		}
		return String.format("Success. Called outbound services %s times.", randomCalls);
	}

	public void outboundCall(String url) {
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
			HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
					HttpResponse.BodyHandlers.ofString());
			System.out.println(response);
		} catch (IOException | InterruptedException | URISyntaxException e) {
			System.out.println(e);
		}
	}

	private AmazonDynamoDB amazonDynamoDB;

	private String DYNAMODB_TABLE_NAME = "PTY_RGST";
	private Regions REGION = Regions.US_EAST_1;

	private void initDynamoDbClient() {
		this.amazonDynamoDB = AmazonDynamoDBClientBuilder.standard().withRegion(REGION).build();
	}

	private String getItem() {
		GetItemRequest request = null;
		String name = "TRIZETTO#oyvp";
		String output = "";

		HashMap<String, AttributeValue> key_to_get = new HashMap<String, AttributeValue>();

		key_to_get.put("PAR_PTY_ID", new AttributeValue(name));

		request = new GetItemRequest().withKey(key_to_get).withTableName(DYNAMODB_TABLE_NAME);

//		final AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.defaultClient();

		try {
			Map<String, AttributeValue> returned_item = amazonDynamoDB.getItem(request).getItem();
			if (returned_item != null) {
				Set<String> keys = returned_item.keySet();
				for (String key : keys) {
					chelloLogger.log(ChelloLogLevel.INFO, returned_item.get(key).toString());
					output = returned_item.get(key).toString();
				}
			} else {
				chelloLogger.log(ChelloLogLevel.INFO, "No item found with the key %s!\n", name);
			}
		} catch (AmazonServiceException e) {
			chelloLogger.log(ChelloLogLevel.ERROR, e.getErrorMessage());
		}
		return output;
	}
}
