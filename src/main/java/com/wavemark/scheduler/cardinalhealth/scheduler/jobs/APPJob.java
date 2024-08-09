package com.wavemark.scheduler.cardinalhealth.scheduler.jobs;

import com.wavemark.scheduler.cardinalhealth.scheduler.http.HTTPConnection;
import com.wavemark.scheduler.cardinalhealth.scheduler.jobs.jobTypes.JobTypeExecution;
import com.wavemark.scheduler.cardinalhealth.scheduler.jobs.switchJob.JobSupplier;
import com.wavemark.scheduler.fire.http.property.HttpProperty;

import lombok.NoArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wavemark.scheduler.common.constant.DataMapProperty.ENDPOINT_NAME;

@Component
@NoArgsConstructor
public class APPJob implements Job
{
  @Autowired
  private HTTPConnection httpConnection;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    JobTypeExecution job = getJobTypeExecution (jobExecutionContext);
    try
    {
      executeJob(jobExecutionContext,job);
    }
    catch (Exception e)
    {
      throw new JobExecutionException(e);
//      errorRecovery(e,jobExecutionContext,job.getJobId());
    }
  }

  private void executeJob (JobExecutionContext jobExecutionContext,JobTypeExecution job)
      throws Exception
  {
    HttpProperty httpProperty = HttpProperty.builder()
            .url(job.getUrl())
            .endpointName(jobExecutionContext.getJobDetail().getJobDataMap().get(ENDPOINT_NAME).toString())
            .bodyParam(job.getJobParams().toString())
            .taskName(jobExecutionContext.getJobDetail().getJobDataMap().get("reportInstanceId").toString())
            .build();
    httpConnection.sendHTTPRequest(job.getJobParams(), httpProperty);
    PostJobFiring.resumeJobErrorCounter(jobExecutionContext);
  }

  private void errorRecovery (Exception e,JobExecutionContext jobExecutionContext,String jobId)
  {
    PostJobFiring.scheduleRecoveryJob(jobExecutionContext, jobId);
  }

  private String getJobType (JobExecutionContext jobExecutionContext)
  {
    String jobName = jobExecutionContext.getJobDetail().getKey().getName();
    return JobType.getJobType(jobName).toString();
  }

  private JobTypeExecution getJobTypeExecution (JobExecutionContext jobExecutionContext)
  {
    JobSupplier jobSupplier = new JobSupplier(jobExecutionContext);
    return jobSupplier.supplyJob(getJobType(jobExecutionContext));
  }

  protected String getJobId(JobExecutionContext context)
  {
    return context.getJobDetail().getJobDataMap().getString("reportInstanceId");
  }
}
