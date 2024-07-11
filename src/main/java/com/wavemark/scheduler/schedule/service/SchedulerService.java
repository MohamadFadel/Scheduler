package com.wavemark.scheduler.schedule.service;

import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;

import java.text.ParseException;
import java.util.List;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.schedule.service.quartz.JobDetailService;
import com.wavemark.scheduler.schedule.service.quartz.TriggerService;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SchedulerService {

    private final Scheduler clusteredScheduler;
    private final TaskService taskService;
    private final JobDetailService jobDetailService;
    private final TriggerService triggerService;
    private final RecordLogService recordLogService;

    public Integer scheduleTask(TaskInput taskInput)
            throws SchedulerException, ParseException, CronExpressionException, EntryNotFoundException {

        log.info("Creating new scheduler task for: " + StringEscapeUtils.escapeJava(taskInput.getTaskType())
                + " for endpoint: " + SecurityUtilsV2.getCurrentAuthDepartment());

        Task task = taskService.buildTask(taskInput);
        String jobName = task.getTaskName();

        log.info("Creating a Job Detail");
        JobDetail jobDetail = jobDetailService.buildJobDetail(taskInput, jobName);

        log.info("Creating a cron trigger");
        Trigger trigger = triggerService.buildTrigger(taskInput, jobDetail, jobName, task.getCronExpression());

        log.info("Calling logTaskUpdateRecordLog method to log into RecordLog Table");
        Integer logId = recordLogService.logDiffableRecordLog(task.getLogId(), task.mapToTaskLogDiffable(),
                task.updateTaskStatus(State.CREATED).mapToTaskLogDiffable());
        task.setLogId(logId);

        log.info("Calling scheduleJob method in Scheduler object");
        Integer createdTaskId = taskService.saveTask(task);
        clusteredScheduler.scheduleJob(jobDetail, trigger);

        log.info("New scheduler task is created successfully for: " + StringEscapeUtils.escapeJava(taskInput.getTaskType()));
        return createdTaskId;
    }

    public void updateScheduledTask(Integer id, TaskInput taskInput)
            throws CronExpressionException, SchedulerException, EntryNotFoundException, ParseException {
        log.info("Updating scheduler task with Id: " + id + " for endpoint: " + SecurityUtilsV2.getCurrentAuthDepartment());

        Task dbTask = taskService.findActiveTaskById(id);

        Task task = taskService.updateTask(dbTask, taskInput);
        JobKey jobKey = new JobKey(task.getTaskName(), CLUSTERED_JOBS_GROUP);
        TriggerKey triggerKey = new TriggerKey(task.getTaskName() + "_TRG", CLUSTERED_JOBS_GROUP);

        Trigger.TriggerState triggerState = clusteredScheduler.getTriggerState(triggerKey);

        log.info("Pausing the previous task");
        clusteredScheduler.pauseJob(jobKey);

        JobDetail jobDetail = jobDetailService.buildJobDetail(taskInput, task.getTaskName());
        Trigger trigger = triggerService.buildTrigger(taskInput, jobDetail, task.getTaskName(), task.getCronExpression());

        log.info("Calling logTaskUpdateRecordLog method to log into RecordLog Table");
        Integer logId = recordLogService.logDiffableRecordLog(dbTask.getLogId(), dbTask.mapToTaskLogDiffable(), task.mapToTaskLogDiffable());
        task.setLogId(logId);

        log.info("Saving updated task " + StringEscapeUtils.escapeJava(task.getTaskName()));
        taskService.saveTask(task);
        clusteredScheduler.addJob(jobDetail, true);
        clusteredScheduler.rescheduleJob(triggerKey, trigger);

        if (triggerState.equals(Trigger.TriggerState.PAUSED))
        {
            log.info("Pausing the task since it was already paused before editing it");
            clusteredScheduler.pauseJob(jobKey);
        }
    }

    public void deleteTask(Integer taskId) throws SchedulerException, EntryNotFoundException {

        log.info("Deleting scheduler task with Id: " + taskId + " for endpoint: " + SecurityUtilsV2.getCurrentAuthDepartment());

        Task task = taskService.findActiveTaskById(taskId);
        boolean isDeleted = clusteredScheduler.deleteJob(new JobKey(task.getTaskName(), CLUSTERED_JOBS_GROUP));

        if (isDeleted) {
            log.info("Calling logTaskUpdateRecordLog method to log into RecordLog Table");
            Integer logId = recordLogService.logDiffableRecordLog(task.getLogId(), task.mapToTaskLogDiffable(),
                    task.updateTaskStatus(State.DEACTIVATED).mapToTaskLogDiffable());

            task.setLogId(logId);
            task.setTaskStatus(String.valueOf(State.DEACTIVATED));
            taskService.saveTask(task);

            log.info("Task Deleted successfully for Id: " + taskId);
        }
        else
            log.error("Task Failed to be deleted for Id: " + taskId);
    }

    //Delete Data completely from the DB - Used for cucumber testing
    public void deleteTasks(List<Integer> tasksIds) throws SchedulerException, EntryNotFoundException {

        for (Integer taskId : tasksIds) {
            log.info("Fetching task with ID: " + taskId);
            Task task = taskService.findTaskById(taskId);

            log.info("Deleting scheduler task with ID: " + taskId);
            taskService.deleteTask(taskId);
            boolean isDeleted = clusteredScheduler.deleteJob(new JobKey(task.getTaskName(), CLUSTERED_JOBS_GROUP));

            if (isDeleted)
                log.info("Task successfully deleted");
            else
                log.info("Task partially deleted");
        }
    }

}