package com.wavemark.scheduler.schedule.service;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.dto.request.ReportInstanceInput;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.ReportInstanceService;
import com.wavemark.scheduler.schedule.service.quartz.QuartzService;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.SchedulerException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportSchedulerServiceTest {

    @Mock
    private QuartzService quartzService;

    @Mock
    private RecordLogService recordLogService;

    @Mock
    private ReportInstanceService reportInstanceService;

    @InjectMocks
    private ReportSchedulerService reportSchedulerService;

    private ReportInstanceConfig reportInstanceConfig;
    private TaskInput taskInput;
    private ReportInstanceInput reportInstanceInput;

    @BeforeEach
    void setUp() {
        reportInstanceConfig = DataUtil.generateReportInstanceConfig();
        taskInput = new TaskInput();
        reportInstanceInput = DataUtil.generateReportInstanceInput();
    }

    @Test
    void testScheduleReportInstance() throws SchedulerException, CronExpressionException {
        when(reportInstanceService.getNewReportId()).thenReturn(1L);
        when(recordLogService.logDiffableRecordLog(any(), any(), any())).thenReturn(1);
        when(reportInstanceService.buildReportInstanceConfig(any())).thenReturn(reportInstanceConfig);
        doNothing().when(quartzService).buildJob(any(),any());
        doNothing().when(reportInstanceService).saveReportInstance(any());


        reportSchedulerService.scheduleReportInstance(reportInstanceInput, taskInput);

        verify(reportInstanceService, times(1)).getNewReportId();
        verify(quartzService, times(1)).buildJob(taskInput, reportInstanceConfig);
        verify(recordLogService, times(1)).logDiffableRecordLog(any(), any(), any());
        verify(reportInstanceService, times(1)).saveReportInstance(reportInstanceConfig);


    }

    @Test
    void testScheduleReportInstanceThrowsCronExpressionException() throws CronExpressionException, SchedulerException {
        when(reportInstanceService.getNewReportId()).thenReturn(1L);
        when(reportInstanceService.buildReportInstanceConfig(any())).thenReturn(reportInstanceConfig);
        doThrow(new CronExpressionException("Invalid Cron Expression"))
                .when(quartzService).buildJob(any(), any());

        assertThrows(CronExpressionException.class,
                () -> reportSchedulerService.scheduleReportInstance(reportInstanceInput, taskInput));

        verify(reportInstanceService, times(1)).getNewReportId();
        verify(quartzService, times(1)).buildJob(any(), any());
    }

    @Test
    void testScheduleReportInstanceThrowsSchedulerException() throws SchedulerException, CronExpressionException {
        when(reportInstanceService.getNewReportId()).thenReturn(1L);
        when(reportInstanceService.buildReportInstanceConfig(any())).thenReturn(reportInstanceConfig);
        doThrow(new SchedulerException())
                .when(quartzService).buildJob(any(), any());

        assertThrows(SchedulerException.class,
                () -> reportSchedulerService.scheduleReportInstance(reportInstanceInput, taskInput));

        verify(reportInstanceService, times(1)).getNewReportId();
        verify(quartzService, times(1)).buildJob(any(), any());
    }

    @Test
    void testUpdateReportInstance() throws SchedulerException, CronExpressionException, EntryNotFoundException {
        when(reportInstanceService.findReportById(1L)).thenReturn(reportInstanceConfig);
        when(reportInstanceService.buildReportInstanceConfig(any())).thenReturn(reportInstanceConfig);
        when(recordLogService.logDiffableRecordLog(any(), any(), any())).thenReturn(1);
        doNothing().when(quartzService).rescheduleJob(any(), any(), any());
        doNothing().when(reportInstanceService).saveReportInstance(any());

        reportSchedulerService.updateReportInstance("1", reportInstanceInput, taskInput);

        verify(reportInstanceService, times(1)).findReportById(1L);
        verify(quartzService, times(1)).rescheduleJob("1", taskInput, reportInstanceConfig);
        verify(recordLogService, times(1)).logDiffableRecordLog(any(), any(), any());
        verify(reportInstanceService, times(1)).saveReportInstance(reportInstanceConfig);
    }

    @Test
    void testUpdateReportInstanceThrowsCronExpressionException() throws CronExpressionException, SchedulerException, EntryNotFoundException {
        when(reportInstanceService.findReportById(1L)).thenReturn(reportInstanceConfig);
        when(reportInstanceService.buildReportInstanceConfig(any())).thenReturn(reportInstanceConfig);
        doThrow(new CronExpressionException("Invalid Cron Expression"))
                .when(quartzService).rescheduleJob(any(), any(), any());

        assertThrows(CronExpressionException.class,
                () -> reportSchedulerService.updateReportInstance("1", reportInstanceInput, taskInput));

        verify(reportInstanceService, times(1)).findReportById(1L);
        verify(quartzService, times(1)).rescheduleJob(any(), any(), any());
    }

    @Test
    void testUpdateReportInstanceThrowsSchedulerException() throws SchedulerException, CronExpressionException, EntryNotFoundException {
        when(reportInstanceService.findReportById(1L)).thenReturn(reportInstanceConfig);
        when(reportInstanceService.buildReportInstanceConfig(any())).thenReturn(reportInstanceConfig);
        doThrow(new SchedulerException())
                .when(quartzService).rescheduleJob(any(), any(), any());

        assertThrows(SchedulerException.class,
                () -> reportSchedulerService.updateReportInstance("1", reportInstanceInput, taskInput));

        verify(reportInstanceService, times(1)).findReportById(1L);
        verify(quartzService, times(1)).rescheduleJob(any(), any(), any());
    }

    @Test
    void testUpdateReportInstanceThrowsEntryNotFoundException() throws SchedulerException, CronExpressionException, EntryNotFoundException {
        when(reportInstanceService.findReportById(1L)).thenThrow(new EntryNotFoundException());
        when(reportInstanceService.buildReportInstanceConfig(any())).thenReturn(reportInstanceConfig);

        assertThrows(EntryNotFoundException.class,
                () -> reportSchedulerService.updateReportInstance("1", reportInstanceInput, taskInput));

        verify(reportInstanceService, times(1)).findReportById(1L);
    }

    @Test
    void testDeleteReportInstance() throws SchedulerException, EntryNotFoundException {
        when(reportInstanceService.findReportById(1L)).thenReturn(reportInstanceConfig);
        when(quartzService.deleteJob("1")).thenReturn(true);
        when(recordLogService.logDiffableRecordLog(any(), any(), any())).thenReturn(1);
        doNothing().when(reportInstanceService).saveReportInstance(any());

        reportSchedulerService.deleteReportInstance("1");

        verify(reportInstanceService, times(1)).findReportById(1L);
        verify(quartzService, times(1)).deleteJob("1");
        verify(recordLogService, times(1)).logDiffableRecordLog(any(), any(), any());
        verify(reportInstanceService, times(1)).saveReportInstance(reportInstanceConfig);
    }

    @Test
    void testDeleteReportInstanceThrowsSchedulerException() throws SchedulerException, EntryNotFoundException {
        when(reportInstanceService.findReportById(1L)).thenReturn(reportInstanceConfig);
        doThrow(new SchedulerException())
                .when(quartzService).deleteJob(any());

        assertThrows(SchedulerException.class,
                () -> reportSchedulerService.deleteReportInstance("1"));

        verify(reportInstanceService, times(1)).findReportById(1L);
        verify(quartzService, times(1)).deleteJob(any());
    }

    @Test
    void testDeleteReportInstanceThrowsEntryNotFoundException() throws SchedulerException, EntryNotFoundException {
        when(reportInstanceService.findReportById(1L)).thenThrow(new EntryNotFoundException());

        assertThrows(EntryNotFoundException.class,
                () -> reportSchedulerService.deleteReportInstance("1"));

        verify(reportInstanceService, times(1)).findReportById(1L);
    }
}
