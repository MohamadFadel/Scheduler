package com.cardinalhealth.scheduler.jobs;


public enum JobType
{
  ADHOCREPORT("AdHocReport"),
  SCHEDULEJOB("ScheduledJob"),
  EMAILREPORT("EmailReport");
  
  private final String value;
  
  JobType (final String value)
  {
    this.value = value;
  }
  
  public String toString()
  {
    return value;
  }
  
  /* This method is responsible of applying the rules that differentiate between different type of scheduled jobs */
  public static JobType getJobType(String name)
  {
    if (name.equals("AdHocReport"))
      return JobType.ADHOCREPORT;
    else if (name.contains("ScheduledJob"))
      return JobType.SCHEDULEJOB;
    else
      return JobType.EMAILREPORT;
  }
}

