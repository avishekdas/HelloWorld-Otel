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

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.chello.core.cache.api.ICacheOperations;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.model.Request;
import com.chello.core.model.Response;
import com.chello.core.util.api.ISSMParameterStore;
import com.chello.insights.config.ChelloConfig;
import com.chello.insights.constants.InsightErrorCodes;

/**
 * This is handler class for all insights lambda handlers, This class will
 * return respective handler based on endpoint and http method type provided as
 * parameter.
 */
public class GetDataRequestHandler extends BaseRequestHandler {

	@Autowired
	private ISSMParameterStore issmParameterStore;

	@Autowired
	private ICacheOperations<String> iCacheOperations;

	static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
			ChelloConfig.class.getPackage().getName());

	/**
	 * No argument Constructor to initialize beans.
	 */
	
	  public GetDataRequestHandler() { 
		  chelloLogger.log(ChelloLogLevel.DEBUG, "Initializing GetDataRequestHandler");
		  ctx.getAutowireCapableBeanFactory().autowireBean(this); 
	  }
	 

	/** The chelloLogger to log the request and responses for tracking. */
	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(GetDataRequestHandler.class);

	public Response handleRequest(Request request, Context context) {
		chelloLogger.log(ChelloLogLevel.INFO, "GetDataRequestHandler: Enterted in handleRequest() eventMap : " + request);

		Response<String> response = new Response<>();
		chelloLogger.log(ChelloLogLevel.INFO, "Enter GetDataRequestHandler : HandleGetInsights" + request,
				request.getCorelationId());
		try {
			initDynamoDbClient();
			String output = getItem();
			
			chelloLogger.log(ChelloLogLevel.INFO, "Fetching value from cache", request.getCorelationId());
			String savanaOrgId = iCacheOperations.find("avistest072501_SAVANA_ORG_ID");
			chelloLogger.log(ChelloLogLevel.INFO, "Cached value: " + savanaOrgId, request.getCorelationId());
			
			response.setResponseBody(output);
			response.setHttpStatusCode(200);
			response.setResponseCode(InsightErrorCodes.SUCCESS.getCode());

		} catch (Exception e) {
			chelloLogger.log(ChelloLogLevel.ERROR, e.getMessage() + " due to: " + e.getCause() + e,
					request.getCorelationId());
		}
		chelloLogger.log(ChelloLogLevel.INFO, "AbstractBaseLambda: Exit from handleRequest() apiResponse: ",
				request.getCorelationId());

		return response;
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
		key_to_get.put("PRCSS_CD", new AttributeValue("RGST"));

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
