package com.wavemark.scheduler.schedule.dto.response.builder;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.schedule.domain.projection.TaskRunLogRP;
import com.wavemark.scheduler.schedule.dto.response.TaskRunLogResponse;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class TaskRunLogResponseBuilderTest {

    private static MockedStatic<SecurityUtilsV2> securityUtils;

    @BeforeAll
    static void beforeAll() {
        securityUtils = Mockito.mockStatic(SecurityUtilsV2.class);
    }

    @AfterAll
    static void afterAll() {
        securityUtils.close();
    }

    @Test
    void buildBuildTaskRunLogResponse() {
        securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

        TaskRunLogRP taskRunLogRP = DataUtil.generateSchdTaskRunLog();
        TaskRunLogResponse taskRunLogResponse = TaskRunLogResponseBuilder.buildTaskRunLogResponse(taskRunLogRP, String.valueOf(Frequency.MONTHLY));

        assertNotNull(taskRunLogResponse);
        assertEquals(taskRunLogRP.getTaskId(), taskRunLogResponse.getTaskId());
        assertEquals(taskRunLogRP.getTaskType(), taskRunLogResponse.getTaskType());
        assertEquals(taskRunLogRP.getDescription(), taskRunLogResponse.getTaskDescription());
        assertEquals("COMPLETED", taskRunLogResponse.getStatus());
        assertEquals(taskRunLogRP.getResponseMessage(), taskRunLogResponse.getResponseMessage());
        assertEquals("MONTHLY", taskRunLogResponse.getTaskFrequency());
    }

    @Test
    void buildBuildTaskRunLogResponseFailed() {
        securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

        TaskRunLogRP taskRunLogRP = DataUtil.generateFailedSchdTaskRunLog();
        TaskRunLogResponse taskRunLogResponse = TaskRunLogResponseBuilder.buildTaskRunLogResponse(taskRunLogRP, String.valueOf(Frequency.MONTHLY));

        assertNotNull(taskRunLogResponse);
        assertEquals(taskRunLogRP.getTaskId(), taskRunLogResponse.getTaskId());
        assertEquals(taskRunLogRP.getTaskType(), taskRunLogResponse.getTaskType());
        assertEquals(taskRunLogRP.getDescription(), taskRunLogResponse.getTaskDescription());
        assertEquals("FAILED", taskRunLogResponse.getStatus());
        assertEquals(taskRunLogRP.getResponseMessage() + " Please contact support for more information.", taskRunLogResponse.getResponseMessage());
        assertEquals("MONTHLY", taskRunLogResponse.getTaskFrequency());
    }

}