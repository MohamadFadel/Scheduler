package com.wavemark.scheduler.schedule.domain.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.TimeZone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.dto.logdiffable.TaskLogDiffable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

@Data
@Table(name = "TASK")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskId;
    private String taskName;
    private Integer taskTypeId;
    @Column(name = "SOURCEID_ENDPOINT_ID")
    private String sourceIdEndpointId;
    private String description;
    private Instant createdOn;
    private String createdBy;
    private Instant lastUpdatedOn;
    private String lastUpdatedBy;
    private Instant nextScheduledRun;
    private String taskStatus;
    @Lob
    private char[] configuration;
    private String emailToList;
    private String cronExpression;
    private Integer lastRunLogId;
    private Integer lastSuccessfulRunLogId;
    private TimeZone hospitalDepartmentTimeZone;
    private Integer logId;

    public Task updateTaskStatus(State taskStatus)
    {
        Task task = SerializationUtils.clone(this);
        task.setTaskStatus(String.valueOf(taskStatus));
        return task;
    }

    public TaskLogDiffable mapToTaskLogDiffable() {
        return TaskLogDiffable.builder()
                .taskId(this.getTaskId())
                .taskTypeId(this.getTaskTypeId())
                .taskStatus(StringUtils.capitalize(this.getTaskStatus().toLowerCase()))
                .description(Objects.isNull(this.getDescription()) ? "" : this.getDescription())
                .configuration(new String(this.getConfiguration()))
                .emailToList(Objects.isNull(this.getEmailToList()) ? "" : this.getEmailToList())
                .cronExpression(this.getCronExpression())
                .hospitalDepartmentTimeZone(this.getHospitalDepartmentTimeZone().getID())
                .build();
    }
}