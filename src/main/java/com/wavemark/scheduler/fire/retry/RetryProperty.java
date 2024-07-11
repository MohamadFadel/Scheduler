package com.wavemark.scheduler.fire.retry;

import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;

import java.util.Date;

import com.wavemark.scheduler.fire.constant.TriggerState;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public class RetryProperty {

    public static final String RETRY_CLUSTERED_JOBS_GROUP = "RetryClusteredTasksGroup";
    public static final String RETRY_TRG = "_RETRY_TRG_";
    public static final String TRIGGER_STATUS_KEY = "TRIGGER_STATUS";
    public static final String RETRY_NUMBER_KEY = "RETRY_NUMBER";

    private final JobExecutionContext context;

    public RetryProperty(JobExecutionContext context) {
        this.context = context;
    }

    public Integer getRetryCount() {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String triggerStatus = jobDataMap.getString(TRIGGER_STATUS_KEY);
        if (triggerStatus != null && triggerStatus.equalsIgnoreCase(String.valueOf(TriggerState.RETRY))) {
            return jobDataMap.getIntValue(RETRY_NUMBER_KEY);
        } else {
            return 0;
        }
    }
    public Date nextFireTime() {
        if (isRetryTrigger(context)) {
            try {
                return getNextFireTimeForTheJob();
            } catch (SchedulerException e) {
                return null;
            }
        } else {
            return context.getNextFireTime();
        }
    }

    private Date getNextFireTimeForTheJob() throws SchedulerException {
        TriggerKey triggerKey = getTriggerKey();
        Trigger jobTrigger = context.getScheduler().getTrigger(triggerKey);
        if (jobTrigger == null) {
            return null;
        } else {
            return jobTrigger.getNextFireTime();
        }
    }

    TriggerKey getTriggerKey() {
        String triggerName =  context.getTrigger().getKey().getName();
        triggerName = StringUtils.substringBefore(triggerName, RETRY_TRG);

        return new TriggerKey(triggerName + "_TRG", CLUSTERED_JOBS_GROUP);
    }

    private boolean isRetryTrigger(JobExecutionContext context) {
        String triggerGroup =  context.getTrigger().getKey().getGroup();
        return triggerGroup.equalsIgnoreCase(RETRY_CLUSTERED_JOBS_GROUP);
    }
}
