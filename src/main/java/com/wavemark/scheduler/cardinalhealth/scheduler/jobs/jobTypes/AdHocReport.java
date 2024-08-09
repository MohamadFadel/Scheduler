package com.wavemark.scheduler.cardinalhealth.scheduler.jobs.jobTypes;

import com.wavemark.scheduler.cardinalhealth.scheduler.http.HTTPParameter;

import com.wavemark.scheduler.cardinalhealth.scheduler.http.HTTPURL;
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
    return HTTPURL.getAPPJobServerURL(urlActionName);
  }

  @Override
  public List<NameValuePair> getJobParams()
  {
    return HTTPParameter.getAdhocReportParam(getJobDataMap(), getJobName());
  }

}