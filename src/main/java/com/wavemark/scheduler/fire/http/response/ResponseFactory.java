package com.wavemark.scheduler.fire.http.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Response;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
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
}
