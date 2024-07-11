package com.wavemark.scheduler.fire.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;

@Slf4j
public class SchedulerHandlingListener implements SchedulerListener {

    @Override
    public void jobScheduled(Trigger trigger) {
        log.info("[SCHEDULED] Schedule job for trigger name: " + trigger.getKey().getName());
    }

    @Override
    public void jobUnscheduled(TriggerKey triggerKey) {
        log.info("[UNSCHEDULED] Unscheduled job for trigger name: " + triggerKey.getName());
    }

    @Override
    public void triggerFinalized(Trigger trigger) {
        log.info("[FINISHED] Finalize trigger for trigger name: " + trigger.getKey().getName());
    }

    @Override
    public void triggerPaused(TriggerKey triggerKey) {
        log.info("[PAUSED] Pause trigger for trigger name: " + triggerKey.getName());
    }

    @Override
    public void triggersPaused(String triggerGroup) {
        log.info("[PAUSED] Pause triggers for trigger Group: " + triggerGroup);
    }

    @Override
    public void triggerResumed(TriggerKey triggerKey) {
        log.info("[RESUMED] Resume trigger for trigger name: " + triggerKey.getName());
    }

    @Override
    public void triggersResumed(String triggerGroup) {
        log.info("[RESUMED] Resume triggers for trigger Group: " + triggerGroup);
    }

    @Override
    public void jobAdded(JobDetail jobDetail) {
        log.info("[ADDED] Add job for job name: " + jobDetail.getKey().getName());
    }

    @Override
    public void jobDeleted(JobKey jobKey) {
        log.info("[DELETED] Delete job for job name: " + jobKey.getName());
    }

    @Override
    public void jobPaused(JobKey jobKey) {
        log.info("[PAUSED] Pause job for job name: " + jobKey.getName());
    }

    @Override
    public void jobsPaused(String jobGroup) {
        log.info("[PAUSED] Pause jobs for job Group: " + jobGroup);
    }

    @Override
    public void jobResumed(JobKey jobKey) {
        log.info("[RESUMED] Resume job for job name: " + jobKey.getName());
    }

    @Override
    public void jobsResumed(String jobGroup) {
        log.info("[RESUMED] Resume jobs for job Group: " + jobGroup);
    }

    @Override
    public void schedulerError(String msg, SchedulerException cause) {
        log.error("[ERROR] Scheduler Error occurred with cause = '{}' and exception = '{}'",
                cause != null ? cause.getCause() : "NULL", msg);
    }

    @Override
    public void schedulerInStandbyMode() {
        log.info("[STANDBY] Scheduler is in Standby Mode at: " + new Date());
    }

    @Override
    public void schedulerStarted() {
        log.info("[STARTED] Scheduler Started at: " + new Date());
    }

    @Override
    public void schedulerStarting() {
        log.info("[STARTING] Scheduler is starting now at: " + new Date());
    }

    @Override
    public void schedulerShutdown() {
        log.info("[SHUTDOWN] Scheduler Shutdown at: " + new Date());
    }

    @Override
    public void schedulerShuttingdown() {
        log.info("[SHUTTING DOWN] Scheduler is Shutting down now at: " + new Date());
    }

    @Override
    public void schedulingDataCleared() {
        log.info("[DATA CLEARED] Scheduler Data Cleared at: " + new Date());
    }

}
