package com.wavemark.scheduler.schedule.dto.response.builder;

import com.wavemark.scheduler.common.constant.DateUtil;
import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.dto.response.TaskDetailResponse;
import com.wavemark.scheduler.schedule.dto.response.TaskFrequencyResponse;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class TaskResponseBuilder {

    public static TaskResponse buildTaskResponse(Task task, String taskType, TaskRunLog taskRunLog,
                                                 CronDescription cronDescription, String frequencyDescribed) {

        log.info("Building TaskResponse Object with jobName: {}", StringEscapeUtils.escapeJava(task.getTaskName()));

        TaskDetailResponse taskDetailResponse = TaskDetailResponse.builder()
          .description(task.getDescription())
          .bodyParam(new String(task.getConfiguration()))
          .emails(task.getEmailToList())
          .build();

        TaskFrequencyResponse taskFrequencyResponse = buildTaskFrequencyResponse(task, cronDescription);
        taskFrequencyResponse.setFrequencyDescribed(frequencyDescribed);

        TaskResponse taskResponse = TaskResponse.builder()
          .taskId(task.getTaskId())
          .taskType(taskType)
          .description(task.getDescription())
          .createdOn(DateUtil.convertToZonedDateTime(task.getCreatedOn()))
          .createdBy(task.getCreatedBy())
          .lastUpdatedBy(task.getLastUpdatedBy())
          .lastUpdatedOn(DateUtil.convertToZonedDateTime(task.getLastUpdatedOn()))
          .nextScheduledRun(DateUtil.convertToZonedDateTime(task.getNextScheduledRun()))
          .state(task.getTaskStatus())
          .configuration(taskDetailResponse)
          .timeFrequency(taskFrequencyResponse)
          .build();

        if (task.getLastRunLogId() != null) {
            taskResponse.setLastRunOn(DateUtil.convertToZonedDateTime(taskRunLog.getRunStartDate()));
            taskResponse.setLastRunState(taskRunLog.getRunStatus());
            taskResponse.setLastRunResponseMessage(taskRunLog.getResponseMessage());
        }

        return taskResponse;
    }

    private static TaskFrequencyResponse buildTaskFrequencyResponse(Task task, CronDescription cronDescription) {
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
