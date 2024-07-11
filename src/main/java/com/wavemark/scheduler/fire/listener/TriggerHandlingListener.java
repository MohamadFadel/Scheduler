package com.wavemark.scheduler.fire.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

@Slf4j
public class TriggerHandlingListener implements TriggerListener {

    private static final String TRIGGER_LISTENER_NAME = "GlobalTriggerListener";

    @Override
    public String getName() {
        return TRIGGER_LISTENER_NAME;
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        log.info("[FIRED] trigger fired for trigger name: " + trigger.getKey());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        log.info("[VETOED] Veto Job Execution trigger for trigger name: " + trigger.getKey());
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        log.info("[MISFIRED] " + getName() + " trigger: " + trigger.getKey() + " misfired at " + trigger.getStartTime());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {
        log.info("[COMPLETED] " + getName() + " trigger: " + trigger.getKey() + " completed at " + trigger.getStartTime());
    }

}
