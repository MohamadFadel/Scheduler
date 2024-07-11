package com.wavemark.scheduler.schedule.domain.projection;

import java.time.Instant;
import java.util.TimeZone;

public interface TaskRunLogRP {

	Integer getTaskId();
	String getEndpointId();
	String getTaskType();
	String getDescription();
	String getCronExpression();
	TimeZone getTimeZone();
	Instant getRunStartDate();
	String getRunStatus();
	int getResponseCode();
	String getResponseMessage();
}
