package com.wavemark.scheduler.schedule.dto.response.builder;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class TaskResponseBuilderTest {

    @Test
    void testBuildTaskResponse() {
        Task task = DataUtil.generateTask();

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

            TaskResponse result = TaskResponseBuilder.buildTaskResponse(task, "Auto-Order", new TaskRunLog(), new CronDescription(DataUtil.generateTaskFrequencyInput()), "frequencyDescription");

            assertNotNull(result);
        }
    }

}