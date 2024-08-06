package com.cardinalhealth.scheduler.jobs.jobTypes;

import com.cardinalhealth.scheduler.http.HTTPConnection;
import com.cardinalhealth.scheduler.http.HTTPParameter;
import org.apache.http.NameValuePair;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ScheduleJob extends JobExecutionPrep implements JobTypeExecution
{
  private final String urlActionName = "ScheduledJob";
  private static final String PARAM_JOB_INSTANCE_ID = "jobInstanceId";
  @Autowired
  private HTTPConnection httpConnection;

  public ScheduleJob (JobExecutionContext jobExecutionContext)
  {
    super (jobExecutionContext,PARAM_JOB_INSTANCE_ID);
  }

  @Override
  public String getUrl()
  {
    return httpConnection.getAPPJobServerURL(urlActionName);
  }

  @Override
  public List<NameValuePair> getJobParams()
  {
    return HTTPParameter.getScheduledJobParam(getJobDataMap(),getJobName() );
  }

}
