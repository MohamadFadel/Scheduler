package com.wavemark.scheduler.schedule.dto.response.builder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.wavemark.scheduler.common.constant.DateUtil;
import com.wavemark.scheduler.cron.dto.FrequencyDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.logging.recordlog.entity.RecordLog;
import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.dto.logdiffable.TaskLogDiffable;
import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;
import com.wavemark.scheduler.schedule.dto.response.autoorder.AutoOrderConfiguration;
import com.wavemark.scheduler.schedule.dto.response.autoorder.AutoOrderConfigurationDiff;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskUpdateLogResponseBuilder {

	private final CronExpressionService cronExpressionService;
	private final FrequencyDescription frequencyDescription;
	private final TaskTypeService taskTypeService;

	public List<TaskUpdateLogResponse> buildTaskUpdateLogResponseInstance(RecordLog taskRecordLog, Task task)
			throws CronExpressionException, EntryNotFoundException {

		String frequency = cronExpressionService.reverseCronExpression(task.getCronExpression(), SecurityUtilsV2.getTimezone().getId()).getFrequency().getCapitalizedCronExpression();
		String taskType = taskTypeService.getTaskType(task.getTaskTypeId());

		List<TaskUpdateLogResponse> taskUpdateLogResponseList = buildTaskUpdateLogResponseInstance(taskRecordLog);
		taskUpdateLogResponseList.forEach(taskUpdateLogResponse -> {
			taskUpdateLogResponse.setTaskType(taskType);
			taskUpdateLogResponse.setTaskDescription(task.getDescription());
			taskUpdateLogResponse.setTaskFrequency(frequency);
		});

		return taskUpdateLogResponseList;
	}

	private List<TaskUpdateLogResponse> buildTaskUpdateLogResponseInstance(RecordLog taskRecordLog)
	{
		switch (taskRecordLog.getFieldName())
		{
//			case "emailToList":
//				return buildTaskUpdateLogResponseEmail(taskRecordLog);
			case "cronExpression":
				return buildTaskUpdateLogResponseFrequency(taskRecordLog);
			case "configuration":
				return buildTaskUpdateLogResponseConfiguration(taskRecordLog);
			default:
				return buildTaskUpdateLogResponse(taskRecordLog);
		}
	}

	private List<TaskUpdateLogResponse> buildTaskUpdateLogResponseConfiguration(RecordLog taskRecordLog)
	{
		List<TaskUpdateLogResponse> taskUpdateLogResponseList;
		ObjectMapper mapper = new ObjectMapper();

		try {
			AutoOrderConfiguration previousValue = mapper.readValue(taskRecordLog.getOldValue(), AutoOrderConfiguration.class);
			AutoOrderConfiguration newValue = mapper.readValue(taskRecordLog.getNewValue(), AutoOrderConfiguration.class);

			taskUpdateLogResponseList = AutoOrderConfigurationDiff.getDiff(previousValue, newValue);

			taskUpdateLogResponseList.forEach(taskUpdateLogResponse -> {
				taskUpdateLogResponse.setUpdatedBy(taskRecordLog.getUpdatedBy());
				taskUpdateLogResponse.setUpdatedOn(DateUtil.convertToZonedDateTime(taskRecordLog.getUpdatedDate()));
			});

		} catch (JsonProcessingException e) {
			return Collections.singletonList(TaskUpdateLogResponse.builder()
					.updatedField(TaskLogDiffable.getFieldNameByIdentifier(taskRecordLog.getFieldName()))
					.previousValue(taskRecordLog.getOldValue())
					.newValue(taskRecordLog.getNewValue())
					.updatedBy(taskRecordLog.getUpdatedBy())
					.updatedOn(DateUtil.convertToZonedDateTime(taskRecordLog.getUpdatedDate()))
					.build());
		}

		return taskUpdateLogResponseList;
	}

	private List<TaskUpdateLogResponse> buildTaskUpdateLogResponseFrequency(RecordLog taskRecordLog)
	{
		String previousValue = frequencyDescription.describe(taskRecordLog.getOldValue());
		String newValue = frequencyDescription.describe(taskRecordLog.getNewValue());

		return Collections.singletonList(TaskUpdateLogResponse.builder()
				.updatedField(TaskLogDiffable.getFieldNameByIdentifier(taskRecordLog.getFieldName()))
				.previousValue(previousValue)
				.newValue(newValue)
				.updatedBy(taskRecordLog.getUpdatedBy())
				.updatedOn(DateUtil.convertToZonedDateTime(taskRecordLog.getUpdatedDate()))
				.build());
	}

//	private List<TaskUpdateLogResponse> buildTaskUpdateLogResponseEmail(RecordLog taskRecordLog)
//	{
//		String previousValue;
//		String newValue;
//		if (Objects.isNull(taskRecordLog.getOldValue()) || taskRecordLog.getOldValue().isEmpty()) {
//			previousValue = "Disabled";
//			newValue = "Enabled, " + taskRecordLog.getNewValue().split(",").length + " new email(s) added";
//		} else if (Objects.isNull(taskRecordLog.getNewValue()) || taskRecordLog.getNewValue().isEmpty()) {
//			previousValue = "Enabled, " + taskRecordLog.getOldValue().split(",").length + " email(s)";
//			newValue = "Disabled";
//		} else {
//			previousValue = "Enabled, " + taskRecordLog.getOldValue().split(",").length + " email(s)";
//			newValue = "Enabled, " + taskRecordLog.getNewValue().split(",").length + " email(s)";
//		}
//
//		return Collections.singletonList(TaskUpdateLogResponse.builder()
//				.updatedField(TaskLogDiffable.getFieldNameByIdentifier(taskRecordLog.getFieldName()))
//				.previousValue(previousValue)
//				.newValue(newValue)
//				.updatedBy(taskRecordLog.getUpdatedBy())
//				.updatedOn(DateUtil.convertToZonedDateTime(taskRecordLog.getUpdatedDate()))
//				.build());
//	}

	private List<TaskUpdateLogResponse> buildTaskUpdateLogResponse(RecordLog taskRecordLog) {

		if (taskRecordLog.getFieldName().equalsIgnoreCase("taskStatus") && Objects.nonNull(taskRecordLog.getNewValue()) && taskRecordLog.getNewValue().equalsIgnoreCase(String.valueOf(State.CREATED)))
			return Collections.singletonList(TaskUpdateLogResponse.builder()
					.updatedField(TaskLogDiffable.getFieldNameByIdentifier(taskRecordLog.getFieldName()))
					.previousValue("")
					.newValue(taskRecordLog.getNewValue())
					.updatedBy(taskRecordLog.getUpdatedBy())
					.updatedOn(DateUtil.convertToZonedDateTime(taskRecordLog.getUpdatedDate()))
					.build());

		boolean deleted = taskRecordLog.getFieldName().equalsIgnoreCase("taskStatus") && Objects.nonNull(taskRecordLog.getNewValue())
				&& taskRecordLog.getNewValue().equalsIgnoreCase(String.valueOf(State.DEACTIVATED));

		return Collections.singletonList(TaskUpdateLogResponse.builder()
				.updatedField(TaskLogDiffable.getFieldNameByIdentifier(taskRecordLog.getFieldName()))
				.previousValue(taskRecordLog.getOldValue())
				.newValue(deleted ? "Deleted" : taskRecordLog.getNewValue())
				.updatedBy(taskRecordLog.getUpdatedBy())
				.updatedOn(DateUtil.convertToZonedDateTime(taskRecordLog.getUpdatedDate()))
				.build());
	}
}
