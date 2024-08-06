//package com.wavemark.scheduler.fire.task;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.io.IOException;
//
//import com.wavemark.scheduler.fire.authentication.service.OAuth2Service;
//import com.wavemark.scheduler.fire.task.service.ScheduledTaskService;
//import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
//import com.wavemark.scheduler.testing.util.DataUtil;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.SchedulerException;
//
//@ExtendWith(MockitoExtension.class)
//class ScheduledTaskTest {
//
//    @Mock
//    OAuth2Service oAuth2Service;
//    @Mock
//    ScheduledTaskService scheduledTaskService;
//    @Mock
//    JobExecutionContext context;
//
//    @InjectMocks
//    ScheduledTask scheduledTask;
//
//    @Test
//    void testExecute() throws SchedulerException, EntryNotFoundException, IOException {
//
//        when(context.getMergedJobDataMap()).thenReturn(DataUtil.generateJobDataMap());
//        when(oAuth2Service.getWardenAuthToken(any())).thenReturn("T1X-3jt11SYjIUfwGj0Rm1Y_Ym8");
//
//        when(scheduledTaskService.fetchTaskRequestProperty(any(), any())).thenReturn(DataUtil.generateHttpProperty());
//        doNothing().when(scheduledTaskService).postTask(any());
//
//        scheduledTask.execute(context);
//        verify(scheduledTaskService).postTask(any());
//    }
//
//    @Test
//    void testExecuteThrowsException() throws SchedulerException, EntryNotFoundException, IOException {
//
//        when(context.getMergedJobDataMap()).thenReturn(DataUtil.generateJobDataMap());
//        when(oAuth2Service.getWardenAuthToken(any())).thenReturn("T1X-3jt11SYjIUfwGj0Rm1Y_Ym8");
//        when(scheduledTaskService.fetchTaskRequestProperty(any(), any())).thenReturn(DataUtil.generateHttpProperty());
//        doThrow(new JobExecutionException()).when(scheduledTaskService).postTask(any());
//
//        assertThrows(JobExecutionException.class, () -> scheduledTask.execute(context));
//    }
//
//    @Test
//    void testFireTask() throws SchedulerException, EntryNotFoundException, IOException {
//
//        when(oAuth2Service.getWardenAuthToken(any())).thenReturn("T1X-3jt11SYjIUfwGj0Rm1Y_Ym8");
//        when(scheduledTaskService.fetchTaskRequestProperty(any(), any())).thenReturn(DataUtil.generateHttpProperty());
//        doNothing().when(scheduledTaskService).postTask(any());
//
//        scheduledTask.fireTask("Auto-Order_endpointId", "params");
//        verify(scheduledTaskService).postTask(any());
//    }
//
//    @Test
//    void testFireTaskThrowsException() throws SchedulerException, EntryNotFoundException, IOException {
//
//        when(oAuth2Service.getWardenAuthToken(any())).thenReturn("T1X-3jt11SYjIUfwGj0Rm1Y_Ym8");
//        when(scheduledTaskService.fetchTaskRequestProperty(any(), any())).thenReturn(DataUtil.generateHttpProperty());
//        doThrow(new JobExecutionException()).when(scheduledTaskService).postTask(any());
//
//        assertThrows(JobExecutionException.class, () -> scheduledTask.fireTask("Auto-Order_endpointId", "params"));
//    }
//
//}