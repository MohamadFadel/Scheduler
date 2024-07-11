package com.wavemark.scheduler.fire.http.response;

import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.schedule.service.quartz.TriggerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseLogService {

    private final TaskRunLogService taskRunLogService;
    private final TaskService taskService;
    private final TriggerService triggerService;

    public void logResponse(Task task, HttpResponse httpResponse, long time) throws SchedulerException {

        Trigger dbTrigger = triggerService.getTrigger(task.getTaskName());

        Integer taskRunLogId = saveTaskRunLog(task, httpResponse.getMessage(), httpResponse.getCode(), time);
        updateTaskLog(task, dbTrigger, taskRunLogId);

        if (httpResponse.isSuccess()) {
          log.info("Api call {} was successful.", StringEscapeUtils.escapeJava(httpResponse.getUrl()));
            task.setLastSuccessfulRunLogId(taskRunLogId);
        }

        taskService.saveTask(task);
    }

    public void logResponseError(Task task, HttpResponse httpResponse, long time) throws SchedulerException {

        log.info("Api call " + StringEscapeUtils.escapeJava(httpResponse.getUrl())
                + " Failed with response code " + StringEscapeUtils.escapeJava(String.valueOf(httpResponse.getCode())));
        Trigger dbTrigger = triggerService.getTrigger(task.getTaskName());
        Integer taskLogId = saveTaskRunLog(task, httpResponse.getMessage(), httpResponse.getCode(), time);

        updateTaskLog(task, dbTrigger, taskLogId);
        taskService.saveTask(task);
    }

    private Integer saveTaskRunLog(Task dbTask, String responseMessage, Integer responseCode, long time)
            throws SchedulerException {

        TaskRunLog taskRunLog = taskRunLogService.buildTaskRunLog(dbTask, responseMessage, responseCode, time);
        return taskRunLogService.saveTaskRunLog(taskRunLog);
    }

    private void updateTaskLog(Task dbTask, Trigger dbTrigger, Integer taskLogId) {
        dbTask.setNextScheduledRun(dbTrigger.getNextFireTime().toInstant());
        dbTask.setLastRunLogId(taskLogId);
    }

}
