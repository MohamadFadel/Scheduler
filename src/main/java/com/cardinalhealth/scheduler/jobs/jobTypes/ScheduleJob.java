package com.cardinalhealth.scheduler.jobs.jobTypes;

import com.cardinalhealth.scheduler.http.HTTPConnection;
import com.cardinalhealth.scheduler.http.HTTPParameter;
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
    return HTTPConnection.getAPPJobServerURL(urlActionName);
  }

  @Override
  public List<NameValuePair> getJobParams()
  {
    return HTTPParameter.getScheduledJobParam(getJobDataMap(),getJobName() );
  }

}
