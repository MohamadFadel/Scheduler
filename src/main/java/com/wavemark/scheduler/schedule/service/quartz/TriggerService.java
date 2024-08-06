package com.wavemark.scheduler.schedule.service.quartz;

import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;

@Service
@RequiredArgsConstructor
public class TriggerService {

    private final Scheduler clusteredScheduler;

    public Trigger buildTrigger(TaskInput taskInput, JobDetail jobDetail, String jobName, String cronExpression) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobName + "_TRG", CLUSTERED_JOBS_GROUP)
                .withDescription(taskInput.getDescription())
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
//                        .inTimeZone(TimeZone.getTimeZone(taskInput.getTaskFrequencyInput().getTimezone()))
                        .withMisfireHandlingInstructionFireAndProceed())
                .forJob(jobDetail)
                .build();
    }

    public Trigger buildOldTrigger(String cronExpression, TaskInput taskInput, JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .withIdentity(taskInput.getReportInstanceId() + "_TRG", CLUSTERED_JOBS_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                        //.inTimeZone(timeZone)
                        .withMisfireHandlingInstructionFireAndProceed())
                .forJob(jobDetail)
                .build();
    }

    public Trigger getTrigger(String taskName) throws SchedulerException {

        List<? extends Trigger> triggers = clusteredScheduler.getTriggersOfJob(new JobKey(taskName, CLUSTERED_JOBS_GROUP));
        return triggers.stream().filter(trigger -> trigger instanceof CronTrigger).findFirst().orElse(null);
    }


}
