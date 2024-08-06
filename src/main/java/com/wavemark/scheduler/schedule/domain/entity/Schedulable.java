package com.wavemark.scheduler.schedule.domain.entity;

import java.time.Instant;
import java.util.TimeZone;

public interface Schedulable {
    Integer getSchedulableId();
    String getTaskName();
    String getDescription();
    String getCronExpression();
    void setLastRunLogId(Integer lastRunLogId);
    String getIdentification();
    void setLastSuccessfulRunLogId(int lastSuccessfulRunLogId);
    void setNextScheduledRun(Instant nextScheduledRun);
    String getEmailToList();
    String getTaskStatus();
    Instant getNextScheduledRun();
    TimeZone getTimeZone();

}
