package com.wavemark.scheduler.schedule.service.core;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.domain.projection.TaskRunLogRP;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskRunLogRepository;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

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
    void testGetLastRun() throws EntryNotFoundException {
        when(taskRunLogRepository.findById(any())).thenReturn(Optional.of(new TaskRunLog()));

        TaskRunLog taskRunLog = taskRunLogService.getLastRun(1);

        assertNotNull(taskRunLog);
    }

    @Test
    void testGetSchdTaskRunLog() {

        Mockito.when(taskRunLogRepository.getTaskRunLogReport(any())).thenReturn(new ArrayList<>());

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
            List<TaskRunLogRP> taskRunLogRPList = taskRunLogService.getTaskRunLogReport();

            assertNotNull(taskRunLogRPList);
        }
    }

    @Test
    void testGetLastRunThrowsEntryNotFoundException() {
        when(taskRunLogRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> taskRunLogService.getLastRun(5));
    }

    @Test
    void testBuildTaskLog() throws SchedulerException {
        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
        when(scheduler.getSchedulerInstanceId()).thenReturn("testInstance");

        TaskRunLog taskRunLog = taskRunLogService.buildTaskRunLog(DataUtil.generateTask(), "responseMessage", 200, 54L);

        assertNotNull(taskRunLog);
    }

    @Test
    void testBuildTaskLogResponseMessageGreaterThan4000() throws SchedulerException {
        String responseMessage = RandomStringUtils.random(4005);

        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);
        when(scheduler.getSchedulerInstanceId()).thenReturn("testInstance");

        TaskRunLog taskRunLog = taskRunLogService.buildTaskRunLog(DataUtil.generateTask(), responseMessage, 200, 65L);

        assertNotNull(taskRunLog);
    }

    @Test
    void testSaveLog() {
        Mockito.doReturn(DataUtil.generateTaskRunLog()).when(taskRunLogRepository).save(any());
        Integer taskRunLogId = taskRunLogService.saveTaskRunLog(new TaskRunLog());

        assertNotNull(taskRunLogId);
        assertEquals(2, taskRunLogId);
    }

}