package com.wavemark.scheduler.fire.http.response;

import com.wavemark.scheduler.fire.http.response.email.ResponseEmailService;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.ReportInstanceService;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.testing.util.DataUtil;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResponseHandlerTest {

    @Mock
    private Response response;

    @Mock
    private TaskService taskService;

    @Mock
    private ResponseLogService responseLogService;

    @Mock
    private ResponseEmailService responseEmailService;

    @Mock
    private ReportInstanceService reportInstanceService;

    @InjectMocks
    private ResponseHandler responseHandler;

    @Test
    void testHandle_status200() throws SchedulerException, EntryNotFoundException {
        when(taskService.findActiveTaskByTaskName(any())).thenReturn(new Task());
        doNothing().when(responseLogService).logResponse(any(), any(), anyLong());
        doNothing().when(responseEmailService).sendEmailMessage(any(), any(), any());

        try (MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class)) {
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any())).thenReturn(HttpResponse.builder().success(true).build());

            assertDoesNotThrow(() -> responseHandler.handle(DataUtil.generateHttpProperty(), response, 76L));
        }
    }
    @Test
    void testHandleJob_status200() throws SchedulerException, EntryNotFoundException {

        when(reportInstanceService.findReportById(any())).thenReturn(new ReportInstanceConfig());
        doNothing().when(responseLogService).logResponse(any(), any(), anyLong());
        doNothing().when(responseEmailService).sendEmailMessage(any(), any(), any());

        try (MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class)) {
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any())).thenReturn(HttpResponse.builder().success(true).build());

            assertDoesNotThrow(() -> responseHandler.handleJob(DataUtil.generateOldHttpProperty(), response, 76L));
        }
    }

    @Test
    void testHandle_status412() throws SchedulerException, EntryNotFoundException {

        when(taskService.findActiveTaskByTaskName(any())).thenReturn(new Task());
        doNothing().when(responseLogService).logResponse(any(), any(), anyLong());
        doNothing().when(responseEmailService).sendEmailMessage(any(), any(), any());

        try (MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class)) {
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any()))
                    .thenReturn(HttpResponse.builder().success(false).code(412).build());

            assertDoesNotThrow(() -> responseHandler.handle(DataUtil.generateHttpProperty(), response, 76L));
        }
    }

    @Test
    void testHandleJob_status412() throws SchedulerException, EntryNotFoundException {

        when(reportInstanceService.findReportById(any())).thenReturn(new ReportInstanceConfig());
        doNothing().when(responseLogService).logResponse(any(), any(), anyLong());
        doNothing().when(responseEmailService).sendEmailMessage(any(), any(), any());

        try (MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class)) {
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any()))
                    .thenReturn(HttpResponse.builder().success(false).code(412).build());

            assertDoesNotThrow(() -> responseHandler.handleJob(DataUtil.generateOldHttpProperty(), response, 76L));
        }
    }

    @Test
    void testHandle_status500() throws SchedulerException, EntryNotFoundException {

        when(taskService.findActiveTaskByTaskName(any())).thenReturn(new Task());
        doNothing().when(responseLogService).logResponse(any(), any(),anyLong());
        doNothing().when(responseEmailService).sendEmailMessage(any(), any(), any());

        HttpResponse httpResponse = HttpResponse.builder().success(false).code(500).build();

        try (MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class)) {
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any())).thenReturn(httpResponse);

            assertThrows(JobExecutionException.class, () -> responseHandler.handle(DataUtil.generateHttpProperty(), response, 54L));
        }
    }

    @Test
    void testHandleJob_status500() throws SchedulerException, EntryNotFoundException {

        when(reportInstanceService.findReportById(any())).thenReturn(new ReportInstanceConfig());
        doNothing().when(responseLogService).logResponse(any(), any(),anyLong());
        doNothing().when(responseEmailService).sendEmailMessage(any(), any(), any());

        HttpResponse httpResponse = HttpResponse.builder().success(false).code(500).build();

        try (MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class)) {
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any())).thenReturn(httpResponse);

            assertThrows(JobExecutionException.class, () -> responseHandler.handleJob(DataUtil.generateOldHttpProperty(), response, 54L));
        }
    }

    @Test
    void testHandleError() throws SchedulerException, EntryNotFoundException {

        when(taskService.findActiveTaskByTaskName(any())).thenReturn(new Task());
        doNothing().when(responseLogService).logResponseError(any(), any(), anyLong());
        doNothing().when(responseEmailService).sendEmailMessage(any(), any(), any());

        HttpResponse httpResponse = HttpResponse.builder().success(false).code(500).build();

        try (MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class)) {
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any(), any())).thenReturn(httpResponse);

            assertDoesNotThrow(() -> responseHandler.handleError(DataUtil.generateHttpProperty(), new Exception(), 54L));
        }
    }

    @Test
    void testHandleJobError() throws SchedulerException, EntryNotFoundException {

        when(reportInstanceService.findReportById(any())).thenReturn(new ReportInstanceConfig());
        doNothing().when(responseLogService).logResponseError(any(), any(), anyLong());
        doNothing().when(responseEmailService).sendEmailMessage(any(), any(), any());

        HttpResponse httpResponse = HttpResponse.builder().success(false).code(500).build();

        try (MockedStatic<ResponseFactory> responseFactoryMockedStatic = mockStatic(ResponseFactory.class)) {
            responseFactoryMockedStatic.when(() -> ResponseFactory.initializeHttpResponse(any(), any())).thenReturn(httpResponse);

            assertDoesNotThrow(() -> responseHandler.handleJobError(DataUtil.generateOldHttpProperty(), new Exception(), 54L));
        }
    }



}