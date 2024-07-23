package com.wavemark.scheduler.schedule.service.web;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wavemark.scheduler.common.constant.DateUtil;
import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.dto.response.TaskDetailResponse;
import com.wavemark.scheduler.schedule.dto.response.TaskFrequencyResponse;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskResponseService {

    private final TaskTypeService taskTypeService;
    private final TaskRunLogService taskRunLogService;
    private final CronExpressionService cronExpressionService;

    public TaskResponse buildTaskResponse(Task task) throws CronExpressionException, EntryNotFoundException {

        log.info("Building TaskResponse Object with jobName: " + StringEscapeUtils.escapeJava(task.getTaskName()));

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setTaskId(task.getTaskId());
        taskResponse.setTaskType(taskTypeService.getTaskType(task.getTaskTypeId()));
        taskResponse.setDescription(task.getDescription());
        taskResponse.setCreatedOn(DateUtil.convertToZonedDateTime(task.getCreatedOn()));
        taskResponse.setCreatedBy(task.getCreatedBy());
        taskResponse.setLastUpdatedBy(task.getLastUpdatedBy());
        taskResponse.setLastUpdatedOn(DateUtil.convertToZonedDateTime(task.getLastUpdatedOn()));
        taskResponse.setNextScheduledRun(DateUtil.convertToZonedDateTime(task.getNextScheduledRun()));
        taskResponse.setState(task.getTaskStatus());

        if (task.getLastRunLogId() != null) {
            TaskRunLog taskRunLog = taskRunLogService.getLastRun(task.getLastRunLogId());
            taskResponse.setLastRunOn(DateUtil.convertToZonedDateTime(taskRunLog.getRunStartDate()));
            taskResponse.setLastRunState(taskRunLog.getRunStatus());
            taskResponse.setLastRunResponseMessage(taskRunLog.getResponseMessage());
        }

        TaskDetailResponse taskDetailResponse = TaskDetailResponse.builder()
                .description(task.getDescription())
                .bodyParam(new String(task.getConfiguration()))
                .emails(task.getEmailToList())
                .build();
        taskResponse.setConfiguration(taskDetailResponse);

        String cronExpressionString = task.getCronExpression();
        CronDescription cronDescription = cronExpressionService.reverseCronExpression(cronExpressionString, task.getHospitalDepartmentTimeZone().getID());
        TaskFrequencyResponse taskFrequencyResponse = buildTaskFrequencyResponse(task, cronDescription);
        taskResponse.setTimeFrequency(taskFrequencyResponse);

        return taskResponse;
    }

    private TaskFrequencyResponse buildTaskFrequencyResponse(Task task, CronDescription cronDescription) {
        List<Integer> months = Optional.ofNullable(cronDescription.getMonths())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(month -> Integer.parseInt(month.getCronExpression()))
                .collect(Collectors.toList());

        List<String> daysOfMonth = Optional.ofNullable(cronDescription.getDaysOfMonth())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(DayOfMonth::getCronExpression)
                .collect(Collectors.toList());

        List<String> daysOfWeek = Optional.ofNullable(cronDescription.getDaysOfWeek())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(DayOfWeek::getCronExpression)
                .collect(Collectors.toList());

        return TaskFrequencyResponse.builder()
                .frequency(cronDescription.getFrequency().getCronExpression())
                .minutes(cronDescription.getMinutes())
                .hours(cronDescription.getHours())
                .months(months)
                .daysOfMonth(daysOfMonth)
                .daysOfWeek(daysOfWeek)
                .timezone(task.getHospitalDepartmentTimeZone().getID())
                .build();
    }

}
