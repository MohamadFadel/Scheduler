package com.wavemark.scheduler.logging.recordlog.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.wavemark.scheduler.logging.recordlog.LogDiffable;
import com.wavemark.scheduler.logging.recordlog.entity.RecordLog;
import com.wavemark.scheduler.logging.recordlog.service.repository.RecordLogRepository;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.service.core.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordLogService {

	private final TaskService taskService;
	private final RecordLogRepository recordLogRepository;

	public HashMap<Task, List<RecordLog>> getRecordLogs() {
		HashMap<Task, List<RecordLog>> recordLogMap = new HashMap<>();

		List<Task> taskList = taskService.findTasksByEndpointId();
		for (Task task : taskList)
			recordLogMap.put(task, getRecordLogs(Collections.singletonList(task.getLogId())));

		return recordLogMap;
	}

	public List<RecordLog> getRecordLogs(List<Integer> logIds) {
		return recordLogRepository.findFirst100ByLogIdInOrderByUpdatedDateDesc(logIds);
	}

	public Integer logDiffableRecordLog(Integer logId, LogDiffable oldLogDiffable, LogDiffable newLogDiffable) {
		if (Objects.isNull(logId))
			logId = getLogIdNextValueSequence();

		List<RecordLog> recordLogList = RecordLogFactory.getRecordLogInstance(logId, oldLogDiffable, newLogDiffable);
		insertIntoRecordLog(recordLogList);

		return logId;
	}

	private void insertIntoRecordLog(List<RecordLog> recordLogs)
	{
		if (!recordLogs.isEmpty())
			recordLogs.forEach(recordLog ->
					recordLogRepository.insertIntoRecordLog(recordLog.getTableName(), recordLog.getFieldName(), recordLog.getOldValue(),
							recordLog.getNewValue(), recordLog.getUpdatedBy(), new Timestamp(Calendar.getInstance().getTime().getTime()),
							recordLog.getLogId(), recordLog.getWmComment(), "ACTIVE")
			);
	}

	public Integer getLogIdNextValueSequence() {
		return recordLogRepository.getLogIdNextValueSequence().intValue();
	}
}
