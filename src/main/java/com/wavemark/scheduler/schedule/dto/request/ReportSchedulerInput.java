package com.wavemark.scheduler.schedule.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportSchedulerInput {

    TaskInput taskInput;
    ReportInstanceInput reportInstanceInput;
}
