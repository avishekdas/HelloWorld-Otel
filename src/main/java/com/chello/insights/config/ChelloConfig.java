package com.chello.insights.config;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Objects;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClient;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.chello.core.exception.ChelloException;
import com.chello.core.exception.CustomResponseErrorHandler;
import com.chello.core.logging.api.ChelloLogLevel;
import com.chello.core.logging.api.ChelloLogger;
import com.chello.core.logging.api.ChelloLoggerFactory;
import com.chello.core.util.api.ISSMParameterStore;
import com.chello.core.util.api.ISSMSecretStore;
import com.chello.insights.util.ErrorCodes;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

/**
 * Configuration class along with component scan provided for bean creation on
 * app start
 * 
 * @author Cognizant
 * @version 1.0
 * @since 2022-11-01
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = "com.chello")
public class ChelloConfig {

	private static final ChelloLogger chelloLogger = ChelloLoggerFactory.getInstance(ChelloConfig.class);
	public static final Integer DEFAULT_REDIS_PORT = 6379;
	public static final Regions REGION = Regions.fromName(System.getenv("AWS_REGION"));
	public static final String TPS_KEY_STORE_PATH = "chello_key_store_path";
	public static final String TPS_TRUST_STORE_PATH = "chello_trust_store_path";
	public static final String TPS_KEYSTORE_PASS = "chello_keystore_pass";
	public static final String TPS_TRUSTSTORE_PASS = "chello_trustore_pass";
	public static final String TPS_TRUST_KEY_AUTH = "chello_store_pass";
	public static final Region SINGLE_REGION = Region.of(System.getenv("AWS_REGION"));

	@Autowired
	private ISSMParameterStore issmParameterStore;

	@Autowired
	private ISSMSecretStore issmSecretStore;

	private String assumeRoleARN = System.getenv("assume_role_arn");

	/**
	 * Create and returns JedisConnectionFactory
	 * 
	 * @return JedisConnectionFactory
	 * @throws Exception
	 */
	@Lazy
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() throws Exception {

		JedisConnectionFactory jedisConFactory = null;
		String sslAuthToken = issmSecretStore.getSecretsValue("redis_cluster", "ssl_auth_token");
		try (JedisCluster jc = new JedisCluster(
				Collections.singleton(new HostAndPort(issmParameterStore.getParameterByKey("redis_cluster_host"),
						Objects.nonNull(Integer.parseInt(issmParameterStore.getParameterByKey("redis_cluster_port")))
								? Integer.parseInt(issmParameterStore.getParameterByKey("redis_cluster_port"))
								: DEFAULT_REDIS_PORT)),
				DefaultJedisClientConfig.builder().password(sslAuthToken).ssl(true).build(), 50,
				new JedisPoolConfig())) {

			RedisClusterConfiguration conf = new RedisClusterConfiguration(jc.getClusterNodes().keySet());
			// Setting password required in conf for test environment
			conf.setPassword(sslAuthToken);
			JedisClientConfiguration confJedis = JedisClientConfiguration.builder().useSsl().build();
			jedisConFactory = new JedisConnectionFactory(conf, confJedis);

		} catch (Exception exp) {
			throw new ChelloException(exp);
		}
		return jedisConFactory;
	}

	/**
	 * passing JedisConnectionFactory via redisTemplate to connect to redis
	 * 
	 * @param <T>
	 * @param jedisConnectionFactory
	 * @return
	 * @throws Exception
	 */
	@Lazy
	@Bean(value = "redisTemplate")
	public <T> RedisTemplate<String, T> redisTemplate(JedisConnectionFactory jedisConnectionFactory) throws Exception {

		RedisTemplate<String, T> redisTemplate = null;

		if (Objects.nonNull(jedisConnectionFactory)) {

			redisTemplate = new RedisTemplate<>();
			redisTemplate.setConnectionFactory(jedisConnectionFactory);
		}

		return redisTemplate;
	}

	/**
	 * Creates dynamoDBMapperConfig
	 * 
	 * @return DynamoDBMapperConfig
	 */
	@Lazy
	@Bean
	public DynamoDBMapperConfig dynamoDBMapperConfig() {

		return DynamoDBMapperConfig.builder()
				.withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES).build();

	}

	/**
	 * Returns dynamoDBMapper
	 * 
	 * @param amazonDynamoDB
	 * @param config
	 * @return
	 */
	@Lazy
	@Bean
	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB, DynamoDBMapperConfig config) {
		return new DynamoDBMapper(amazonDynamoDB, config);
	}

	/**
	 * Creates and returns AmazonDynamoDB instance
	 * 
	 * @return AmazonDynamoDB
	 */
	@Lazy
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		chelloLogger.log(ChelloLogLevel.DEBUG, "Region received from ENV Variable: " + REGION);
		if (null == assumeRoleARN) {
			return AmazonDynamoDBClientBuilder.standard().withRegion(REGION).build();
		} else {
			// Temporary security credentials
			return AmazonDynamoDBClientBuilder.standard().withRegion(REGION)
					.withCredentials(getTemporaryAssumeRoleCredentials()).build();
		}
	}

	/**
	 * Create RestTemplate
	 * 
	 * @return restTemplate
	 */
	@Lazy
	@Bean(name = "restTemplate")
	public RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
		restTemplate.setErrorHandler(new CustomResponseErrorHandler());
		return restTemplate;
	}

	/**
	 * Returns and create HttpComponentsClientHttpRequestFactory
	 * 
	 * @return getClientHttpRequestFactory.
	 */
	@Lazy
	@Bean
	public HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {
		final HttpComponentsClientHttpRequestFactory clientFactory = new HttpComponentsClientHttpRequestFactory();
		clientFactory.setHttpClient(httpClient());
		return clientFactory;
	}

	/**
	 * Creates and returns HttpClient
	 * 
	 * @return httpClient
	 */
	@Lazy
	@Bean
	public HttpClient httpClient() {
		final CredentialsProvider creProvider = new BasicCredentialsProvider();
		/*
		 * creProvider.setCredentials(AuthScope.ANY, new
		 * UsernamePasswordCredentials(issmSecretStore.getSecretsValue(
		 * "savana_auth_username", "username"),
		 * issmSecretStore.getSecretsValue("savana_auth_password", "password")));
		 */

		return HttpClientBuilder.create().setDefaultCredentialsProvider(creProvider).build();

	}

	/**
	 * Creates and returns AWSSimpleSystemsManagement
	 * 
	 * @return
	 */
	@Bean
	public AWSSimpleSystemsManagement simpleSystemsManagementClient() {
		chelloLogger.log(ChelloLogLevel.DEBUG, "Region received from ENV Variable: " + REGION);
		if (null == assumeRoleARN) {
			return AWSSimpleSystemsManagementClientBuilder.standard().withRegion(REGION).build();
		} else {
			// Temporary security credentials
			return AWSSimpleSystemsManagementClientBuilder.standard().withRegion(REGION)
					.withCredentials(getTemporaryAssumeRoleCredentials()).build();
		}
	}

	/**
	 * Creates and returns AWSSecretsManager
	 * 
	 * @return AWSSecretsManager
	 */
	@Bean
	public AWSSecretsManager secretsManagerClient() {
		chelloLogger.log(ChelloLogLevel.DEBUG, "Region received from ENV Variable: " + REGION);
		if (null == assumeRoleARN) {
			return AWSSecretsManagerClientBuilder.standard().withRegion(REGION).build();
		} else {
			// Temporary security credentials
			return AWSSecretsManagerClientBuilder.standard().withRegion(REGION)
					.withCredentials(getTemporaryAssumeRoleCredentials()).build();
		}
	}

	/**
	 * Creates and returns AWSStaticCredentialsProvider
	 * 
	 * @return AWSStaticCredentialsProvider
	 */
	private AWSStaticCredentialsProvider getTemporaryAssumeRoleCredentials() {
		chelloLogger.log(ChelloLogLevel.INFO, "inside getTemporaryAssumeRoleCredentials");
		chelloLogger.log(ChelloLogLevel.INFO, "assumeRoleARN: " + assumeRoleARN);

		AWSSecurityTokenServiceClient stsClient = new AWSSecurityTokenServiceClient();

		AssumeRoleRequest assumeRequest = new AssumeRoleRequest().withRoleArn(assumeRoleARN).withDurationSeconds(3600)
				.withRoleSessionName("testSession_DynamoDB");

		AssumeRoleResult assumeResult = stsClient.assumeRole(assumeRequest);

		// Step 2. AssumeRole returns temporary security credentials for the IAM role.
		BasicSessionCredentials temporaryCredentials = new BasicSessionCredentials(
				assumeResult.getCredentials().getAccessKeyId(), assumeResult.getCredentials().getSecretAccessKey(),
				assumeResult.getCredentials().getSessionToken());

		return new AWSStaticCredentialsProvider(temporaryCredentials);
	}

	/**
	 * Create and returns SSLContext
	 * 
	 * @return SSLContext
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws UnrecoverableKeyException
	 * @throws KeyManagementException
	 */
	@Lazy
	@Bean
	public SSLContext getSSLContext() throws CertificateException, KeyStoreException, IOException,
			NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

		SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
		try {
			// server
			String pathToKeystoreJKS = issmParameterStore.getParameterByKey(TPS_KEY_STORE_PATH);
			String pathToTruststoreJKS = issmParameterStore.getParameterByKey(TPS_TRUST_STORE_PATH);

			// local
//			 String pathToKeystoreJKS = "C:\\temp\\foo\\26Aug2022\\chellokeystore";
//			 String pathToTruststoreJKS = "C:\\temp\\foo\\26Aug2022\\chellotruststore";

			String keyStorePassword = issmSecretStore.getSecretsValue(TPS_TRUST_KEY_AUTH, TPS_KEYSTORE_PASS);
			String trustStorePassword = issmSecretStore.getSecretsValue(TPS_TRUST_KEY_AUTH, TPS_TRUSTSTORE_PASS);

			KeyStore keyStore = KeyStore.getInstance(Paths.get(pathToKeystoreJKS).toFile(),
					keyStorePassword.toCharArray());

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(keyStore, keyStorePassword.toCharArray());

			KeyStore trustStore = KeyStore.getInstance(Paths.get(pathToTruststoreJKS).toFile(),
					trustStorePassword.toCharArray());

			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(trustStore);

			sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		} catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException
				| UnrecoverableKeyException | KeyManagementException | IllegalArgumentException exp) {
			chelloLogger.log(ChelloLogLevel.INFO, ErrorCodes.TPS_SSLCONTEXT_INSTANTIATION_FAILED.getMessage());
		}

		return sslContext;
	}

	/**
	 * Method to create SnsClient bean
	 * 
	 * @return SnsClient
	 */
	@Bean
	public AmazonSNS amazonSNS() {
		return AmazonSNSClientBuilder.standard().withRegion(REGION).build();
	}

	/**
	 * Method to create cognito bean
	 * 
	 * @return AWSCognitoIdentityProvider
	 */
	@Bean
	public AWSCognitoIdentityProvider getAWSCognitoIdentityProvider() {

		chelloLogger.log(ChelloLogLevel.INFO, "Region received from ENV Variable: " + REGION);

		if (null == assumeRoleARN) {
			return AWSCognitoIdentityProviderClientBuilder.standard().withRegion(REGION).build();
		} else {
			// Temporary security credentials
			return AWSCognitoIdentityProviderClientBuilder.standard().withRegion(REGION)
					.withCredentials(getTemporaryAssumeRoleCredentials()).build();
		}
	}

	/**
	 * Method to create S3client bean
	 * 
	 * @return AmazonS3
	 */
	@Bean
	public AmazonS3 getAmazonS3() {
		return AmazonS3ClientBuilder.standard().build();
	}

	/**
	 * Method to create sqsClient bean
	 * 
	 * @return sqsClient
	 */
	@Bean
	public SqsClient getSqsClient() {
		chelloLogger.log(ChelloLogLevel.INFO, "Region received from ENV Variable: " + SINGLE_REGION);
		return SqsClient.builder().region(SINGLE_REGION).build();
	}
}
