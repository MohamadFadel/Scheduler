package com.cardinalhealth.scheduler.jobs.jobTypes;

import com.cardinalhealth.scheduler.http.HTTPConnection;
import com.cardinalhealth.scheduler.http.HTTPParameter;
import org.apache.http.NameValuePair;
import org.quartz.JobExecutionContext;

import java.util.List;

public class AdHocReport extends JobExecutionPrep implements JobTypeExecution {
  private final String urlActionName = "AdHocReportJob";

  public AdHocReport(JobExecutionContext jobExecutionContext)
  {
    super(jobExecutionContext ,null);
  }

  @Override
  public String getUrl()
  {
    return HTTPConnection.getAPPJobServerURL(urlActionName);
  }

  @Override
  public List<NameValuePair> getJobParams()
  {
    return HTTPParameter.getAdhocReportParam(getJobDataMap(), getJobName());
  }

}