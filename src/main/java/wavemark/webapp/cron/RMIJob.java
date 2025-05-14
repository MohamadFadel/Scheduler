package wavemark.webapp.cron;

import com.cardinalhealth.scheduler.jobs.APPJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class RMIJob
  implements Job
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
