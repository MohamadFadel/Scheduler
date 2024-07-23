package com.wavemark.scheduler.schedule.service;

import com.cardinalhealth.service.support.security.SecurityUtils;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.service.quartz.JobDetailService;
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

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentAuthDepartment).thenReturn("testDep");

            assertNotNull(jobDetailService.buildJobDetail(taskInput, "testJobName"));
        }
    }

}