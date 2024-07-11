package com.wavemark.scheduler.fire.listener;

import static com.wavemark.scheduler.fire.retry.RetryProperty.TRIGGER_STATUS_KEY;

import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wavemark.scheduler.fire.constant.TriggerState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

@ExtendWith(MockitoExtension.class)
class JobDataHandlerTest {

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
    JobBuilder jobBuilder;

    @InjectMocks
    JobDataHandler jobDataHandler;

    @BeforeEach
    void setUp() {
        when(context.getJobDetail()).thenReturn(jobDetail);
        when(jobDetail.getJobDataMap()).thenReturn(jobDataMap);
    }

    @Test
    void testResetJobData_isARetry() {
        when(jobDataMap.getString(TRIGGER_STATUS_KEY)).thenReturn(TriggerState.RETRY.toString());
        when(context.getTrigger()).thenReturn(trigger);
        when(trigger.getKey()).thenReturn(triggerKey);
        when(triggerKey.getName()).thenReturn("testName");
        when(jobDetail.getJobBuilder()).thenReturn(jobBuilder);
        when(jobBuilder.storeDurably(anyBoolean())).thenReturn(jobBuilder);

        jobDataHandler.resetJobData();

        verify(context, times(2)).getJobDetail();
        verify(context).getTrigger();
        verify(context).getScheduler();
    }

    @Test
    void testResetJobData_isNotARetry() {
        when(jobDataMap.getString(TRIGGER_STATUS_KEY)).thenReturn(TriggerState.SUCCESS.toString());

        jobDataHandler.resetJobData();

        verify(context).getJobDetail();
    }

    @Test
    void testResetJobDataThrowsException() {
        when(jobDataMap.getString(TRIGGER_STATUS_KEY)).thenReturn(TriggerState.RETRY.toString());
        when(context.getTrigger()).thenThrow(new RuntimeException());

        jobDataHandler.resetJobData();

        verify(context, never()).getScheduler();
    }

}