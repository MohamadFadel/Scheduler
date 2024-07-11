package com.wavemark.scheduler.fire.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;
import org.slf4j.Logger;

import java.util.Date;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SchedulerHandlingListenerTest {

    @Mock
    private Logger log;

    @InjectMocks
    private SchedulerHandlingListener schedulerHandlingListener;

    @Test
    void jobScheduled() {
        Trigger trigger = Mockito.mock(Trigger.class);
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Mockito.doReturn(keyMock).when(trigger).getKey();
        Mockito.doReturn("1").when(keyMock).getName();

        schedulerHandlingListener.jobScheduled(trigger);

        verify(log).info("[SCHEDULED] Schedule job for trigger name: 1");
    }

    @Test
    void jobUnscheduled() {
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Mockito.doReturn("1").when(keyMock).getName();

        schedulerHandlingListener.jobUnscheduled(keyMock);

        verify(log).info("[UNSCHEDULED] Unscheduled job for trigger name: 1");
    }

    @Test
    void triggerFinalized() {
        Trigger trigger = Mockito.mock(Trigger.class);
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Mockito.doReturn(keyMock).when(trigger).getKey();
        Mockito.doReturn("1").when(keyMock).getName();

        schedulerHandlingListener.triggerFinalized(trigger);

        verify(log).info("[FINISHED] Finalize trigger for trigger name: 1");
    }

    @Test
    void triggerPaused() {
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Mockito.doReturn("1").when(keyMock).getName();

        schedulerHandlingListener.triggerPaused(keyMock);

        verify(log).info("[PAUSED] Pause trigger for trigger name: 1");
    }

    @Test
    void triggersPaused() {
        schedulerHandlingListener.triggersPaused("5");

        verify(log).info("[PAUSED] Pause triggers for trigger Group: 5");
    }

    @Test
    void triggerResumed() {
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Mockito.doReturn("1").when(keyMock).getName();

        schedulerHandlingListener.triggerResumed(keyMock);

        verify(log).info("[RESUMED] Resume trigger for trigger name: 1");
    }

    @Test
    void triggersResumed() {
        schedulerHandlingListener.triggersResumed("5");

        verify(log).info("[RESUMED] Resume triggers for trigger Group: 5");
    }

    @Test
    void jobAdded() {
        JobDetail jobDetailMock = Mockito.mock(JobDetail.class);
        JobKey jobKeyMock = Mockito.mock(JobKey.class);
        Mockito.doReturn(jobKeyMock).when(jobDetailMock).getKey();
        Mockito.doReturn("1").when(jobKeyMock).getName();

        schedulerHandlingListener.jobAdded(jobDetailMock);

        verify(log).info("[ADDED] Add job for job name: 1");
    }

    @Test
    void jobDeleted() {
        JobKey jobKeyMock = Mockito.mock(JobKey.class);
        Mockito.doReturn("1").when(jobKeyMock).getName();

        schedulerHandlingListener.jobDeleted(jobKeyMock);

        verify(log).info("[DELETED] Delete job for job name: 1");
    }

    @Test
    void jobPaused() {
        JobKey jobKeyMock = Mockito.mock(JobKey.class);
        Mockito.doReturn("1").when(jobKeyMock).getName();

        schedulerHandlingListener.jobPaused(jobKeyMock);

        verify(log).info("[PAUSED] Pause job for job name: 1");
    }

    @Test
    void jobsPaused() {
        schedulerHandlingListener.jobsPaused("5");

        verify(log).info("[PAUSED] Pause jobs for job Group: 5");
    }

    @Test
    void jobResumed() {
        JobKey jobKeyMock = Mockito.mock(JobKey.class);
        Mockito.doReturn("1").when(jobKeyMock).getName();

        schedulerHandlingListener.jobResumed(jobKeyMock);

        verify(log).info("[RESUMED] Resume job for job name: 1");
    }

    @Test
    void jobsResumed() {
        schedulerHandlingListener.jobsResumed("5");

        verify(log).info("[RESUMED] Resume jobs for job Group: 5");
    }

    @Test
    void schedulerError() {
        SchedulerException schedulerExceptionMock = Mockito.mock(SchedulerException.class);
        Throwable cause = Mockito.mock(Throwable.class);
        Mockito.doReturn(cause).when(schedulerExceptionMock).getCause();

        schedulerHandlingListener.schedulerError("msg", schedulerExceptionMock);

        verify(log).error("[ERROR] Scheduler Error occurred with cause = '{}' and exception = '{}'",
                cause, "msg");
    }

    @Test
    void schedulerError_Null() {
        schedulerHandlingListener.schedulerError("msg", null);

        verify(log).error("[ERROR] Scheduler Error occurred with cause = '{}' and exception = '{}'", "NULL", "msg");
    }

    @Test
    void schedulerInStandbyMode() {
        Date dateMock = new Date();

        schedulerHandlingListener.schedulerInStandbyMode();

        verify(log).info("[STANDBY] Scheduler is in Standby Mode at: " + dateMock);
    }

    @Test
    void schedulerStarted() {
        Date dateMock = new Date();

        schedulerHandlingListener.schedulerStarted();

        verify(log).info("[STARTED] Scheduler Started at: " + dateMock);
    }

    @Test
    void schedulerStarting() {
        Date dateMock = new Date();

        schedulerHandlingListener.schedulerStarting();

        verify(log).info("[STARTING] Scheduler is starting now at: " + dateMock);
    }

    @Test
    void schedulerShutdown() {
        Date dateMock = new Date();

        schedulerHandlingListener.schedulerShutdown();

        verify(log).info("[SHUTDOWN] Scheduler Shutdown at: " + dateMock);
    }

    @Test
    void schedulerShuttingDown() {
        Date dateMock = new Date();

        schedulerHandlingListener.schedulerShuttingdown();

        verify(log).info("[SHUTTING DOWN] Scheduler is Shutting down now at: " + dateMock);
    }

    @Test
    void schedulingDataCleared() {
        Date dateMock = new Date();

        schedulerHandlingListener.schedulingDataCleared();

        verify(log).info("[DATA CLEARED] Scheduler Data Cleared at: " + dateMock);
    }

}