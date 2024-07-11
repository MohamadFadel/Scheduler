package com.wavemark.scheduler.fire.authentication.service;

import com.wavemark.scheduler.fire.http.client.HttpClient;
import com.wavemark.scheduler.fire.http.response.ResponseFactory;
import com.wavemark.scheduler.fire.http.response.ResponseHandler;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.testing.util.DataUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2ServiceTest {

    @Mock
    HttpClient httpClient;

    @Mock
    Response response;

    @Mock
    ResponseHandler responseHandler;

    @Mock
    OkHttpClient okHttpClient;

    @Mock
    Call call;

    @InjectMocks
    OAuth2Service oAuth2Service;

    @Test
    void testGetWardenAuthToken() throws IOException, SchedulerException, EntryNotFoundException {
        try (
                MockedStatic<HttpClient> httpClientMockedStatic = mockStatic(HttpClient.class);
                MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class);
        ) {
            httpClientMockedStatic.when(HttpClient::getInstance).thenReturn(httpClient);
            when(httpClient.getClient()).thenReturn(okHttpClient);
            when(okHttpClient.newCall(any())).thenReturn(call);
            when(call.execute()).thenReturn(response);
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any()))
                    .thenReturn(DataUtil.generateSuccessHttpResponse());

            ReflectionTestUtils.setField(oAuth2Service, "host", "https://ldec0609wmapp09.cardinalhealth.net/");

            String token = oAuth2Service.getWardenAuthToken(DataUtil.generateJobDataMap());
            assertNotNull(token);
            assertEquals("T1X-3jt11SYjIUfwGj0Rm1Y_Ym8", token);
        }
    }

    @Test
    void testGetWardenAuthToken_expiresSoon() throws IOException, SchedulerException, EntryNotFoundException {
        try (
                MockedStatic<HttpClient> httpClientMockedStatic = mockStatic(HttpClient.class);
                MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class);
        ) {
            httpClientMockedStatic.when(HttpClient::getInstance).thenReturn(httpClient);
            when(httpClient.getClient()).thenReturn(okHttpClient);
            when(okHttpClient.newCall(any())).thenReturn(call);
            when(call.execute()).thenReturn(response);
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any()))
                    .thenReturn(DataUtil.generateSuccessHttpResponseWithTokenExpiresSoon());

            ReflectionTestUtils.setField(oAuth2Service, "host", "https://ldec0609wmapp09.cardinalhealth.net/");

            String token = oAuth2Service.getWardenAuthToken(DataUtil.generateJobDataMap());
            assertNotNull(token);
            assertEquals("T1X-3jt11SYjIUfwGj0Rm1Y_Ym9", token);
        }
    }

    @Test
    void testGetWardenAuthTokenThrowsException() throws IOException, SchedulerException, EntryNotFoundException {
        try (
                MockedStatic<HttpClient> httpClientMockedStatic = mockStatic(HttpClient.class);
                MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class);
        ) {
            httpClientMockedStatic.when(HttpClient::getInstance).thenReturn(httpClient);
            when(httpClient.getClient()).thenReturn(okHttpClient);
            when(okHttpClient.newCall(any())).thenReturn(call);
            when(call.execute()).thenReturn(response);
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any()))
                    .thenReturn(DataUtil.generateBadHttpResponse());

            ReflectionTestUtils.setField(oAuth2Service, "host", "https://ldec0609wmapp09.cardinalhealth.net/");

            assertThrows(JobExecutionException.class, () -> oAuth2Service.getWardenAuthToken(DataUtil.generateJobDataMap()));
        }
    }

}