package com.wavemark.scheduler.cardinalhealth.scheduler.jobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public abstract class AbstractJob
  implements Job
{
  Logger logger = LogManager.getLogger(AbstractJob.class);
  public static final String PARAM_REPORT_INSTANCE_ID = "reportInstanceId";

  public AbstractJob()
  {
    super();
  }

  protected abstract String getJobId(JobExecutionContext context);
}