package com.cardinalhealth.scheduler.jobs;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;

import java.util.Calendar;
import java.util.Date;

/* This Class is responsible of re-scheduling failed jobs.
 * This code needs to be refactored after moving the quartz scheduling functionality from webapp to scheduler*/
public class PostJobFiring
{
  private static Logger logger = LogManager.getLogger(PostJobFiring.class);
  private static final int QUARTER = 15;
  private static final int QUARTERPLUS = 16;
  private static final int MAX_ALLOWED_ERRORS = 4;
  private static final String ERROR_CLUSTER_DYN_REPORTS_GROUP = "ErrorClstredJobsGroup";
  private static final String PARAM_JOB_ERROR = "ERRORJOB";
  private static final String PARAM_COUNT_JOB_ERROR = "ERRORCOUNT";
  private static final String PARAM_ERROR = "ERROR";

  public static void scheduleRecoveryJob(JobExecutionContext context, String reportInstanceId)
  {
    try
    {
      logger.info("onErrorScheduleJob method started.");
      JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
      String triggerName = context.getTrigger().getKey().getName();
      logger.info("Trigger name = " + triggerName);

      Trigger originalTrigger;
      Date orgTriggerNextFireTime = null;
      String triggerGroup = context.getTrigger().getKey().getGroup();
      logger.info("Trigger Group = " + triggerGroup);
      if (triggerGroup.equalsIgnoreCase(ERROR_CLUSTER_DYN_REPORTS_GROUP))
      {
        String reportInstanceID = triggerName.split("_")[0];
        originalTrigger = context.getScheduler().getTrigger(new TriggerKey(reportInstanceID + "_TRG", "ClstredJobsGroup"));
        if (originalTrigger == null)
        {
          logger.info("Original Trigger is null");
          return;
        }
        logger.info("Original Trigger name = " + originalTrigger.getKey().getName());
        orgTriggerNextFireTime = originalTrigger.getNextFireTime();
      }
      else
        orgTriggerNextFireTime = context.getNextFireTime();


      String errorJob = jobDataMap.getString(PARAM_JOB_ERROR);
      int errorCount = 0;
      if (errorJob != null && errorJob.equals(PARAM_ERROR))
      {
        errorCount = jobDataMap.getIntValue(PARAM_COUNT_JOB_ERROR);
      }
      logger.info("current error count = " + errorCount);
      /*Get the date of the next quarter+*/
      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.MINUTE, QUARTERPLUS);
      Date quarterPlus = calendar.getTime();

      if (context.getNextFireTime() != null)
        logger.info("next fire time = " + orgTriggerNextFireTime);
      else
        logger.info("there is no next fire time");

      if (errorCount < MAX_ALLOWED_ERRORS && (orgTriggerNextFireTime == null || quarterPlus.before(orgTriggerNextFireTime)))
      {
        errorCount++;
        Scheduler scheduler = context.getScheduler();
        JobDetail job = context.getJobDetail();
        job.getJobDataMap().put(PARAM_JOB_ERROR, PARAM_ERROR);
        job.getJobDataMap().put(PARAM_COUNT_JOB_ERROR, errorCount);
        job = job.getJobBuilder().storeDurably(true).build();

        /*Get the date of the next quarter*/
        calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, QUARTER);
        if (context.getScheduler().getTrigger(TriggerKey.triggerKey(reportInstanceId + "_ERRORTRG_" + errorCount)) == null)
        {
          Trigger trigger =
            TriggerBuilder.newTrigger().withIdentity(reportInstanceId + "_ERRORTRG_" + errorCount,
                                                     ERROR_CLUSTER_DYN_REPORTS_GROUP).withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(2).withRepeatCount(0).withMisfireHandlingInstructionFireNow()).startAt(calendar.getTime()).forJob(job).build();
          scheduler.scheduleJob(trigger);
          scheduler.addJob(job, true);
        }
        else
          logger.info("Unable to add trigger. A trigger with the name " + reportInstanceId + "_ERRORTRG_" + errorCount + " already exists.");
      }
      else
      {
        logger.info("attempting to fire the same failed job more than 4 times whithout any success or the next firing time is less than 16 min.");
      }
      logger.info("onErrorScheduleJob method ended.");
    }
    catch (Exception e)
    {
      logger.error("Failed to reschedule job", e);
    }
  }

  public static void resumeJobErrorCounter(JobExecutionContext context)
  {
    try
    {
      logger.info("resumeErrorCounter method started.");
      JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

      logger.info("Trigger name = " + context.getTrigger().getKey().getName());
      String errorJob = jobDataMap.getString(PARAM_JOB_ERROR);
      int errorCount = 0;
      if (errorJob != null && errorJob.equals(PARAM_ERROR))
      {
        Scheduler scheduler = context.getScheduler();
        JobDetail job = context.getJobDetail();
        job.getJobDataMap().put(PARAM_COUNT_JOB_ERROR, errorCount);
        job = job.getJobBuilder().storeDurably(true).build();
        scheduler.addJob(job, true);
      }
      else
      {
        logger.info("no errors.");
      }
      logger.info("resumeErrorCounter method ended.");
    }
    catch (Exception e)
    {
      logger.error("Failed to update job", e);
    }
  }
}