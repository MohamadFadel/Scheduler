package com.wavemark.scheduler.fire.http.response;

import com.wavemark.scheduler.schedule.domain.entity.Schedulable;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.service.core.ReportInstanceService;
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
    private final ReportInstanceService reportInstanceService;

    public void logResponse(Schedulable schedulable, HttpResponse httpResponse, long time) throws SchedulerException {

        Trigger dbTrigger = triggerService.getTrigger(schedulable.getIdentification());

        Integer taskRunLogId = saveTaskRunLog(schedulable, httpResponse.getMessage(), httpResponse.getCode(), time);
        updateTaskLog(schedulable, dbTrigger, taskRunLogId);

        if (httpResponse.isSuccess()) {
          log.info("Api call {} was successful.", StringEscapeUtils.escapeJava(httpResponse.getUrl()));
            schedulable.setLastSuccessfulRunLogId(taskRunLogId);
        }
        if (schedulable instanceof Task) {
            taskService.saveTask((Task) schedulable);
        } else if (schedulable instanceof ReportInstanceConfig) {
            reportInstanceService.saveReportInstance((ReportInstanceConfig) schedulable);
        }
    }

    public void logResponseError(Schedulable schedulable, HttpResponse httpResponse, long time) throws SchedulerException {

        log.info("Api call " + StringEscapeUtils.escapeJava(httpResponse.getUrl())
                + " Failed with response code " + StringEscapeUtils.escapeJava(String.valueOf(httpResponse.getCode())));
        Trigger dbTrigger = triggerService.getTrigger(schedulable.getTaskName());
        Integer taskLogId = saveTaskRunLog(schedulable, httpResponse.getMessage(), httpResponse.getCode(), time);

        updateTaskLog(schedulable, dbTrigger, taskLogId);
        if (schedulable instanceof Task) {
            taskService.saveTask((Task) schedulable);
        } else if (schedulable instanceof ReportInstanceConfig) {
            reportInstanceService.saveReportInstance((ReportInstanceConfig) schedulable);
        }
    }

    private Integer saveTaskRunLog(Schedulable schedulable, String responseMessage, Integer responseCode, long time)
            throws SchedulerException {

        TaskRunLog taskRunLog = taskRunLogService.buildTaskRunLog(schedulable, responseMessage, responseCode, time);
        return taskRunLogService.saveTaskRunLog(taskRunLog);
    }

    private void updateTaskLog(Schedulable schedulable, Trigger dbTrigger, Integer taskLogId) {
        schedulable.setNextScheduledRun(dbTrigger.getNextFireTime().toInstant());
        schedulable.setLastRunLogId(taskLogId);
    }


}
