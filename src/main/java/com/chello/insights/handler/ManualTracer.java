package com.chello.insights.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Random;

import com.amazonaws.services.lambda.runtime.Context;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.instrumentation.awslambdacore.v1_0.TracingRequestHandler;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;
import io.opentelemetry.sdk.trace.samplers.Sampler;

public class ManualTracer extends TracingRequestHandler<Map<String, Object>, String> {

	final private int BOUND = 4;

	static SpanExporter exporter = OtlpGrpcSpanExporter.builder().setEndpoint("http://10.208.163.224:4317")
			.build();

	static SdkTracerProvider provider = SdkTracerProvider.builder()
			.addSpanProcessor(SimpleSpanProcessor.create(exporter)).setSampler(Sampler.alwaysOn()).build();

	private static final OpenTelemetrySdk SDK = OpenTelemetrySdk.builder().setTracerProvider(provider)
			.buildAndRegisterGlobal();

	private static final Tracer tracer = SDK.getTracer("NolanOT");

	public ManualTracer() {
		super(SDK);
	}

	public String doHandleRequest(Map<String, Object> event, Context context) {
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
		Span span = tracer.spanBuilder("HTTP request").setSpanKind(SpanKind.CLIENT).startSpan();
		try {
			HttpRequest request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
			HttpResponse<String> response = HttpClient.newBuilder().build().send(request,
					HttpResponse.BodyHandlers.ofString());
			System.out.println(response);
		} catch (IOException | InterruptedException | URISyntaxException e) {
			System.out.println(e);
		} finally {
			span.end();
		}
	}
}
