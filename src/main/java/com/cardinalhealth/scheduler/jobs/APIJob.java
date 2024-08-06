package com.cardinalhealth.scheduler.jobs;

import com.cardinalhealth.scheduler.http.HTTPConnection;
import com.cardinalhealth.scheduler.http.HTTPParameter;
import com.wavemark.scheduler.fire.http.property.HttpProperty;
import lombok.AllArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wavemark.scheduler.common.constant.DataMapProperty.ENDPOINT_NAME;
@Service
@AllArgsConstructor
public class APIJob extends AbstractJob
{
  private static Logger logger = LogManager.getLogger(APIJob.class);
  private final HTTPConnection httpConnection;

  public void execute(JobExecutionContext jobExecutionContext)
    throws JobExecutionException
  {
    logger.info("Invoke execute in APIJob");
    try
    {
      String url = httpConnection.getAPIJobServerURL("wminternal/updateReceivingCost");
      List<NameValuePair> params = HTTPParameter.getAPIParam(jobExecutionContext.getMergedJobDataMap(), "UpdateReceivingCost");
      HttpProperty httpProperty = HttpProperty.builder()
              .url(url)
              .endpointName(jobExecutionContext.getJobDetail().getJobDataMap().get(ENDPOINT_NAME).toString())
              .bodyParam(params.toString())
              .taskName(jobExecutionContext.getJobDetail().getJobDataMap().get(PARAM_REPORT_INSTANCE_ID).toString())
              .build();
      httpConnection.sendHTTPRequest(params, httpProperty);
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