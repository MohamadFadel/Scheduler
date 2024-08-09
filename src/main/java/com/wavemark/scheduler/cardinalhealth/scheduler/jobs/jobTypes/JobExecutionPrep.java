package com.wavemark.scheduler.cardinalhealth.scheduler.jobs.jobTypes;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

public class JobExecutionPrep
{
  private JobExecutionContext jobExecutionContext;
  private String jobName;
  private JobDataMap jobDataMap;
  private String jobId;
  private static final String PARAM_REPORT_INSTANCE_ID = "reportInstanceId";

  public JobExecutionPrep(JobExecutionContext jobExecutionContext, String jobIdentifier)
  {
    this.jobExecutionContext = jobExecutionContext;
    jobDataMap = jobExecutionContext.getMergedJobDataMap();
    jobName = jobExecutionContext.getJobDetail().getKey().getName();
    jobId = getJobExecutionContext().getJobDetail().getJobDataMap().getString(jobIdentifier != null ?  jobIdentifier : PARAM_REPORT_INSTANCE_ID);
  }

  public JobExecutionContext getJobExecutionContext()
  {
    return jobExecutionContext;
  }

  public String getJobName()
  {
    return jobName;
  }

  public JobDataMap getJobDataMap()
  {
    return jobDataMap;
  }

  public String getJobId()
  {
    return jobId;
  }

}
