package com.wavemark.scheduler.schedule.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.ZoneId;

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

import com.cardinalhealth.service.support.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

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

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentDepartmentTimeZone).thenReturn(ZoneId.systemDefault());

            TaskResponse result = taskResponseService.buildTaskResponse(task);

            assertNotNull(result);
        }
    }

}