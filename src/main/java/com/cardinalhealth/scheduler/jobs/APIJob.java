package com.cardinalhealth.scheduler.jobs;

import com.cardinalhealth.scheduler.http.HTTPConnection;
import com.cardinalhealth.scheduler.http.HTTPParameter;
import org.apache.http.NameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;

public class APIJob extends AbstractJob
{
  private static Logger logger = LogManager.getLogger(APIJob.class);

  public void execute(JobExecutionContext jobExecutionContext)
    throws JobExecutionException
  {
    logger.info("Invoke execute in APIJob");
    try
    {
      String url = HTTPConnection.getAPIJobServerURL("wminternal/updateReceivingCost");
      List<NameValuePair> params = HTTPParameter.getAPIParam(jobExecutionContext.getMergedJobDataMap(), "UpdateReceivingCost");
      HTTPConnection.sendHTTPRequest(url, params);
      PostJobFiring.resumeJobErrorCounter(jobExecutionContext);
    }
    catch (Exception e)
    {
      logger.error("Error calling APIJob", e);
      PostJobFiring.scheduleRecoveryJob(jobExecutionContext, getJobId(jobExecutionContext));
    }
  }

  @Override
  protected String getJobId(JobExecutionContext context)
  {
    return context.getJobDetail().getKey().getName();
  }
}