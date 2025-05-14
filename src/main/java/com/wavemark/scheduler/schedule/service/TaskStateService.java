package com.wavemark.scheduler.schedule.service;

import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;

import java.text.ParseException;
import java.util.Date;

import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskService;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskStateService {

    private final Scheduler clusteredScheduler;
    private final TaskService taskService;
    private final RecordLogService recordLogService;

    public void pauseTask(Integer taskId) throws SchedulerException, EntryNotFoundException {

        log.info("Pausing scheduler task with Id: " + taskId + " for endpoint: " + SecurityUtilsV2.getCurrentAuthDepartment());

        Task task = taskService.findActiveTaskById(taskId);
        clusteredScheduler.pauseJob(new JobKey(task.getTaskName(), CLUSTERED_JOBS_GROUP));

        log.info("Calling logTaskUpdateRecordLog method to log into RecordLog Table");
        Integer logId = recordLogService.logDiffableRecordLog(task.getLogId(), task.mapToTaskLogDiffable(),
                task.updateTaskStatus(State.PAUSED).mapToTaskLogDiffable());

        task.setLogId(logId);
        task.setTaskStatus(String.valueOf(State.PAUSED));
        taskService.saveTask(task);
        task.setLogId(logId);
        task.setTaskStatus(String.valueOf(State.PAUSED));
        taskService.saveTask(task);
        task.setLogId(logId);
        task.setTaskStatus(String.valueOf(State.PAUSED));
        taskService.saveTask(task);

        log.info("Task Paused successfully for: " + taskId);
    }

    public void resumeTask(Integer taskId) throws SchedulerException, EntryNotFoundException, ParseException {

        log.info("Resuming scheduler task with Id: " + taskId + " for endpoint: " + SecurityUtilsV2.getCurrentAuthDepartment());

        Task task = taskService.findActiveTaskById(taskId);
        clusteredScheduler.resumeJob(new JobKey(task.getTaskName(), CLUSTERED_JOBS_GROUP));

        CronExpression cronExpressionObj = new CronExpression(task.getCronExpression());
        task.setNextScheduledRun(cronExpressionObj.getNextValidTimeAfter(new Date()).toInstant());

        log.info("Calling logTaskUpdateRecordLog method to log into RecordLog Table");
        Integer logId = recordLogService.logDiffableRecordLog(task.getLogId(), task.mapToTaskLogDiffable(),
                task.updateTaskStatus(State.ACTIVE).mapToTaskLogDiffable());

        task.setLogId(logId);
        task.setTaskStatus(String.valueOf(State.ACTIVE));
        taskService.saveTask(task);

        log.info("Task Resumed successfully for: " + taskId);
    }
}
