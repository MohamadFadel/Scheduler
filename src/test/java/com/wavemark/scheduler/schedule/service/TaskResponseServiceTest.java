package com.wavemark.scheduler.schedule.service;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;
import com.wavemark.scheduler.schedule.service.web.TaskResponseService;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskResponseServiceTest {

    @Mock
    private TaskTypeService taskTypeService;
    @Mock
    private TaskRunLogService taskRunLogService;
    @Mock
    private CronExpressionService cronExpressionService;

    @InjectMocks
    private TaskResponseService taskResponseService;

    @Test
    void testBuildTaskResponse() throws CronExpressionException, EntryNotFoundException {
        Task task = DataUtil.generateTask();
        when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");
        when(taskRunLogService.getLastRun(any())).thenReturn(new TaskRunLog());
        when(cronExpressionService.reverseCronExpression(any(), any()))
                .thenReturn(new CronDescription(DataUtil.generateTaskFrequencyInput()));

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

            TaskResponse result = taskResponseService.buildTaskResponse(task);

            assertNotNull(result);
        }
    }

}