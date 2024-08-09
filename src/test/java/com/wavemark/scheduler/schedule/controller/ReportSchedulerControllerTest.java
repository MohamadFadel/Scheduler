package com.wavemark.scheduler.schedule.controller;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.schedule.constant.Success;
import com.wavemark.scheduler.schedule.dto.request.ReportSchedulerInput;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.ReportSchedulerService;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportSchedulerControllerTest {
    @Mock
    private ReportSchedulerService reportSchedulerService;

    @InjectMocks
    private ReportSchedulerController reportSchedulerController;


    @Test
    void testScheduleReportInstanceConfig() throws SchedulerException, CronExpressionException {
        ReportSchedulerInput reportSchedulerInput = DataUtil.generateReportSchedulerInput();

        doNothing().when(reportSchedulerService).scheduleReportInstance(any(), any());

        ResponseEntity<?> responseEntity = reportSchedulerController.scheduleReportInstanceConfig(reportSchedulerInput);

//        verify(reportSchedulerService, times(1)).scheduleReportInstance((ReportInstanceConfig) wrapper[1], (TaskInput) wrapper[0]);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new Success(), responseEntity.getBody());
    }

    @Test
    void testUpdateReportInstanceConfig() throws SchedulerException, CronExpressionException, EntryNotFoundException {
        ReportSchedulerInput reportSchedulerInput = DataUtil.generateReportSchedulerInput();

        ResponseEntity<?> responseEntity = reportSchedulerController.updateReportInstanceConfig("1", reportSchedulerInput);

        verify(reportSchedulerService, times(1)).updateReportInstance("1", reportSchedulerInput.getReportInstanceInput(), reportSchedulerInput.getTaskInput());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new Success(), responseEntity.getBody());
    }

    @Test
    void testDeleteReportInstanceConfig() throws SchedulerException, EntryNotFoundException {

        ResponseEntity<?> responseEntity = reportSchedulerController.deleteReportInstanceConfig("1");

        verify(reportSchedulerService, times(1)).deleteReportInstance("1");
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(new Success(), responseEntity.getBody());
    }
}
