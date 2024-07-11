package com.wavemark.scheduler.schedule.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.schedule.service.quartz.JobDetailService;
import com.wavemark.scheduler.schedule.service.quartz.TriggerService;
import com.wavemark.scheduler.testing.util.DataUtil;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    private Scheduler clusteredScheduler;

    @Mock
    private TaskService taskService;

    @Mock
    private JobDetailService jobDetailService;

    @Mock
    private TriggerService triggerService;
    @Mock
    private RecordLogService recordLogService;

    @InjectMocks
    private SchedulerService schedulerService;

    @Test
    void testScheduleTask() throws SchedulerException, CronExpressionException, ParseException, EntryNotFoundException {

        when(taskService.buildTask(any())).thenReturn(DataUtil.generateTask());
        doReturn(1).when(taskService).saveTask(any());
        when(clusteredScheduler.scheduleJob(any(), any())).thenReturn(new Date());
        when(jobDetailService.buildJobDetail(any(), any())).thenReturn(DataUtil.generateJobDetail());
        when(triggerService.buildTrigger(any(), any(), any(), any())).thenReturn(DataUtil.generateTrigger());

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            schedulerService.scheduleTask(DataUtil.generateTaskInput());

            verify(taskService).saveTask(any());
            verify(clusteredScheduler).scheduleJob(any(), any());
        }
    }

    @Test
    void testScheduleTaskThrowsCronExpressionException() throws CronExpressionException, ParseException, EntryNotFoundException {
        when(taskService.buildTask(any())).thenThrow(new CronExpressionException(""));

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(CronExpressionException.class,
                    () -> schedulerService.scheduleTask(DataUtil.generateTaskInput()));
        }
    }

    @Test
    void testScheduleTaskTThrowsParseException() throws CronExpressionException, ParseException, EntryNotFoundException {
        when(taskService.buildTask(any())).thenThrow(new ParseException("", 0));

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(ParseException.class, () -> schedulerService.scheduleTask(DataUtil.generateTaskInput()));
        }
    }

    @Test
    void testScheduleTaskThrowsSchedulerException() throws SchedulerException, CronExpressionException, ParseException, EntryNotFoundException {

        when(taskService.buildTask(any())).thenReturn(DataUtil.generateTask());
        doReturn(1).when(taskService).saveTask(any());
        when(clusteredScheduler.scheduleJob(any(), any())).thenThrow(new SchedulerException());

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(SchedulerException.class, () -> schedulerService.scheduleTask(DataUtil.generateTaskInput()));
        }
    }

    @Test
    void testScheduleTaskThrowsEntryNotFoundException() throws CronExpressionException, ParseException, EntryNotFoundException {

        when(taskService.buildTask(any())).thenThrow(new EntryNotFoundException());

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(EntryNotFoundException.class, () -> schedulerService.scheduleTask(DataUtil.generateTaskInput()));
        }
    }

    @Test
    void testUpdateScheduledTask()
            throws SchedulerException, CronExpressionException, EntryNotFoundException, ParseException {

        when(taskService.findActiveTaskById(any())).thenReturn(DataUtil.generateTask());
        when(taskService.updateTask(any(), any())).thenReturn(DataUtil.generateTask());
        doReturn(1).when(taskService).saveTask(any());

        doNothing().when(clusteredScheduler).pauseJob(any());
        doNothing().when(clusteredScheduler).addJob(any(), anyBoolean());
        when(clusteredScheduler.rescheduleJob(any(), any())).thenReturn(new Date());
        when(clusteredScheduler.getTriggerState(any())).thenReturn(Trigger.TriggerState.PAUSED);

        when(jobDetailService.buildJobDetail(any(), any())).thenReturn(DataUtil.generateJobDetail());

        Trigger trigger = mock(Trigger.class);
        when(triggerService.buildTrigger(any(), any(), any(), any())).thenReturn(trigger);

        when(recordLogService.logDiffableRecordLog(any(), any(), any())).thenReturn(1);

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            schedulerService.updateScheduledTask(5, DataUtil.generateTaskInput());

            verify(taskService).saveTask(any());
            verify(clusteredScheduler).rescheduleJob(any(), any());
        }
    }

    @Test
    void testUpdateScheduledTaskThrowsCronExpressionException() throws CronExpressionException, ParseException {

        when(taskService.updateTask(any(), any())).thenThrow(new CronExpressionException(""));

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(CronExpressionException.class,
                    () -> schedulerService.updateScheduledTask(5, DataUtil.generateTaskInput()));
        }
    }

    @Test
    void testUpdateScheduledTaskThrowsSchedulerException() throws SchedulerException, CronExpressionException, EntryNotFoundException, ParseException {
        when(taskService.findActiveTaskById(any())).thenReturn(DataUtil.generateTask());
        when(taskService.updateTask(any(), any())).thenReturn(DataUtil.generateTask());
        doReturn(1).when(taskService).saveTask(any());
        doReturn(1).when(recordLogService).logDiffableRecordLog(any(), any(), any());

        Trigger trigger = mock(Trigger.class);
        when(triggerService.buildTrigger(any(), any(), any(), any())).thenReturn(trigger);

        when(clusteredScheduler.rescheduleJob(any(), any())).thenThrow(new SchedulerException());

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(SchedulerException.class, () -> schedulerService.updateScheduledTask(5, DataUtil.generateTaskInput()));
        }
    }

    @Test
    void testDeleteTask() throws SchedulerException, EntryNotFoundException {

        when(taskService.findActiveTaskById(any())).thenReturn(DataUtil.generateTask());
        when(taskService.saveTask(any())).thenReturn(null);
        doReturn(1).when(recordLogService).logDiffableRecordLog(any(), any(), any());

        when(clusteredScheduler.deleteJob(any())).thenReturn(true);

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            schedulerService.deleteTask(5);

            verify(taskService).saveTask(any());
            verify(clusteredScheduler).deleteJob(any());
        }
    }

    @Test
    void testDeleteTaskFailed() throws SchedulerException, EntryNotFoundException {

        when(taskService.findActiveTaskById(any())).thenReturn(DataUtil.generateTask());

        when(clusteredScheduler.deleteJob(any())).thenReturn(false);

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            schedulerService.deleteTask(5);

            verifyNoMoreInteractions(taskService);
            verify(clusteredScheduler).deleteJob(any());
        }
    }

    @Test
    void testDeleteTaskThrowsEntryNotFoundException() throws EntryNotFoundException {

        when(taskService.findActiveTaskById(any())).thenThrow(new EntryNotFoundException());

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(EntryNotFoundException.class, () -> schedulerService.deleteTask(5));
        }
    }

    @Test
    void testDeleteTasks() throws SchedulerException, EntryNotFoundException {

        when(taskService.findTaskById(any())).thenReturn(DataUtil.generateTask());
        doNothing().when(taskService).deleteTask(any());

        when(clusteredScheduler.deleteJob(any())).thenReturn(true);

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            schedulerService.deleteTasks(Collections.singletonList(5));
        }
    }

    @Test
    void testDeleteTasksFailed() throws SchedulerException, EntryNotFoundException {

        when(taskService.findTaskById(any())).thenReturn(DataUtil.generateTask());
        doNothing().when(taskService).deleteTask(any());

        when(clusteredScheduler.deleteJob(any())).thenReturn(false);

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            schedulerService.deleteTasks(Collections.singletonList(5));
        }
    }

    @Test
    void testDeleteTasksThrowsEntryNotFoundException() throws EntryNotFoundException {

        when(taskService.findTaskById(any())).thenThrow(new EntryNotFoundException());

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(EntryNotFoundException.class, () -> schedulerService.deleteTasks(Collections.singletonList(5)));
        }
    }

}