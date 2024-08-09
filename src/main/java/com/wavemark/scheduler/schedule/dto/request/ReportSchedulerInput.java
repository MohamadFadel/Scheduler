package com.wavemark.scheduler.schedule.dto.request;

import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSchedulerInput {
	TaskInput taskInput;
	ReportInstanceConfig reportInstanceConfig;
}
