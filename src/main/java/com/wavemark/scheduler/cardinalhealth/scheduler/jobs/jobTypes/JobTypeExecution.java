package com.wavemark.scheduler.cardinalhealth.scheduler.jobs.jobTypes;

import org.apache.http.NameValuePair;

import java.util.List;

public interface JobTypeExecution
{
  String getJobId();
  String getUrl();
  List<NameValuePair> getJobParams();
}
