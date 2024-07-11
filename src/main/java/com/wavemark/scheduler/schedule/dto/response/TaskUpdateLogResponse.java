package com.wavemark.scheduler.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateLogResponse {

	private String taskType;
	private String taskDescription;
	private String taskFrequency;
	@JsonIgnore
	private String identifier;
	private String updatedField;
	private Object previousValue;
	private Object newValue;
	private String updatedOn;
	private String updatedBy;
}
