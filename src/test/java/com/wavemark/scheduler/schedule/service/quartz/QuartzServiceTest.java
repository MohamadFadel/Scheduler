package com.wavemark.scheduler.schedule.service.quartz;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;

import java.text.ParseException;
import java.util.Date;

import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuartzServiceTest {

    @Mock
    private Scheduler clusteredScheduler;

    @Mock
    private JobDetailService jobDetailService;

    @Mock
    private TriggerService triggerService;

    @Mock
    private CronExpressionService cronService;

    @InjectMocks
    private QuartzService quartzService;

    private ReportInstanceConfig reportInstanceConfig;
    private TaskInput taskInput;
    private JobDetail jobDetail;
    private Trigger trigger;

    @BeforeEach
    void setUp() throws SchedulerException {
        reportInstanceConfig = DataUtil.generateReportInstanceConfig();
        reportInstanceConfig.setId(1L);
        taskInput = DataUtil.generateTaskInput();

        jobDetail = DataUtil.generateJobDetail();
        trigger = DataUtil.generateTrigger();
    }

    @Test
    void testBuildJob() throws CronExpressionException, SchedulerException, ParseException {
        when(cronService.generateCronExpression(any())).thenReturn("0 0/5 * * * ?");
        when(jobDetailService.buildOldJobDetail(any())).thenReturn(jobDetail);
        when(triggerService.buildOldTrigger(any(),any(),any())).thenReturn(trigger);
        when(clusteredScheduler.scheduleJob(any(),any())).thenReturn(new Date());

        quartzService.buildJob(taskInput, reportInstanceConfig);

        verify(jobDetailService, times(1)).buildOldJobDetail(taskInput);
        verify(cronService, times(1)).generateCronExpression(any());
        verify(triggerService, times(1)).buildOldTrigger(any(String.class), any(TaskInput.class), any(JobDetail.class));
        verify(clusteredScheduler, times(1)).scheduleJob(jobDetail, trigger);
    }

    @Test
    void testBuildJobThrowsCronExpressionException() throws CronExpressionException, SchedulerException {
        when(cronService.generateCronExpression(any())).thenThrow(new CronExpressionException("Invalid Cron Expression"));

        assertThrows(CronExpressionException.class,
                () -> quartzService.buildJob(taskInput, reportInstanceConfig));

        verify(cronService, times(1)).generateCronExpression(any());
    }

    @Test
    void testBuildJobThrowsSchedulerException() throws SchedulerException, CronExpressionException {
        when(cronService.generateCronExpression(any())).thenReturn("0 0/5 * * * ?");
        doThrow(new SchedulerException()).when(clusteredScheduler).scheduleJob(any(), any());

        assertThrows(SchedulerException.class,
                () -> quartzService.buildJob(taskInput, reportInstanceConfig));

        verify(clusteredScheduler, times(1)).scheduleJob(any(), any());
    }

    @Test
    void testPauseJob() throws SchedulerException {
        quartzService.pauseJob("1");

        verify(clusteredScheduler, times(1)).pauseJob(new JobKey("1", CLUSTERED_JOBS_GROUP));
    }

    @Test
    void testPauseJobThrowsSchedulerException() throws SchedulerException {
        doThrow(new SchedulerException()).when(clusteredScheduler).pauseJob(any(JobKey.class));

        assertThrows(SchedulerException.class,
                () -> quartzService.pauseJob("1"));

        verify(clusteredScheduler, times(1)).pauseJob(any(JobKey.class));
    }

    @Test
    void testRescheduleJob() throws SchedulerException, CronExpressionException, ParseException {
        when(cronService.generateCronExpression(any())).thenReturn("0 0/5 * * * ?");
        doNothing().when(clusteredScheduler).pauseJob(any());
        when(jobDetailService.buildOldJobDetail(any())).thenReturn(jobDetail);
        when(triggerService.buildOldTrigger(any(),any(),any())).thenReturn(trigger);
        when(clusteredScheduler.rescheduleJob(any(),any())).thenReturn(new Date());

        quartzService.rescheduleJob("1", taskInput, reportInstanceConfig);

        verify(jobDetailService, times(1)).buildOldJobDetail(taskInput);
        verify(cronService, times(1)).generateCronExpression(any());
        verify(triggerService, times(1)).buildOldTrigger(any(), any(), any());
        verify(clusteredScheduler, times(1)).pauseJob(new JobKey("1", CLUSTERED_JOBS_GROUP));
        verify(clusteredScheduler, times(1)).addJob(jobDetail, true);
        verify(clusteredScheduler, times(1)).rescheduleJob(new TriggerKey("1_TRG", CLUSTERED_JOBS_GROUP), trigger);
    }

    @Test
    void testRescheduleJobThrowsCronExpressionException() throws CronExpressionException, SchedulerException {
        when(cronService.generateCronExpression(any())).thenThrow(new CronExpressionException("Invalid Cron Expression"));

        assertThrows(CronExpressionException.class,
                () -> quartzService.rescheduleJob("1", taskInput, reportInstanceConfig));

        verify(cronService, times(1)).generateCronExpression(any());
    }

    @Test
    void testRescheduleJobThrowsSchedulerException() throws SchedulerException, CronExpressionException {
        when(cronService.generateCronExpression(any())).thenReturn("0 0/5 * * * ?");
        doThrow(new SchedulerException()).when(clusteredScheduler).rescheduleJob(any(), any());

        assertThrows(SchedulerException.class,
                () -> quartzService.rescheduleJob("1", taskInput, reportInstanceConfig));

        verify(clusteredScheduler, times(1)).rescheduleJob(any(), any());
    }

    @Test
    void testDeleteJob() throws SchedulerException {
        when(clusteredScheduler.deleteJob(any(JobKey.class))).thenReturn(true);

        boolean isDeleted = quartzService.deleteJob("1");

        verify(clusteredScheduler, times(1)).deleteJob(new JobKey("1", CLUSTERED_JOBS_GROUP));
        assertTrue(isDeleted);
    }

    @Test
    void testDeleteJobThrowsSchedulerException() throws SchedulerException {
        doThrow(new SchedulerException()).when(clusteredScheduler).deleteJob(any(JobKey.class));

        assertThrows(SchedulerException.class,
                () -> quartzService.deleteJob("1"));

        verify(clusteredScheduler, times(1)).deleteJob(any(JobKey.class));
    }
}
