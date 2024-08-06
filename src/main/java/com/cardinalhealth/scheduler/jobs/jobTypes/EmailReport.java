package com.cardinalhealth.scheduler.jobs.jobTypes;

import com.cardinalhealth.scheduler.http.HTTPConnection;
import com.cardinalhealth.scheduler.http.HTTPParameter;
import org.apache.http.NameValuePair;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmailReport extends JobExecutionPrep implements JobTypeExecution
{
  private final String urlActionName = "EmailReportJob";
  @Autowired
  private HTTPConnection httpConnection;

  public EmailReport (JobExecutionContext jobExecutionContext)
  {
    super (jobExecutionContext,null);
  }

  @Override
  public String getUrl()
  {
    return httpConnection.getAPPJobServerURL(urlActionName);
  }
  @Override
  public List<NameValuePair> getJobParams()
  {
   return HTTPParameter.getEmailReportParam(getJobDataMap(),getJobName());
  }

}
