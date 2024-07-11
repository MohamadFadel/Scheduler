package com.wavemark.scheduler.fire.listener;

import com.wavemark.scheduler.fire.constant.TriggerState;
import com.wavemark.scheduler.fire.retry.Retry;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.MDC;

@Slf4j
public class JobHandlingListener implements JobListener {

    private static final String JOB_LISTENER_NAME = "GlobalJobListener";

    @Override
    public String getName() {
        return JOB_LISTENER_NAME;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        MDC.put("caller_id", context.getTrigger().getKey().getName());
        log.info("[START] Fire job: " + context.getJobDetail().getKey().getName());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

        log.info("[VETOED] Job will be vetoed");
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

        String fireState;
        if (jobException == null) {
            fireState = String.valueOf(TriggerState.SUCCESS);
            new JobDataHandler(context).resetJobData();
        } else {
            fireState = String.valueOf(TriggerState.FAILURE);
            new Retry(context).retry();
        }

        log.info("[END] Fired job state: " + fireState);
    }

}
