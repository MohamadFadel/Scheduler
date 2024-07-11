package com.wavemark.scheduler.schedule.dto.logdiffable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.wavemark.scheduler.logging.recordlog.LogDiffable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskLogDiffable implements Serializable, LogDiffable<TaskLogDiffable> {

	private static final Map<String, String> fieldsDisplayName = TaskLogDiffable.getFieldNames();

	private Integer taskId;
	private Integer taskTypeId;
	private String taskStatus;
	private String description;
	private String configuration;
	private String emailToList;
	private String cronExpression;
	private String hospitalDepartmentTimeZone;

	public static String getFieldNameByIdentifier(String identifier) {
		return TaskLogDiffable.fieldsDisplayName.get(identifier);
	}

	private static Map<String, String> getFieldNames() {
		Map<String, String> keyValue = new HashMap<>();

		keyValue.put("taskStatus", "State");
		keyValue.put("description", "Description");
		keyValue.put("configuration", "Configuration");
		keyValue.put("emailToList", "Email Notification");
		keyValue.put("cronExpression", "Frequency");
		keyValue.put("hospitalDepartmentTimeZone", "Timezone");

		return keyValue;
	}

	@Override
	public DiffResult<TaskLogDiffable> diff(TaskLogDiffable obj) {
		return new DiffBuilder<>(this, obj, ToStringStyle.JSON_STYLE)
				.append("taskStatus", this.taskStatus, obj.taskStatus)
				.append("description", this.description, obj.description)
				.append("configuration", this.configuration, obj.configuration)
				.append("emailToList", this.emailToList, obj.emailToList)
				.append("cronExpression", this.cronExpression, obj.cronExpression)
				.append("hospitalDepartmentTimeZone", this.hospitalDepartmentTimeZone, obj.hospitalDepartmentTimeZone)
				.build();
	}

	@Override
	public String getTableName() {
		return "TASK";
	}
}
