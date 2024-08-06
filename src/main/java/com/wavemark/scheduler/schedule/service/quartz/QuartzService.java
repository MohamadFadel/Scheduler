package com.wavemark.scheduler.schedule.service.quartz;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuartzService {
    private final Scheduler clusteredScheduler;
    private final JobDetailService jobDetailService;
    private final TriggerService triggerService;
    private final CronExpressionService cronService;


    public void buildJob(TaskInput taskInput, ReportInstanceConfig reportInstanceConfig) throws CronExpressionException, SchedulerException {
        log.info("Creating a Job Detail");
        JobDetail jobDetail = jobDetailService.buildOldJobDetail(taskInput);

        String cronExpression=cronService.generateCronExpression(taskInput.getTaskFrequencyInput());
        reportInstanceConfig.setCronschedule(cronExpression);

        log.info("Creating a cron trigger");
        Trigger trigger = triggerService.buildOldTrigger(cronExpression, taskInput, jobDetail);
        clusteredScheduler.scheduleJob(jobDetail, trigger);
    }

    public void pauseJob(String reportId) throws SchedulerException {
        JobKey jobKey = new JobKey(reportId, CLUSTERED_JOBS_GROUP);
        clusteredScheduler.pauseJob(jobKey);
    }

    public void rescheduleJob(String reportId, TaskInput taskInput, ReportInstanceConfig reportInstanceConfig) throws SchedulerException, CronExpressionException {
        log.info("Creating a Job Detail");
        JobDetail jobDetail = jobDetailService.buildOldJobDetail(taskInput);

        String cronExpression=cronService.generateCronExpression(taskInput.getTaskFrequencyInput());
        reportInstanceConfig.setCronschedule(cronExpression);

        log.info("Creating a cron trigger");
        Trigger trigger = triggerService.buildOldTrigger(cronExpression, taskInput, jobDetail);

        pauseJob(reportId);

        TriggerKey triggerKey = new TriggerKey(reportId + "_TRG", CLUSTERED_JOBS_GROUP);
        clusteredScheduler.addJob(jobDetail, true);
        clusteredScheduler.rescheduleJob(triggerKey, trigger);
    }

    public boolean deleteJob(String reportId) throws SchedulerException {
        return clusteredScheduler.deleteJob(new JobKey(reportId, CLUSTERED_JOBS_GROUP));
    }

}
