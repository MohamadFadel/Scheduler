package com.wavemark.scheduler.fire.listener;

import static com.wavemark.scheduler.fire.retry.RetryProperty.RETRY_NUMBER_KEY;
import static com.wavemark.scheduler.fire.retry.RetryProperty.TRIGGER_STATUS_KEY;

import com.wavemark.scheduler.fire.constant.TriggerState;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

@Slf4j
public class JobDataHandler {

    private final JobExecutionContext context;

    public JobDataHandler(JobExecutionContext context) {
        this.context = context;
    }

    public void resetJobData() {
        try {
            if (isARetry()) {
                log.info("Reset retry number. Trigger name = " + context.getTrigger().getKey().getName());
                resetRetryKeys();
            }

        } catch (Exception e) {
            log.error("Failed to reset retry number", e);
        }
    }
    private boolean isARetry() {
        String triggerStatus = context
                .getJobDetail()
                .getJobDataMap()
                .getString(TRIGGER_STATUS_KEY);

        return triggerStatus != null && triggerStatus.equals(String.valueOf(TriggerState.RETRY));
    }

    private void resetRetryKeys() throws SchedulerException {
        JobDetail job = context.getJobDetail();
        job.getJobDataMap().put(RETRY_NUMBER_KEY, 0);
        job.getJobDataMap().put(TRIGGER_STATUS_KEY, String.valueOf(TriggerState.SUCCESS));
        job = job.getJobBuilder().storeDurably(true).build();
        context.getScheduler().addJob(job, true);
    }
}
