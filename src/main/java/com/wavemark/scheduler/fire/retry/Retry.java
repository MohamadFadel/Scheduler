package com.wavemark.scheduler.fire.retry;

import static com.wavemark.scheduler.common.constant.DataMapProperty.NAME;
import static com.wavemark.scheduler.fire.retry.RetryProperty.RETRY_CLUSTERED_JOBS_GROUP;
import static com.wavemark.scheduler.fire.retry.RetryProperty.RETRY_NUMBER_KEY;
import static com.wavemark.scheduler.fire.retry.RetryProperty.RETRY_TRG;
import static com.wavemark.scheduler.fire.retry.RetryProperty.TRIGGER_STATUS_KEY;

import java.util.Calendar;
import java.util.Date;

import com.wavemark.scheduler.fire.constant.TriggerState;
import com.wavemark.scheduler.fire.configuration.RetryConfiguration;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

@Slf4j
@NoArgsConstructor
public class Retry {

    private JobExecutionContext context;

    private Integer retryCount;

    private Date nextFireTime;

    public Retry(JobExecutionContext context) {
        this.context = context;
        this.nextFireTime = new RetryProperty(context).nextFireTime();
        this.retryCount = new RetryProperty(context).getRetryCount() + 1;
    }

    public void retry() {

        try {
            if (RetryValidator.isValid(retryCount, nextFireTime)) {
                scheduleRetryTrigger();
            } else {
                log.info("Retrying is blocked " +
                        "\n\tFailing to meet retry policy: " +
                        "\n\t1- Exceeding max retries: " + RetryConfiguration.retryPolicy().getMaxRetries() +
                        "\n\t2- Or next firing time is less than " + RetryConfiguration.retryPolicy().getDelay() + " minutes");
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    public void scheduleRetryTrigger() throws SchedulerException {

        String newTriggerKey = getTriggerName(context.getJobDetail(), retryCount);

        if (canCreateTrigger(newTriggerKey)) {
            JobDetail job = updateJobDataMap(context.getJobDetail(), retryCount);
            Trigger trigger = newTrigger(job, newTriggerKey);
            Scheduler scheduler = context.getScheduler();
            scheduler.scheduleJob(trigger);
            scheduler.addJob(job, true);
            log.info("A retry trigger was successfully scheduled");
        } else {
            log.info("Unable to add trigger. A trigger with the name " + newTriggerKey + " already exists.");
        }
    }

    protected void setContext(JobExecutionContext context) {
        this.context = context;
    }


    private JobDetail updateJobDataMap(JobDetail job, Integer retryCount) {
        job.getJobDataMap().put(TRIGGER_STATUS_KEY, String.valueOf(TriggerState.RETRY));
        job.getJobDataMap().put(RETRY_NUMBER_KEY, retryCount);
        job = job.getJobBuilder().storeDurably(true).build();
        return job;
    }

    private Trigger newTrigger(JobDetail job, String triggerKey) {
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerKey, RETRY_CLUSTERED_JOBS_GROUP)
                .withSchedule(getSchedBuilder())
                .startAt(getTheTimeOfTheNextRetry().getTime())
                .forJob(job)
                .build();
    }

    private SimpleScheduleBuilder getSchedBuilder() {
        return SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(2)
                .withRepeatCount(0)
                .withMisfireHandlingInstructionFireNow();
    }

    private Calendar getTheTimeOfTheNextRetry() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, RetryConfiguration.RETRY_INTERVAL_MINUTES);
        return calendar;
    }

    public boolean canCreateTrigger(String triggerKey) throws SchedulerException {
        return context.getScheduler().getTrigger(TriggerKey.triggerKey(triggerKey)) == null;
    }

    public String getTriggerName(JobDetail job, Integer retryCount) {
        return job.getJobDataMap().getString(NAME) + RETRY_TRG + retryCount;
    }
}