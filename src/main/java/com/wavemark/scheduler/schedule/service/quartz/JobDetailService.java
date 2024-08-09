package com.wavemark.scheduler.schedule.service.quartz;

import com.wavemark.scheduler.cardinalhealth.scheduler.jobs.APPJob;
import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.fire.task.ScheduledTask;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.stereotype.Service;

import static com.wavemark.scheduler.common.constant.DataMapProperty.*;

@Service
public class JobDetailService {
    public static final String PARAM_REPORT_INSTANCE_ID = "reportInstanceId";
    public static final String PARAM_USER_TOKEN = "userToken";


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

    public JobDetail buildOldJobDetail(TaskInput taskInput) {
        return JobBuilder.newJob(APPJob.class)
                .withIdentity(taskInput.getReportInstanceId(), CLUSTERED_JOBS_GROUP)
                .usingJobData(PARAM_REPORT_INSTANCE_ID, taskInput.getReportInstanceId())
                .usingJobData(PARAM_USER_TOKEN, taskInput.getUserToken())
//                .withDescription(taskInput.getDescription())
//                .usingJobData(NAME, jobName)
//                .usingJobData(ENDPOINT_ID, SecurityUtilsV2.getCurrentAuthDepartment())
                .usingJobData(ENDPOINT_NAME, SecurityUtilsV2.getCurrentHospitalDepartmentName())
//                .usingJobData(BODY_PARAM, taskInput.getBodyParam())
                .requestRecovery()
                .storeDurably()
                .build();
    }
}
