package com.wavemark.scheduler.fire.httpinvoker.requestor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.wavemark.scheduler.fire.httpinvoker.client.HttpClient;
import com.wavemark.scheduler.fire.httpinvoker.response.ResponseHandler;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.testing.util.DataUtil;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

@ExtendWith(MockitoExtension.class)
class HttpPostRequesterTest {

    @Mock
    HttpClient httpClient;

    @Mock
    OkHttpClient okHttpClient;

    @Mock
    Call call;

    @Mock
    Response response;

    @Mock
    ResponseHandler responseHandler;

    @InjectMocks
    HttpPostRequester httpPostRequester;

    @Test
    void testPostTask() throws IOException, JobExecutionException {
        try (
                MockedStatic<HttpClient> httpClientMockedStatic = mockStatic(HttpClient.class)
        ) {
            httpClientMockedStatic.when(HttpClient::getInstance).thenReturn(httpClient);
            when(httpClient.getClient()).thenReturn(okHttpClient);
            when(okHttpClient.newCall(any())).thenReturn(call);
            when(call.execute()).thenReturn(response);

            doNothing().when(responseHandler).handle(any(),any(),anyLong());

            httpPostRequester.postTask(DataUtil.generateHttpProperty());

            verify(responseHandler).handle(any(),any(),anyLong());
        } catch (SchedulerException | EntryNotFoundException e) {
            throw new JobExecutionException(e);
        }
    }

    @Test
    void testPostThrowsException() throws IOException {
        try (
                MockedStatic<HttpClient> httpClientMockedStatic = mockStatic(HttpClient.class)
        ) {
            httpClientMockedStatic.when(HttpClient::getInstance).thenReturn(httpClient);
            when(httpClient.getClient()).thenReturn(okHttpClient);
            when(okHttpClient.newCall(any())).thenReturn(call);
            when(call.execute()).thenReturn(response);

            Throwable cause = mock(Throwable.class);

            doThrow(new SchedulerException(cause)).when(responseHandler).handle(any(),any(),anyLong());
            doNothing().when(responseHandler).handleError(any(),any(),anyLong());

            assertThrows(JobExecutionException.class, () -> httpPostRequester.postTask(DataUtil.generateHttpProperty()));
        } catch (SchedulerException | EntryNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testPostWardenAuthToken() throws IOException {
        try (
                MockedStatic<HttpClient> httpClientMockedStatic = mockStatic(HttpClient.class)
        ) {
            httpClientMockedStatic.when(HttpClient::getInstance).thenReturn(httpClient);
            when(httpClient.getClient()).thenReturn(okHttpClient);
            when(okHttpClient.newCall(any())).thenReturn(call);
            when(call.execute()).thenReturn(response);

            ResponseBody responseBody = mock(ResponseBody.class);
            when(response.body()).thenReturn(responseBody);
            when(responseBody.string()).thenReturn("{\"access_token\":\"T1X-3jt11SYjIUfwGj0Rm1Y_Ym8\",\"token_type\":\"bearer\",\"refresh_token\":\"j3bQtY3kr41YBrmhOHoafJIsFRE\",\"expires_in\":7102,\"scope\":\"read write\"}");

            String token = httpPostRequester.postWardenAuthToken(DataUtil.generateHttpProperty());
            assertNotNull(token);
            assertEquals("T1X-3jt11SYjIUfwGj0Rm1Y_Ym8", token);
        }
    }

    @Test
    void testPostWardenAuthTokenThrowsException() throws IOException {
        try (
                MockedStatic<HttpClient> httpClientMockedStatic = mockStatic(HttpClient.class)
        ) {
            httpClientMockedStatic.when(HttpClient::getInstance).thenReturn(httpClient);
            when(httpClient.getClient()).thenReturn(okHttpClient);
            when(okHttpClient.newCall(any())).thenReturn(call);
            when(call.execute()).thenReturn(response);

            ResponseBody responseBody = mock(ResponseBody.class);
            when(response.body()).thenReturn(responseBody);
            when(responseBody.string()).thenReturn("");

            assertThrows(IOException.class, () -> httpPostRequester.postWardenAuthToken(DataUtil.generateHttpProperty()));
        }
    }
}