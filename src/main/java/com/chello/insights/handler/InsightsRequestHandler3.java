package com.chello.insights.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.util.Pair;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chello.core.exception.ChelloTechnicalException;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.model.Request;
import com.chello.core.model.Response;
import com.chello.core.util.api.ISSMParameterStore;
import com.chello.insights.config.ChelloConfig;

/**
 * This is handler class for all insights lambda handlers, This class will
 * return respective handler based on endpoint and http method type provided as
 * parameter.
 */
public class InsightsRequestHandler3 implements RequestHandler<Map<String, Object>, String> {

	public static final String WARMP_UP_KEY = "message";
	public static final String QUERY_STRING_PARAMS = "queryStringParameters";

	@Autowired
	private ISSMParameterStore issmParameterStore;

	/** The handle create SSO user. */
	@Autowired
	private GetDataRequestHandler getHandler;

	/** The handle update practice info. */
	@Autowired
	private PostDataRequestHandler postHandler;

	static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
			ChelloConfig.class.getPackage().getName());

	/**
	 * No argument Constructor to initialize beans.
	 */

	public InsightsRequestHandler3() {
		chelloLogger.log(ChelloLogLevel.DEBUG, "Initializing InsightsRequestHandler2");
		ctx.getAutowireCapableBeanFactory().autowireBean(this);
	}

	/** The chelloLogger to log the request and responses for tracking. */
	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(InsightsRequestHandler3.class);

	public String handleRequest(Map<String, Object> eventMap, Context context) {
		List<Response<?>> responses = new ArrayList<>();
		Request<?> request = new Request<>();
		Pair<String, String> endPoint = null;

		try {
			endPoint = getEndpoint(eventMap);
			IRequestHandler requestHanlder = getRequestHandler(endPoint.getFirst(), endPoint.getSecond());
			Response<?> response = requestHanlder.handleRequest(request, context);

			chelloLogger.log(ChelloLogLevel.INFO, "After lambda handleRequest() response: " + response.toString(),
					request.getCorelationId());
			responses.add(response);
		} catch (Exception ex) {
			Response<?> errorResponse = new Response<>();
			responses.add(errorResponse);
		}
		return "Success";
	}

	public String handleRequest2(Map<String, Object> event, Context context) {

		String topicArn = issmParameterStore.getParameterByKey("/baselambda/sns_user_login");

		return String.format("Success. Called outbound services %s times.", topicArn);
	}

	final private static int BOUND = 4;

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

	@SuppressWarnings("unchecked")
	public Pair<String, String> getEndpoint(Map<String, Object> request) throws ChelloTechnicalException {
		// warm_up & clearcontextcache handling
		List<Pair<String, String>> endPoints = new ArrayList<>();
		Object eventSource = request.get(WARMP_UP_KEY);
		if (null == eventSource && null != request.get(QUERY_STRING_PARAMS)) {
			Map<String, Object> body = (Map<String, Object>) request.get(QUERY_STRING_PARAMS);
			eventSource = ((null == body) ? null : body.get(WARMP_UP_KEY));
		}
		if (null != eventSource) {
			String endPoint = eventSource.toString();
			String httpMethod = HttpMethod.GET.toString();
			endPoints.add(Pair.of(endPoint.toLowerCase().trim(), httpMethod.toLowerCase().trim()));
			return endPoints.get(0);
		}
		return null;
	}

	protected IRequestHandler getRequestHandler(String endPoint, String httpMethod) {
		IRequestHandler requestHanlder = null;
		switch (endPoint + ":" + httpMethod) {
		case "data:post":
			return postHandler;
		case "data:get":
			return getHandler;
		default:
			chelloLogger.log(ChelloLogLevel.INFO, "Unknown resource : " + endPoint);
			break;
		}
		chelloLogger.log(ChelloLogLevel.INFO, "Completed getRequestHandler");
		return requestHanlder;
	}
}
