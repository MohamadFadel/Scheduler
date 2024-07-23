package com.cardinalhealth.scheduler.jobs.switchJob;

import com.cardinalhealth.scheduler.jobs.JobType;
import com.cardinalhealth.scheduler.jobs.jobTypes.AdHocReport;
import com.cardinalhealth.scheduler.jobs.jobTypes.EmailReport;
import com.cardinalhealth.scheduler.jobs.jobTypes.JobTypeExecution;
import com.cardinalhealth.scheduler.jobs.jobTypes.ScheduleJob;
import org.quartz.JobExecutionContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class JobSupplier
{
  private static final Map<String, Supplier<JobTypeExecution>> JOB_SUPPLIER;
  private static JobExecutionContext jobExecutionContext;
  public JobSupplier(JobExecutionContext jobExecutionContext)
  {
    this.jobExecutionContext = jobExecutionContext;
  }

  static {

    final Map<String, Supplier<JobTypeExecution>> jobs = new HashMap<>();
    jobs.put(JobType.ADHOCREPORT.toString(), ()-> new AdHocReport(jobExecutionContext));
    jobs.put(JobType.SCHEDULEJOB.toString(), ()-> new ScheduleJob(jobExecutionContext));
    jobs.put(JobType.EMAILREPORT.toString(), ()-> new EmailReport(jobExecutionContext));

    JOB_SUPPLIER = Collections.unmodifiableMap(jobs);
  }

  public JobTypeExecution supplyJob(String jobType)
  {
    Supplier<JobTypeExecution> jobTypeExecution = JOB_SUPPLIER.get(jobType);

    if (jobTypeExecution == null) {
      throw new IllegalArgumentException("Invalid player type: " + jobType);
    }

    return jobTypeExecution.get();
  }
}
