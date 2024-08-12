package com.wavemark.scheduler.schedule.service;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.dto.request.ReportInstanceInput;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.ReportInstanceService;
import com.wavemark.scheduler.schedule.service.quartz.QuartzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReportSchedulerService {
    private final QuartzService quartzService;
//    private final TriggerService triggerService;
//    private final JobDetailService jobDetailService;
//    private final CronExpressionService cronService;
    private final RecordLogService recordLogService;
    private final ReportInstanceService reportInstanceService;


    public void scheduleReportInstance(ReportInstanceInput reportInstanceInput, TaskInput taskInput)
            throws SchedulerException, CronExpressionException {
//        log.info("Creating a Job Detail");
//        JobDetail jobDetail = jobDetailService.buildOldJobDetail(taskInput);
//
//        String cronExpression=cronService.generateCronExpression(taskInput.getTaskFrequencyInput());
//        reportInstanceConfig.setCronschedule(cronExpression);
//
//        log.info("Creating a cron trigger");
//        Trigger trigger = triggerService.buildOldTrigger(cronExpression, taskInput, jobDetail);
        ReportInstanceConfig reportInstanceConfig = reportInstanceService.buildReportInstanceConfig(reportInstanceInput);

        Long reportId = reportInstanceService.getNewReportId();
        taskInput.setReportInstanceId(reportId.toString());
        reportInstanceConfig.setId(reportId);
        quartzService.buildJob(taskInput, reportInstanceConfig);

        Integer logId = recordLogService.logDiffableRecordLog(reportInstanceConfig.getLogId(), reportInstanceConfig.mapToReportInstanceDiffable(),
                reportInstanceConfig.updateReportInstanceStatus(State.CREATED).mapToReportInstanceDiffable());
        reportInstanceConfig.setLogId(logId);

        reportInstanceService.saveReportInstance(reportInstanceConfig);
    }

    public void updateReportInstance(String reportId, ReportInstanceInput reportInstanceInput, TaskInput taskInput)
            throws SchedulerException, CronExpressionException, EntryNotFoundException {

        ReportInstanceConfig reportInstanceConfig = reportInstanceService.buildReportInstanceConfig(reportInstanceInput);
        ReportInstanceConfig oldReportInstanceConfig = reportInstanceService.findReportById(Long.parseLong(reportId));
//        JobKey jobKey = new JobKey(reportId, CLUSTERED_JOBS_GROUP);
//        TriggerKey triggerKey = new TriggerKey(reportId + "_TRG", CLUSTERED_JOBS_GROUP);
//        clusteredScheduler.pauseJob(jobKey);
//        JobDetail jobDetail = jobDetailService.buildOldJobDetail(taskInput);
//
//        String cronExpression=cronService.generateCronExpression(taskInput.getTaskFrequencyInput());
//        reportInstanceConfig.setCronschedule(cronExpression);
//
//        Trigger trigger = triggerService.buildOldTrigger(cronExpression, taskInput, jobDetail);
        taskInput.setReportInstanceId(reportId);
        reportInstanceConfig.setId(new Long(reportId));
        quartzService.rescheduleJob(reportId, taskInput, reportInstanceConfig);

        Integer logId = recordLogService.logDiffableRecordLog(oldReportInstanceConfig.getLogId(), oldReportInstanceConfig.mapToReportInstanceDiffable(),
                reportInstanceConfig.mapToReportInstanceDiffable());
        reportInstanceConfig.setLogId(logId);

        reportInstanceService.saveReportInstance(reportInstanceConfig);
//        clusteredScheduler.addJob(jobDetail, true);
//        clusteredScheduler.rescheduleJob(triggerKey, trigger);
    }

    public void deleteReportInstance(String reportId)
            throws SchedulerException, EntryNotFoundException {

        ReportInstanceConfig reportInstanceConfig = reportInstanceService.findReportById(Long.parseLong(reportId));
        boolean isDeleted= quartzService.deleteJob(reportId);

        if(isDeleted){
            Integer logId = recordLogService.logDiffableRecordLog(reportInstanceConfig.getLogId(), reportInstanceConfig.mapToReportInstanceDiffable(),
                reportInstanceConfig.updateReportInstanceStatus(State.DEACTIVATED).mapToReportInstanceDiffable());

            reportInstanceConfig.setLogId(logId);
            reportInstanceConfig.setStatus(String.valueOf(State.DEACTIVATED));
            reportInstanceService.saveReportInstance(reportInstanceConfig);
        }
    }
}
