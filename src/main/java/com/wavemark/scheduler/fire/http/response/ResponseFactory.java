package com.wavemark.scheduler.fire.http.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResponseFactory {

	public static HttpResponse initializeHttpResponse(Response response) throws IOException {
		String responseBody = Objects.requireNonNull(response.body()).string();

		ObjectMapper mapper = new ObjectMapper();
		HttpResponse httpResponse = new HttpResponse();
		try {
			httpResponse = mapper.readValue(responseBody, HttpResponse.class);
		} catch (JsonProcessingException e) {
			httpResponse.setSuccess(response.code() == 200);
			httpResponse.setCode(response.code());
			httpResponse.setMessage(responseBody);
			httpResponse.setCause(responseBody);
		}
		httpResponse.setUrl(response.request().url().encodedPath());

		return httpResponse;
	}

	public static HttpResponse buildHttpResponse(org.apache.http.HttpResponse response, String url) throws IOException {
		String responseMessage = null;
		int statusCode = response.getStatusLine().getStatusCode();
		if (response.getEntity() != null) {
			responseMessage = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		}

		boolean success = statusCode == 200 ;
		String phrase = response.getStatusLine().getReasonPhrase();
		String series = getSeries(statusCode);

		return com.wavemark.scheduler.fire.http.response.HttpResponse.builder()
				.success(success)
				.url(url)
				.code(statusCode)
				.series(series)
				.phrase(phrase)
				.message(responseMessage)
				.cause(null) // cause will be populated later if there is an exception
				.build();
	}

	public static HttpResponse initializeHttpResponse(Exception exception, String url) {

		return HttpResponse.builder()
				.success(false)
				.code(500)
				.phrase("Internal Server Error")
				.series("SERVER_ERROR")
				.message(exception.getMessage())
				.cause(ExceptionUtils.getStackTrace(exception))
				.url(url)
				.build();
	}

	private static String getSeries(int statusCode) {
		if (statusCode >= 100 && statusCode < 200) {
			return "Informational";
		} else if (statusCode >= 200 && statusCode < 300) {
			return "Successful";
		} else if (statusCode >= 300 && statusCode < 400) {
			return "Redirection";
		} else if (statusCode >= 400 && statusCode < 500) {
			return "Client Error";
		} else if (statusCode >= 500 && statusCode < 600) {
			return "Server Error";
		} else {
			return "Unknown";
		}
	}

}
