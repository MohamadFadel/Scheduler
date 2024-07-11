package com.wavemark.scheduler.schedule.service;

import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.logging.recordlog.entity.RecordLog;
import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.projection.TaskRunLogRP;
import com.wavemark.scheduler.schedule.dto.response.TaskRunLogResponse;
import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;
import com.wavemark.scheduler.schedule.dto.response.builder.TaskRunLogResponseBuilder;
import com.wavemark.scheduler.schedule.dto.response.builder.TaskUpdateLogResponseBuilder;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReportLogService {

	private final TaskRunLogService taskRunLogService;
	private final CronExpressionService cronExpressionService;
	private final RecordLogService recordLogService;
	private final TaskUpdateLogResponseBuilder taskUpdateLogResponseBuilder;

	public List<TaskRunLogResponse> getTasksRunLogResponse() {

		log.info("Building TaskRunLogResponse List Object");
		List<TaskRunLogRP> taskRunLogRPList = taskRunLogService.getTaskRunLogReport();

		return taskRunLogRPList.stream().map(taskRunLogRP -> {
			try {
				String frequency = "";
				if (Objects.nonNull(taskRunLogRP.getCronExpression()) && Objects.nonNull(taskRunLogRP.getTimeZone())) {
					String cronExpressionString = taskRunLogRP.getCronExpression();
					CronDescription cronDescription = cronExpressionService.reverseCronExpression(cronExpressionString, taskRunLogRP.getTimeZone().getID());
					frequency = cronDescription.getFrequency().getCapitalizedCronExpression();
				}
				return TaskRunLogResponseBuilder.buildTaskRunLogResponse(taskRunLogRP, frequency);
			} catch (CronExpressionException e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	public List<TaskUpdateLogResponse> getTaskUpdateLogResponse() {

		log.info("Building TaskUpdateLogResponse List Object");
		List<TaskUpdateLogResponse> taskUpdateLogResponseList = new ArrayList<>();
		HashMap<Task, List<RecordLog>> recordLogMap = recordLogService.getRecordLogs();

		for (Task task : recordLogMap.keySet()) {
			List<RecordLog> recordLogs = recordLogMap.get(task);
			recordLogs.forEach(recordLog -> {
				try {
					taskUpdateLogResponseList.addAll(taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(recordLog, task));
				} catch (CronExpressionException | EntryNotFoundException e) {
					throw new RuntimeException(e);
				}
			});
		}

		taskUpdateLogResponseList.sort(Comparator.comparing(TaskUpdateLogResponse::getUpdatedOn).reversed());
		return taskUpdateLogResponseList;
	}

}
