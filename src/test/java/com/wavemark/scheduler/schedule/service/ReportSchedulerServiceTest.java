//package com.wavemark.scheduler.schedule.service;
//
//import com.wavemark.scheduler.cron.exception.CronExpressionException;
//import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
//import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
//import com.wavemark.scheduler.schedule.dto.request.TaskInput;
//import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
//import com.wavemark.scheduler.schedule.service.core.ReportInstanceService;
//import com.wavemark.scheduler.schedule.service.quartz.QuartzService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.quartz.SchedulerException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class ReportSchedulerServiceTest {
//
//    @Mock
//    private QuartzService quartzService;
//
//    @Mock
//    private RecordLogService recordLogService;
//
//    @Mock
//    private ReportInstanceService reportInstanceService;
//
//    @InjectMocks
//    private ReportSchedulerService reportSchedulerService;
//
//    private ReportInstanceConfig reportInstanceConfig;
//    private TaskInput taskInput;
//
//    @BeforeEach
//    void setUp() {
//        reportInstanceConfig = new ReportInstanceConfig();
//        reportInstanceConfig.setId(1L);
//        taskInput = new TaskInput();
//    }
//
//    @Test
//    void testScheduleReportInstance() throws SchedulerException, CronExpressionException {
//        when(reportInstanceService.getNewReportId()).thenReturn(1L);
//        when(recordLogService.logDiffableRecordLog(any(), any(), any())).thenReturn(1);
//
//        reportSchedulerService.scheduleReportInstance(reportInstanceConfig, taskInput);
//
//        verify(reportInstanceService, times(1)).getNewReportId();
//        verify(quartzService, times(1)).buildJob(taskInput, reportInstanceConfig);
//        verify(recordLogService, times(1)).logDiffableRecordLog(any(), any(), any());
//        verify(reportInstanceService, times(1)).saveReportInstance(reportInstanceConfig);
//    }
//
//    @Test
//    void testUpdateReportInstance() throws SchedulerException, CronExpressionException, EntryNotFoundException {
//        when(reportInstanceService.findReportById(1L)).thenReturn(reportInstanceConfig);
//        when(recordLogService.logDiffableRecordLog(any(), any(), any())).thenReturn(1);
//
//        reportSchedulerService.updateReportInstance("1", reportInstanceConfig, taskInput);
//
//        verify(reportInstanceService, times(1)).findReportById(1L);
//        verify(quartzService, times(1)).rescheduleJob("1", taskInput, reportInstanceConfig);
//        verify(recordLogService, times(1)).logDiffableRecordLog(any(), any(), any());
//        verify(reportInstanceService, times(1)).saveReportInstance(reportInstanceConfig);
//    }
//
//    @Test
//    void testDeleteReportInstance() throws SchedulerException, EntryNotFoundException {
//        when(reportInstanceService.findReportById(1L)).thenReturn(reportInstanceConfig);
//        when(quartzService.deleteJob("1")).thenReturn(true);
//        when(recordLogService.logDiffableRecordLog(any(), any(), any())).thenReturn(1);
//
//        reportSchedulerService.deleteReportInstance("1");
//
//        verify(reportInstanceService, times(1)).findReportById(1L);
//        verify(quartzService, times(1)).deleteJob("1");
//        verify(recordLogService, times(1)).logDiffableRecordLog(any(), any(), any());
//        verify(reportInstanceService, times(1)).saveReportInstance(reportInstanceConfig);
//    }
//}
