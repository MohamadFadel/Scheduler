package com.wavemark.scheduler.schedule.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import com.wavemark.scheduler.schedule.validation.annotation.EmailListValidation;
import com.wavemark.scheduler.schedule.validation.annotation.TaskTypeValidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInput {

    @TaskTypeValidation
    private String taskType;
    @Size(max = 250)
    private String description;
    private String bodyParam;
    @Valid
    private TaskFrequencyInput taskFrequencyInput;
    @EmailListValidation
    private String emails;
}
