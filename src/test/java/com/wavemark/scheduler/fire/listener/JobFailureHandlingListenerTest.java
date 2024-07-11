package com.wavemark.scheduler.fire.listener;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.wavemark.scheduler.testing.util.DataUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

@ExtendWith(MockitoExtension.class)
class JobHandlingListenerTest {

    @Mock
    JobExecutionContext context;

    @Mock
    Trigger trigger;

    @Mock
    JobDetail jobDetail;

    @Mock
    TriggerKey triggerKey;

    @Mock
    JobKey jobKey;

    @Mock
    Scheduler scheduler;

    JobHandlingListener jobHandlingListener = new JobHandlingListener();

    @Test
    void testGetName() {
        assertNotNull(jobHandlingListener.getName());
    }

    @Test
    void testJobToBeExecuted() {
        when(context.getTrigger()).thenReturn(trigger);
        when(context.getJobDetail()).thenReturn(jobDetail);
        when(trigger.getKey()).thenReturn(triggerKey);
        when(jobDetail.getKey()).thenReturn(jobKey);
        when(triggerKey.getName()).thenReturn("testName");
        when(jobKey.getName()).thenReturn("testName");

        jobHandlingListener.jobToBeExecuted(context);

        verify(triggerKey).getName();
        verify(jobKey).getName();
    }

    @Test
    void testJobExecutionVetoed() {
        jobHandlingListener.jobExecutionVetoed(context);

        verifyNoInteractions(context);
    }

    @Test
    void testJobWasExecuted_withoutException() {
        assertDoesNotThrow(() -> jobHandlingListener.jobWasExecuted(context, null));
    }

    @Test
    void testJobWasExecuted_withException() {
        when(context.getTrigger()).thenReturn(trigger);
        when(trigger.getKey()).thenReturn(triggerKey);
        when(triggerKey.getGroup()).thenReturn("testGroup");
        when(context.getJobDetail()).thenReturn(DataUtil.generateJobDetail());
        when(context.getScheduler()).thenReturn(scheduler);

        assertDoesNotThrow(() -> jobHandlingListener.jobWasExecuted(context, new JobExecutionException()));
    }

}