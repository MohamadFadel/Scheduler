//package com.wavemark.scheduler.schedule.service.quartz;
//
//import com.wavemark.scheduler.cron.exception.CronExpressionException;
//import com.wavemark.scheduler.cron.service.CronExpressionService;
//import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
//import com.wavemark.scheduler.schedule.dto.request.TaskInput;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.quartz.*;
//
//import static com.wavemark.scheduler.common.constant.DataMapProperty.CLUSTERED_JOBS_GROUP;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class QuartzServiceTest {
//
//    @Mock
//    private Scheduler clusteredScheduler;
//
//    @Mock
//    private JobDetailService jobDetailService;
//
//    @Mock
//    private TriggerService triggerService;
//
//    @Mock
//    private CronExpressionService cronService;
//
//    @InjectMocks
//    private QuartzService quartzService;
//
//    private ReportInstanceConfig reportInstanceConfig;
//    private TaskInput taskInput;
//    private JobDetail jobDetail;
//    private Trigger trigger;
//
//    @BeforeEach
//    void setUp() throws SchedulerException {
//        reportInstanceConfig = new ReportInstanceConfig();
//        reportInstanceConfig.setId(1L);
//        taskInput = new TaskInput();
//
//        jobDetail = JobBuilder.newJob().build();
//        trigger = TriggerBuilder.newTrigger().build();
//
//        when(jobDetailService.buildOldJobDetail(any(TaskInput.class))).thenReturn(jobDetail);
//        when(triggerService.buildOldTrigger(any(String.class), any(TaskInput.class), any(JobDetail.class))).thenReturn(trigger);
//    }
//
//    @Test
//    void testBuildJob() throws CronExpressionException, SchedulerException {
//        when(cronService.generateCronExpression(any())).thenReturn("0 0/5 * * * ?");
//
//        quartzService.buildJob(taskInput, reportInstanceConfig);
//
//        verify(jobDetailService, times(1)).buildOldJobDetail(taskInput);
//        verify(cronService, times(1)).generateCronExpression(any());
//        verify(triggerService, times(1)).buildOldTrigger(any(String.class), any(TaskInput.class), any(JobDetail.class));
//        verify(clusteredScheduler, times(1)).scheduleJob(jobDetail, trigger);
//    }
//
//    @Test
//    void testPauseJob() throws SchedulerException {
//        quartzService.pauseJob("1");
//
//        verify(clusteredScheduler, times(1)).pauseJob(new JobKey("1", CLUSTERED_JOBS_GROUP));
//    }
//
//    @Test
//    void testRescheduleJob() throws SchedulerException, CronExpressionException {
//        when(cronService.generateCronExpression(any())).thenReturn("0 0/5 * * * ?");
//        doNothing().when(clusteredScheduler).pauseJob(any(JobKey.class));
//
//        quartzService.rescheduleJob("1", taskInput, reportInstanceConfig);
//
//        verify(jobDetailService, times(1)).buildOldJobDetail(taskInput);
//        verify(cronService, times(1)).generateCronExpression(any());
//        verify(triggerService, times(1)).buildOldTrigger(any(String.class), any(TaskInput.class), any(JobDetail.class));
//        verify(clusteredScheduler, times(1)).pauseJob(new JobKey("1", CLUSTERED_JOBS_GROUP));
//        verify(clusteredScheduler, times(1)).addJob(jobDetail, true);
//        verify(clusteredScheduler, times(1)).rescheduleJob(new TriggerKey("1_TRG", CLUSTERED_JOBS_GROUP), trigger);
//    }
//
//    @Test
//    void testDeleteJob() throws SchedulerException {
//        when(clusteredScheduler.deleteJob(any(JobKey.class))).thenReturn(true);
//
//        boolean isDeleted = quartzService.deleteJob("1");
//
//        verify(clusteredScheduler, times(1)).deleteJob(new JobKey("1", CLUSTERED_JOBS_GROUP));
//        assertTrue(isDeleted);
//    }
//}
