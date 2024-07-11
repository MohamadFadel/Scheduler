package com.wavemark.scheduler.schedule.service.quartz;

import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;

import java.util.List;

import com.wavemark.scheduler.schedule.dto.request.TaskInput;

import lombok.RequiredArgsConstructor;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;

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

    public Trigger getTrigger(String taskName) throws SchedulerException {

        List<? extends Trigger> triggers = clusteredScheduler.getTriggersOfJob(new JobKey(taskName, CLUSTERED_JOBS_GROUP));
        return triggers.stream().filter(trigger -> trigger instanceof CronTrigger).findFirst().orElse(null);
    }

}
