package com.wavemark.scheduler.schedule.service.quartz;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.wavemark.scheduler.schedule.service.quartz.TriggerService;
import com.wavemark.scheduler.testing.util.DataUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

@ExtendWith(MockitoExtension.class)
class TriggerServiceTest {

    @Mock
    private Scheduler clusteredScheduler;

    @InjectMocks
	TriggerService triggerService;

    @Test
    void testBuildTrigger() {

        Trigger result = triggerService
                .buildTrigger(DataUtil.generateTaskInput(), DataUtil.generateJobDetail(),
                        "testName", "0 0/1 * * * ?");

        assertNotNull(result);
    }

    @Test
    void testGetTrigger() throws SchedulerException {

        doReturn(DataUtil.generateTriggers()).when(clusteredScheduler).getTriggersOfJob(any());

        Trigger result = triggerService.getTrigger("test");

        assertNotNull(result);
    }

}