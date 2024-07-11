package com.wavemark.scheduler.schedule.domain.entity;

import javax.persistence.*;

import lombok.Data;

@Data
@Table(name = "TASK_TYPE")
@Entity
public class TaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer taskTypeId;
    private String taskType;
    private String apiEndpoint;
}
