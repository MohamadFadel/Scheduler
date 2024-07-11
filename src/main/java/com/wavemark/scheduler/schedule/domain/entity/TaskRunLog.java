package com.wavemark.scheduler.schedule.domain.entity;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "TASK_RUN_LOG")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRunLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer taskRunLogId;
	private Integer taskId;
	private String taskName;
	private String taskDescription;
	private String taskCronExpression;
	private Instant runStartDate;
	private Instant runEndDate;
	private String runStatus;
	private Long runDuration;
	private Integer responseCode;
	private String responseMessage;
	private String instanceName;
}
