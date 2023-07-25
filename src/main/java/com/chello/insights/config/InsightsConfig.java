package com.chello.insights.config;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.chello.core.exception.CustomResponseErrorHandler;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.util.api.ISSMSecretStore;
import com.chello.insights.constants.InsightConstants;
import com.chello.insights.constants.InsightErrorCodes;
import com.chello.insights.util.SSMParametersUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is configuration class for insight lambda.
 */
@Configuration
@ComponentScan(basePackages = { "com.chello" })
public class InsightsConfig {

	/** The chelloLogger to log the request and responses for tracking. */
	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(InsightsConfig.class);

	/** The ssm parameters util. */
	@Autowired
	private SSMParametersUtil ssmParametersUtil;

	/** The ssm secret store. */
	@Autowired
	private ISSMSecretStore issmSecretStore;

	/**
	 * Fixed thread pool.
	 *
	 * @return the executor service
	 */
	@Bean("fixedThreadPool")
	public ExecutorService fixedThreadPool() {
		return Executors.newFixedThreadPool(4);
	}

	/**
	 * Gets the object mapper.
	 *
	 * @return the object mapper
	 */
	@Bean
	public ObjectMapper getObjectMapper() {
		return new ObjectMapper();
	}
	
	/**
	 * Rest template.
	 *
	 * @return restTemplate
	 */
	@Bean(name = "wordPressRestTemplate")
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new CustomResponseErrorHandler());
		return restTemplate;
	}
}