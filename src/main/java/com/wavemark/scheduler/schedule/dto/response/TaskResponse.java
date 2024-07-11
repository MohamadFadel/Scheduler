package com.wavemark.scheduler.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponse {

    private Integer taskId;
    private String taskType;
    private String description;
    private TaskFrequencyResponse timeFrequency;
    private String createdOn;
    private String createdBy;
    private String nextScheduledRun;
    private String lastUpdatedOn;
    private String lastUpdatedBy;
    private String state;
    private String lastRunOn;
    private String lastRunState;
    private String lastRunResponseMessage;
    private TaskDetailResponse configuration;
}
