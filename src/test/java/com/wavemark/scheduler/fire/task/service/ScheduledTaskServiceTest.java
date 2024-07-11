package com.wavemark.scheduler.fire.task.service;

import com.wavemark.scheduler.fire.http.client.HttpClient;
import com.wavemark.scheduler.fire.http.property.HttpProperty;
import com.wavemark.scheduler.fire.http.response.ResponseHandler;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskTypeRepository;
import com.wavemark.scheduler.testing.util.DataUtil;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledTaskTest {

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

    @Mock
    TaskTypeRepository taskTypeRepository;

    @InjectMocks
    ScheduledTaskService scheduledTaskService;

    @Test
    void testPostTask() throws IOException, JobExecutionException {
        try (
                MockedStatic<HttpClient> httpClientMockedStatic = mockStatic(HttpClient.class)
        ) {
            httpClientMockedStatic.when(HttpClient::getInstance).thenReturn(httpClient);
            when(httpClient.getClient()).thenReturn(okHttpClient);
            when(okHttpClient.newCall(any())).thenReturn(call);
            when(call.execute()).thenReturn(response);

            doNothing().when(responseHandler).handle(any(), any(), anyLong());

            scheduledTaskService.postTask(DataUtil.generateHttpProperty());

            verify(responseHandler).handle(any(), any(), anyLong());
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

            doThrow(new SchedulerException(cause)).when(responseHandler).handle(any(), any(), anyLong());
            doNothing().when(responseHandler).handleError(any(), any(), anyLong());

            assertThrows(JobExecutionException.class, () -> scheduledTaskService.postTask(DataUtil.generateHttpProperty()));
        } catch (SchedulerException | EntryNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFetchTaskRequestProperty() throws EntryNotFoundException {
        when(taskTypeRepository.findByTaskType(any())).thenReturn(DataUtil.generateTaskType());
        ReflectionTestUtils.setField(scheduledTaskService, "host", "https://ldec0609wmapp09.cardinalhealth.net");

        HttpProperty httpProperty = scheduledTaskService
                .fetchTaskRequestProperty(DataUtil.generateJobDataMap(), "authToken");

        assertNotNull(httpProperty);
        assertEquals("taskName", httpProperty.getTaskName());
        assertEquals("{ testBodyParam: test }", httpProperty.getBodyParam());
        assertEquals("https://ldec0609wmapp09.cardinalhealth.net/test?access_token=authToken",
                httpProperty.getUrl());
        assertEquals("endpointName", httpProperty.getEndpointName());
    }

}