package com.wavemark.scheduler.schedule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskRunLogRepository;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import com.wavemark.scheduler.testing.util.DataUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@ExtendWith(MockitoExtension.class)
class TaskRunLogServiceTest {

    @Mock
    private Scheduler scheduler;

    @Mock
    private SchedulerFactoryBean schedulerFactoryBean;

    @Mock
    private TaskRunLogRepository taskRunLogRepository;

    @InjectMocks
    private TaskRunLogService taskRunLogService;

    @Test
    void getLastRun() throws EntryNotFoundException {
        when(taskRunLogRepository.findById(any())).thenReturn(Optional.of(new TaskRunLog()));

        TaskRunLog taskRunLog = taskRunLogService.getLastRun(1);

        assertNotNull(taskRunLog);
    }

    @Test
    void getLastRunThrowsEntryNotFoundException() {
        when(taskRunLogRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> taskRunLogService.getLastRun(5));
    }

    @Test
    void buildTaskLog() throws SchedulerException {
        Trigger trigger = Mockito.spy(Trigger.class);
        when(trigger.getPreviousFireTime()).thenReturn(new Date());
        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
        when(scheduler.getSchedulerInstanceId()).thenReturn("testInstance");

        TaskRunLog taskRunLog = taskRunLogService.buildTaskRunLog(DataUtil.generateTask(), trigger,
                "responseMessage", 200, 54L);

        assertNotNull(taskRunLog);
    }

    @Test
    void buildTaskLogResponseMessageGreaterThan4000() throws SchedulerException {
        String responseMessage = RandomStringUtils.random(4005);

        Trigger trigger = Mockito.spy(Trigger.class);
        when(trigger.getPreviousFireTime()).thenReturn(new Date());
        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
        when(scheduler.getSchedulerInstanceId()).thenReturn("testInstance");

        TaskRunLog taskRunLog = taskRunLogService.buildTaskRunLog(DataUtil.generateTask(), trigger,
                responseMessage, 200, 65L);

        assertNotNull(taskRunLog);
    }

    @Test
    void saveLog() {
        Mockito.doReturn(DataUtil.generateTaskRunLog()).when(taskRunLogRepository).save(any());
        Integer taskRunLogId = taskRunLogService.saveTaskRunLog(new TaskRunLog());

        assertNotNull(taskRunLogId);
        assertEquals(2, taskRunLogId);
    }

}