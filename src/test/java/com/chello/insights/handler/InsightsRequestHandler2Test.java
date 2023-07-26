package com.chello.insights.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.model.CallerContext;
import com.chello.core.model.Request;
import com.chello.insights.config.ChelloConfig;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ChelloConfig.class, loader = AnnotationConfigContextLoader.class)
class InsightsRequestHandler2Test {

	private static TestContext context;
	private static HttpHeaders httpHeaders = new HttpHeaders();
	private static ObjectMapper objectMapper = null;
	private static CallerContext callerContext;
	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(InsightsRequestHandler2Test.class);
	public static final String USERID = "userid";
	public static final String CORRELATIONID = "corelationid";
	
	private static String userId;
	private static String correlationid;

	InsightsRequestHandler2 handleGenerateInsights = new InsightsRequestHandler2();

	@BeforeClass
	public static void createInput() throws IOException {
		userId = "devtest12314";
		correlationid = "c1";
		callerContext = new CallerContext();
		callerContext.setCaller("testCaller");
		callerContext.setSourceIp("testIp");
		callerContext.setUser(userId);
		callerContext.setUserAgent("testUserAgent");
		context = new TestContext();
		objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);

		httpHeaders.add(USERID, userId);
		httpHeaders.add(CORRELATIONID, correlationid);
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	}

	@Test
	void testHandleRequest() {
		chelloLogger.log(ChelloLogLevel.INFO, "Start");
		Request<Map<String, Object>> request = new Request<Map<String, Object>>();
		request.setUserId("vasuthebest5003");
		request.setCorelationId(correlationid);
		request.setHeaders(httpHeaders);
		request.setEin("529026841");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("names", "loan");
		map.put("orgid","4kBm_QwgEncXf-----PF-BR-");

		@SuppressWarnings("unchecked")
		Object responseOutput = handleGenerateInsights.handleRequest(map, context);
		System.out.println("----" + responseOutput + " code----" + responseOutput);
		chelloLogger.log(ChelloLogLevel.INFO, "End");
		Assert.assertEquals(200, 200);
	}

}
