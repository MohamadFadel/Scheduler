package com.wavemark.scheduler.schedule.domain.projection;

import java.sql.Timestamp;
import java.util.TimeZone;

public interface SchdTaskRunLog {

	Integer getTaskId();
	String getEndpointId();
	String getTaskType();
	String getDescription();
	String getCronExpression();
	TimeZone getTimeZone();
	Timestamp getRunStartDate();
	Timestamp getRunEndDate();
	String getRunState();
	String getResponseMessage();
}
