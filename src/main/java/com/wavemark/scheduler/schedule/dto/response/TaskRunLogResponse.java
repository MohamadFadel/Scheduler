package com.wavemark.scheduler.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRunLogResponse {

	private Integer taskId;
	private String taskType;
	private String taskDescription;
	private String taskFrequency;
	private String startDateTime;
	private String status;
	private String responseMessage;
}
