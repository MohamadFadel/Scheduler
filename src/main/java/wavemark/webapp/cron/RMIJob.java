package wavemark.webapp.cron;

import com.cardinalhealth.scheduler.jobs.APPJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RMIJob
{
  public RMIJob()
  {
    super();
  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext)
    throws JobExecutionException
  {
    APPJob appJob = new APPJob();
    appJob.execute(jobExecutionContext);
    return;
  }
}
