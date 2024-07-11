package com.wavemark.scheduler.schedule.service;

import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.dto.FrequencyDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import com.wavemark.scheduler.schedule.dto.response.builder.TaskResponseBuilder;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReportService {

	private final TaskService taskService;
	private final TaskTypeService taskTypeService;
	private final TaskRunLogService taskRunLogService;
	private final CronExpressionService cronExpressionService;
	private final FrequencyDescription frequencyDescription;

	public List<TaskResponse> getTasks() throws CronExpressionException, EntryNotFoundException {

		List<TaskResponse> taskResponseList = new ArrayList<>();
		List<Task> tasks = taskService.findActiveTasksByEndpointId();

		if (Objects.isNull(tasks) || tasks.isEmpty()) {
			log.info("No DynamicReport Tasks Found, total size is 0");
			return new ArrayList<>();
		}
		else {
			log.info("Getting all DynamicReport Tasks, total size is: " + tasks.size());
			for (Task task : tasks)
				taskResponseList.add(getTask(task));
		}
		return taskResponseList;
	}

	public TaskResponse getTask(Task task) throws CronExpressionException, EntryNotFoundException {

		log.info("Building TaskResponse Object");
		String taskType = taskTypeService.getTaskType(task.getTaskTypeId());

		TaskRunLog taskRunLog = new TaskRunLog();
		if (task.getLastRunLogId() != null)
			taskRunLog = taskRunLogService.getLastRun(task.getLastRunLogId());

		String cronExpressionString = task.getCronExpression();
		CronDescription cronDescription = cronExpressionService.reverseCronExpression(cronExpressionString, task.getHospitalDepartmentTimeZone().getID());
		String frequencyDescribed = frequencyDescription.describe(cronDescription);

		return TaskResponseBuilder.buildTaskResponse(task, taskType, taskRunLog, cronDescription, frequencyDescribed);
	}

}
