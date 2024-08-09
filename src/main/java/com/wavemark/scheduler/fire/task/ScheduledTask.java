package com.wavemark.scheduler.fire.task;

import static com.wavemark.scheduler.common.constant.DataMapProperty.BODY_PARAM;
import static com.wavemark.scheduler.common.constant.DataMapProperty.ENDPOINT_ID;
import static com.wavemark.scheduler.common.constant.DataMapProperty.ENDPOINT_NAME;
import static com.wavemark.scheduler.common.constant.DataMapProperty.NAME;

import java.io.IOException;

import com.wavemark.scheduler.fire.authentication.service.OAuth2Service;
import com.wavemark.scheduler.fire.http.property.HttpProperty;
import com.wavemark.scheduler.fire.task.service.ScheduledTaskService;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ScheduledTask implements Job {

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    @Autowired
    private OAuth2Service OAuth2Service;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();

        if (jobDataMap.getString(NAME) != null) {
            try {
                String authToken = "";
                OAuth2Service.getWardenAuthToken(jobDataMap);

                HttpProperty httpProperty = scheduledTaskService.fetchTaskRequestProperty(jobDataMap, authToken);
                scheduledTaskService.postTask(httpProperty);
            } catch (SchedulerException | EntryNotFoundException | IOException e) {
                throw new JobExecutionException(e);
            }
        }
    }

    public void fireTask(String jobName, String bodyParam) throws JobExecutionException {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(NAME, jobName);
        jobDataMap.put(BODY_PARAM, bodyParam);
        jobDataMap.put(ENDPOINT_ID, StringUtils.substringAfter(jobName, '_'));
        jobDataMap.put(ENDPOINT_NAME, StringUtils.substringAfter(jobName, '_'));

        try {
            String authToken = "";
            OAuth2Service.getWardenAuthToken(jobDataMap);

            HttpProperty httpProperty = scheduledTaskService.fetchTaskRequestProperty(jobDataMap, authToken);
            scheduledTaskService.postTask(httpProperty);
        } catch (SchedulerException | EntryNotFoundException | IOException e) {
            throw new JobExecutionException(e);
        }

    }

}