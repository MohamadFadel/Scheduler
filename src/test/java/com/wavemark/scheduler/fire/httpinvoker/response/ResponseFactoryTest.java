package com.wavemark.scheduler.fire.httpinvoker.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResponseFactoryTest {

	@Mock
	Response response;
	@Mock
	Exception exception;

	@Test
	void testInitializeHttpResponse() throws IOException {

		ResponseBody responseBody = mock(ResponseBody.class);
		when(response.body()).thenReturn(responseBody);
		when(responseBody.string()).thenReturn("{\"success\":true,\"code\":200,\"series\":\"series\",\"phrase\":\"phrase\",\"message\":\"message\",\"cause\":\"cause\"}");

		Request request = mock(Request.class);
		when(response.request()).thenReturn(request);
		HttpUrl httpUrl = mock(HttpUrl.class);
		when(request.url()).thenReturn(httpUrl);
		when(httpUrl.encodedPath()).thenReturn("url");

		HttpResponse httpResponse = ResponseFactory.initializeHttpResponse(response);

		assertNotNull(httpResponse);
		assertTrue(httpResponse.isSuccess());
		assertEquals(200, httpResponse.getCode());
		assertEquals("series", httpResponse.getSeries());
		assertEquals("phrase", httpResponse.getPhrase());
		assertEquals("message", httpResponse.getMessage());
		assertEquals("cause", httpResponse.getCause());
		assertEquals("url", httpResponse.getUrl());
	}

	@Test
	void testInitializeHttpResponseException() throws IOException {

		ResponseBody responseBody = mock(ResponseBody.class);
		when(response.body()).thenReturn(responseBody);
		when(responseBody.string()).thenReturn("");

		Request request = mock(Request.class);
		when(response.request()).thenReturn(request);
		HttpUrl httpUrl = mock(HttpUrl.class);
		when(request.url()).thenReturn(httpUrl);
		when(httpUrl.encodedPath()).thenReturn("url");

		when(response.code()).thenReturn(500);

		HttpResponse httpResponse = ResponseFactory.initializeHttpResponse(response);

		assertNotNull(httpResponse);
		assertFalse(httpResponse.isSuccess());
		assertEquals(500, httpResponse.getCode());
		assertEquals("", httpResponse.getMessage());
		assertEquals("", httpResponse.getCause());
		assertEquals("url", httpResponse.getUrl());
	}

	@Test
	void testInitializeHttpErrorResponse() {

		when(exception.getMessage()).thenReturn("error message");

		HttpResponse httpResponse = ResponseFactory.initializeHttpResponse(exception, "url");

		assertNotNull(httpResponse);
		assertFalse(httpResponse.isSuccess());
		assertEquals(500, httpResponse.getCode());
		assertEquals("SERVER_ERROR", httpResponse.getSeries());
		assertEquals("Internal Server Error", httpResponse.getPhrase());
		assertEquals("error message", httpResponse.getMessage());
		assertEquals("url", httpResponse.getUrl());
	}
}