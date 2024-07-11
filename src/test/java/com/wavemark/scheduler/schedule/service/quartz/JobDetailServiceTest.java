package com.wavemark.scheduler.schedule.service.quartz;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class JobDetailServiceTest {

    @Test
    void testBuildJobDetail() {
        TaskInput taskInput = DataUtil.generateTaskInput();

        JobDetailService jobDetailService = new JobDetailService();

        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

            assertNotNull(jobDetailService.buildJobDetail(taskInput, "testJobName"));
        }
    }

}