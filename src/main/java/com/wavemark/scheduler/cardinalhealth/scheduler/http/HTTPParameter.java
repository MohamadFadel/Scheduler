package com.wavemark.scheduler.cardinalhealth.scheduler.http;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;

import java.util.ArrayList;
import java.util.List;

public class HTTPParameter
{  
  public static List<NameValuePair> getAPIParam(JobDataMap jobDataMap, String jobName)
  {
   return new QueryBuilder(jobDataMap, jobName).addSSOAuthToken().getPostParameters();
  }  
  public static List<NameValuePair> getAdhocReportParam(JobDataMap jobDataMap, String jobName)
  {
   return new QueryBuilder(jobDataMap, jobName).addReportInstanceId().addLastRunDate().addAuthToken().getPostParameters();
  }  
  public static List<NameValuePair> getScheduledJobParam(JobDataMap jobDataMap, String jobName)
  {
    return new QueryBuilder(jobDataMap, jobName).addJobInstanceId().addUserToken().getPostParameters();
  }
  public static List<NameValuePair> getEmailReportParam(JobDataMap jobDataMap, String jobName)
  {
    return new QueryBuilder(jobDataMap, jobName).addReportInstanceId().addUserToken().getPostParameters();
  }
  
  static class QueryBuilder
  {
    private static final String AUTH_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJTY2hlZHVsZXJQcm9qZWN0IiwiaWF0IjoxNTM0MjMyMDU2LCJleHAiOjQwOTAzNzYxMDUsImF1ZCI6ImxvY2FsaG9zdCIsInN1YiI6IiJ9.JgGx9mXEGN3xEDYPBaZZYWcKcuchBDa8fqF2G5fhF00";
    private static final String PARAM_REPORT_INSTANCE_LAST_RUN_DATE = "lastRunDate";
    private static final String PARAM_REPORT_INSTANCE_ID = "reportInstanceId";
    private static final String PARAM_JOB_INSTANCE_ID = "jobInstanceId";
    private static final String PARAM_USER_TOKEN = "userToken";   
    private static final String PARAM_JOB_NAME = "jobName"; 
    
    private JobDataMap jobDataMap;
    private List<NameValuePair> postParameters = new ArrayList<>();
    Logger logger = LogManager.getLogger(QueryBuilder.class);
    
    private QueryBuilder(JobDataMap jobDataMap, String jobName)
    {
      this.jobDataMap = jobDataMap;
      postParameters.add(new BasicNameValuePair(PARAM_JOB_NAME, jobName));
    }
    
    private QueryBuilder addReportInstanceId()
    {
      postParameters.add(new BasicNameValuePair(PARAM_REPORT_INSTANCE_ID, jobDataMap.getString(PARAM_REPORT_INSTANCE_ID)));
      return this;
    }
    
    private QueryBuilder addJobInstanceId()
    {
      postParameters.add(new BasicNameValuePair(PARAM_JOB_INSTANCE_ID, jobDataMap.getString(PARAM_JOB_INSTANCE_ID)));
      return this;
    }
    
    private QueryBuilder addUserToken()
    {
      postParameters.add(new BasicNameValuePair(PARAM_USER_TOKEN, jobDataMap.getString(PARAM_USER_TOKEN)));
      return this;
    }
    
    private QueryBuilder addLastRunDate()
    {
      postParameters.add(new BasicNameValuePair(PARAM_REPORT_INSTANCE_LAST_RUN_DATE, jobDataMap.getString(PARAM_REPORT_INSTANCE_LAST_RUN_DATE)));
      return this;
    }  
    
    private QueryBuilder addSSOAuthToken()
    {
      postParameters.add(new BasicNameValuePair("oauth_ssotoken", AUTH_TOKEN));
      return this;
    }
    
    private QueryBuilder addAuthToken()
    {
      postParameters.add(new BasicNameValuePair("authToken", AUTH_TOKEN));
      return this;
    }
    
    private List<NameValuePair> getPostParameters()
    {
      return postParameters;
    }
  }
}