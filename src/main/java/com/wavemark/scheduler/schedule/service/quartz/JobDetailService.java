package com.wavemark.scheduler.schedule.service.quartz;

import static com.wavemark.scheduler.common.constant.DataMapProperty.BODY_PARAM;
import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;
import static com.wavemark.scheduler.common.constant.DataMapProperty.ENDPOINT_ID;
import static com.wavemark.scheduler.common.constant.DataMapProperty.ENDPOINT_NAME;
import static com.wavemark.scheduler.common.constant.DataMapProperty.NAME;

import com.wavemark.scheduler.fire.task.ScheduledTask;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.stereotype.Service;

@Service
public class JobDetailService {

    public JobDetail buildJobDetail(TaskInput taskInput, String jobName) {
        return JobBuilder.newJob(ScheduledTask.class)
                .withIdentity(jobName, CLUSTERED_JOBS_GROUP)
                .withDescription(taskInput.getDescription())
                .usingJobData(NAME, jobName)
                .usingJobData(ENDPOINT_ID, SecurityUtilsV2.getCurrentAuthDepartment())
                .usingJobData(ENDPOINT_NAME, SecurityUtilsV2.getCurrentHospitalDepartmentName())
                .usingJobData(BODY_PARAM, taskInput.getBodyParam())
                .requestRecovery()
                .storeDurably()
                .build();
    }

}
