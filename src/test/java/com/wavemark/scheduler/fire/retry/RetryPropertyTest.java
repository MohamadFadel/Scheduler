package com.wavemark.scheduler.fire.retry;

import static com.wavemark.scheduler.fire.retry.RetryProperty.RETRY_CLUSTERED_JOBS_GROUP;
import static com.wavemark.scheduler.fire.retry.RetryProperty.RETRY_NUMBER_KEY;
import static com.wavemark.scheduler.fire.retry.RetryProperty.TRIGGER_STATUS_KEY;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import com.wavemark.scheduler.fire.constant.TriggerState;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

@ExtendWith(MockitoExtension.class)
class RetryPropertyTest {

    @Mock
    JobExecutionContext context;

    @Mock
    JobDetail jobDetail;

    @Mock
    JobDataMap jobDataMap;

    @Mock
    Trigger trigger;

    @Mock
    TriggerKey triggerKey;

    @Mock
    Scheduler scheduler;

    @InjectMocks
    RetryProperty retryProperty;

    @Test
    void testGetRetryCount_isRetry() {
        when(context.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDataMap.getString(TRIGGER_STATUS_KEY)).thenReturn(TriggerState.RETRY.toString());
        when(jobDataMap.getIntValue(RETRY_NUMBER_KEY)).thenReturn(1);

        int result = retryProperty.getRetryCount();

        assertEquals(1, result);
    }

    @Test
    void testGetRetryCount_isNotRetry() {
        when(context.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
        when(jobDataMap.getString(TRIGGER_STATUS_KEY)).thenReturn(TriggerState.SUCCESS.toString());

        int result = retryProperty.getRetryCount();

        assertEquals(0, result);
    }

    @Test
    void testNextFireTime_isRetryTrigger() throws SchedulerException {
        Date currentDate = new Date();
        when(context.getTrigger()).thenReturn(trigger);
        when(trigger.getKey()).thenReturn(triggerKey);
        when(triggerKey.getGroup()).thenReturn(RETRY_CLUSTERED_JOBS_GROUP);
        when(triggerKey.getName()).thenReturn("testName");
        when(context.getScheduler()).thenReturn(scheduler);
        when(scheduler.getTrigger(any())).thenReturn(trigger);
        when(trigger.getNextFireTime()).thenReturn(currentDate);

        Date result = retryProperty.nextFireTime();

        verify(triggerKey).getGroup();
        assertEquals(currentDate, result);
    }

    @Test
    void testNextFireTime_isRetryTriggerAndNull() throws SchedulerException {
        when(context.getTrigger()).thenReturn(trigger);
        when(trigger.getKey()).thenReturn(triggerKey);
        when(triggerKey.getGroup()).thenReturn(RETRY_CLUSTERED_JOBS_GROUP);
        when(triggerKey.getName()).thenReturn("testName");
        when(context.getScheduler()).thenReturn(scheduler);
        when(scheduler.getTrigger(any())).thenReturn(null);

        Date result = retryProperty.nextFireTime();

        verify(triggerKey).getGroup();
        assertNull(result);
    }

    @Test
    void testNextFireTimeThrowsException_isRetryTrigger() throws SchedulerException {
        when(context.getTrigger()).thenReturn(trigger);
        when(trigger.getKey()).thenReturn(triggerKey);
        when(triggerKey.getGroup()).thenReturn(RETRY_CLUSTERED_JOBS_GROUP);
        when(triggerKey.getName()).thenReturn("testName");
        when(context.getScheduler()).thenReturn(scheduler);
        when(scheduler.getTrigger(any())).thenThrow(new SchedulerException());

        Date result = retryProperty.nextFireTime();

        verify(triggerKey).getGroup();
        assertNull(result);
    }

    @Test
    void testNextFireTime_isNotRetryTrigger() {
        Date currentDate = new Date();
        when(context.getTrigger()).thenReturn(trigger);
        when(trigger.getKey()).thenReturn(triggerKey);
        when(triggerKey.getGroup()).thenReturn("testGroup");
        when(context.getNextFireTime()).thenReturn(currentDate);

        Date result = retryProperty.nextFireTime();

        verify(triggerKey).getGroup();
        assertEquals(currentDate, result);
    }

}