package com.wavemark.scheduler.schedule.service.core;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.quartz.CronExpression;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskTypeService taskTypeService;

    private final CronExpressionService cronExpressionService;

    public Integer saveTask(Task task) {
        Task createdTask = taskRepository.save(task);
        return createdTask.getTaskId();
    }

    public List<Task> findTasksByEndpointId() {
        return taskRepository.findBySourceIdEndpointId(SecurityUtilsV2.getCurrentAuthDepartment());
    }

    public List<Task> findActiveTasksByEndpointId() {
        List<Task> tasks = taskRepository.findBySourceIdEndpointId(SecurityUtilsV2.getCurrentAuthDepartment());
        return tasks.stream().filter(task -> !task.getTaskStatus().equals(String.valueOf(State.DEACTIVATED))).collect(Collectors.toList());
    }

    public Task findActiveTaskById(Integer taskId) throws EntryNotFoundException {
        Optional<Task> tasks = taskRepository.findByTaskIdAndSourceIdEndpointId(taskId, SecurityUtilsV2.getCurrentAuthDepartment());
        return tasks.filter(task -> !task.getTaskStatus().equals(String.valueOf(State.DEACTIVATED))).orElseThrow(EntryNotFoundException::new);
    }

    public Task findActiveTaskByTaskName(String taskName) throws EntryNotFoundException {
        List<Task> tasks = taskRepository.findByTaskName(taskName);
        return tasks.stream().filter(task -> !task.getTaskStatus().equals(String.valueOf(State.DEACTIVATED))).findFirst().orElseThrow(EntryNotFoundException::new);
    }

    public Task findTaskById(Integer taskId) throws EntryNotFoundException {
        Optional<Task> tasks = taskRepository.findById(taskId);
        return tasks.orElseThrow(EntryNotFoundException::new);
    }

    public Task buildTask(TaskInput taskInput) throws CronExpressionException, ParseException, EntryNotFoundException {

        String cronExpression = cronExpressionService.generateCronExpression(taskInput.getTaskFrequencyInput());
        Integer taskTypeId = taskTypeService.getTaskTypeId(StringEscapeUtils.escapeJava(taskInput.getTaskType()));
        String endpointId = SecurityUtilsV2.getCurrentAuthDepartment();
        String jobName = taskInput.getTaskType() + "_" + endpointId;

        Task task = Task.builder()
                .taskName(jobName)
                .sourceIdEndpointId(endpointId)
                .description(taskInput.getDescription())
//                .createdBy(SecurityUtilsV2.getWebAppUserInfo().getFullName())
                .createdOn(Instant.now())
                .taskStatus(String.valueOf(State.ACTIVE))
                .configuration(taskInput.getBodyParam().toCharArray())
                .emailToList(taskInput.getEmails())
                .cronExpression(cronExpression)
                .taskTypeId(taskTypeId)
                .hospitalDepartmentTimeZone(TimeZone.getTimeZone(taskInput.getTaskFrequencyInput().getTimezone()))
                .build();

        CronExpression cronExpressionObj = new CronExpression(cronExpression);
        task.setNextScheduledRun(cronExpressionObj.getNextValidTimeAfter(new Date()).toInstant());

        return task;
    }

    public Task updateTask(Task dbTask, TaskInput taskInput) throws CronExpressionException, ParseException {

        Task task = new Task();
        String cronExpression = cronExpressionService.generateCronExpression(taskInput.getTaskFrequencyInput());

        task.setTaskId(dbTask.getTaskId());
        task.setTaskName(dbTask.getTaskName());
        task.setTaskTypeId(dbTask.getTaskTypeId());
        task.setLastRunLogId(dbTask.getLastRunLogId());
        task.setLastSuccessfulRunLogId(dbTask.getLastSuccessfulRunLogId());
        task.setSourceIdEndpointId(dbTask.getSourceIdEndpointId());
        task.setCreatedBy(dbTask.getCreatedBy());
        task.setCreatedOn(dbTask.getCreatedOn());
        task.setTaskStatus(dbTask.getTaskStatus());
        task.setLogId(dbTask.getLogId());

        task.setDescription(taskInput.getDescription());
//        task.setLastUpdatedBy(SecurityUtilsV2.getWebAppUserInfo().getFullName());
        task.setLastUpdatedOn(Instant.now());
        task.setConfiguration(taskInput.getBodyParam().toCharArray());
        task.setEmailToList(taskInput.getEmails());
        task.setCronExpression(cronExpression);
        task.setHospitalDepartmentTimeZone(TimeZone.getTimeZone(taskInput.getTaskFrequencyInput().getTimezone()));

        CronExpression cronExpressionObj = new CronExpression(cronExpression);
        task.setNextScheduledRun(cronExpressionObj.getNextValidTimeAfter(new Date()).toInstant());

        return task;
    }

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

}
