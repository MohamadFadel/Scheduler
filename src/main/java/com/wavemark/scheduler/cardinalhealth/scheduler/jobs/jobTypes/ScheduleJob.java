package com.wavemark.scheduler.cardinalhealth.scheduler.jobs.jobTypes;

import com.wavemark.scheduler.cardinalhealth.scheduler.http.HTTPParameter;
import com.wavemark.scheduler.cardinalhealth.scheduler.http.HTTPUrl;
import org.apache.http.NameValuePair;
import org.quartz.JobExecutionContext;

import java.util.List;
public class ScheduleJob extends JobExecutionPrep implements JobTypeExecution
{
  private final String urlActionName = "ScheduledJob";
  private static final String PARAM_JOB_INSTANCE_ID = "jobInstanceId";

  public ScheduleJob (JobExecutionContext jobExecutionContext)
  {
    super (jobExecutionContext,PARAM_JOB_INSTANCE_ID);
  }

  @Override
  public String getUrl()
  {
    return HTTPUrl.getAPPJobServerURL(urlActionName);
  }

  @Override
  public List<NameValuePair> getJobParams()
  {
    return HTTPParameter.getScheduledJobParam(getJobDataMap(),getJobName() );
  }

}
