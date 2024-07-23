package com.cardinalhealth.scheduler.jobs;

import com.cardinalhealth.scheduler.http.HTTPConnection;
import com.cardinalhealth.scheduler.jobs.jobTypes.JobTypeExecution;
import com.cardinalhealth.scheduler.jobs.switchJob.JobSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class APPJob extends AbstractJob
{
  Logger logger = LogManager.getLogger(APPJob.class);

  public void execute(JobExecutionContext jobExecutionContext)
    throws JobExecutionException
  {
    logger.info("Invoke execute in App Job");
    JobTypeExecution job = getJobTypeExecution (jobExecutionContext);
    try
    {
      executeJob(jobExecutionContext,job);
    }
    catch (Exception e)
    {
      errorRecovery(e,jobExecutionContext,job.getJobId());
    }
  }

  private void executeJob (JobExecutionContext jobExecutionContext,JobTypeExecution job)
      throws Exception
  {
    HTTPConnection.sendHTTPRequest(job.getUrl(), job.getJobParams());
    PostJobFiring.resumeJobErrorCounter(jobExecutionContext);
  }

  private void errorRecovery (Exception e,JobExecutionContext jobExecutionContext,String jobId)
  {
    logger.error("Error calling App JOB", e);
    PostJobFiring.scheduleRecoveryJob(jobExecutionContext, jobId);
  }

  private String getJobType (JobExecutionContext jobExecutionContext)
  {
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    return JobType.getJobType(jobName).toString();
  }

  private JobTypeExecution getJobTypeExecution (JobExecutionContext jobExecutionContext)
  {
    JobSupplier jobSupplier = new JobSupplier(jobExecutionContext);
    return jobSupplier.supplyJob(getJobType(jobExecutionContext));
  }

  @Override
  protected String getJobId(JobExecutionContext context)
  {
    return context.getJobDetail().getJobDataMap().getString(PARAM_REPORT_INSTANCE_ID);
  }
}
