package com.wavemark.scheduler.fire.listener;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class TriggerHandlingListenerTest {

    @Mock
    private Logger log;

    @InjectMocks
    private TriggerHandlingListener triggerHandlingListener;

    @Test
    void getName() {
        String name = triggerHandlingListener.getName();

        assertEquals("GlobalTriggerListener", name);
    }

    @Test
    void triggerFired() {
        Trigger trigger = Mockito.mock(Trigger.class);
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Mockito.doReturn(keyMock).when(trigger).getKey();

        triggerHandlingListener.triggerFired(trigger, null);

        verify(log).info("[FIRED] trigger fired for trigger name: " + keyMock);
        verifyNoMoreInteractions(log);
    }

    @Test
    void vetoJobExecution() {
        Trigger trigger = Mockito.mock(Trigger.class);
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Mockito.doReturn(keyMock).when(trigger).getKey();

        triggerHandlingListener.vetoJobExecution(trigger, null);

        verify(log).info("[VETOED] Veto Job Execution trigger for trigger name: " + keyMock);
        verifyNoMoreInteractions(log);
    }

    @Test
    void triggerMisfired() {
        Trigger trigger = Mockito.mock(Trigger.class);
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Date dateMock = Mockito.mock(Date.class);
        Mockito.doReturn(keyMock).when(trigger).getKey();
        Mockito.doReturn(dateMock).when(trigger).getStartTime();

        triggerHandlingListener.triggerMisfired(trigger);

        verify(log).info("[MISFIRED] " + "GlobalTriggerListener" + " trigger: " + keyMock + " misfired at " + dateMock);
        verifyNoMoreInteractions(log);
    }

    @Test
    void triggerComplete() {
        Trigger trigger = Mockito.mock(Trigger.class);
        TriggerKey keyMock = Mockito.mock(TriggerKey.class);
        Date dateMock = Mockito.mock(Date.class);
        Mockito.doReturn(keyMock).when(trigger).getKey();
        Mockito.doReturn(dateMock).when(trigger).getStartTime();

        triggerHandlingListener.triggerComplete(trigger, null, null);

        verify(log).info("[COMPLETED] " + "GlobalTriggerListener" + " trigger: " + keyMock + " completed at " + dateMock);
        verifyNoMoreInteractions(log);
    }

}