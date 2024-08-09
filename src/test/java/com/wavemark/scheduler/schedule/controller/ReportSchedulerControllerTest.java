//package com.wavemark.scheduler.schedule.controller;
//
//import com.wavemark.scheduler.cron.exception.CronExpressionException;
//import com.wavemark.scheduler.schedule.constant.Success;
//import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
//import com.wavemark.scheduler.schedule.dto.request.TaskInput;
//import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
//import com.wavemark.scheduler.schedule.service.ReportSchedulerService;
//import com.wavemark.scheduler.testing.util.DataUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.quartz.SchedulerException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//public class ReportSchedulerControllerTest {
//    @Mock
//    private ReportSchedulerService reportSchedulerService;
//
//    @InjectMocks
//    private ReportSchedulerController reportSchedulerController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testScheduleReportInstanceConfig() throws SchedulerException, CronExpressionException {
//        Object[] wrapper = DataUtil.generateWrapper();
//
//        ResponseEntity<?> responseEntity = reportSchedulerController.scheduleReportInstanceConfig(wrapper);
//
//        verify(reportSchedulerService, times(1)).scheduleReportInstance((ReportInstanceConfig) wrapper[1], (TaskInput) wrapper[0]);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(new Success(), responseEntity.getBody());
//    }
//
//    @Test
//    void testUpdateReportInstanceConfig() throws SchedulerException, CronExpressionException, EntryNotFoundException {
//        Object[] wrapper = DataUtil.generateWrapper();
//
//        ResponseEntity<?> responseEntity = reportSchedulerController.updateReportInstanceConfig("1", wrapper);
//
//        verify(reportSchedulerService, times(1)).updateReportInstance("1", (ReportInstanceConfig) wrapper[1], (TaskInput) wrapper[0]);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(new Success(), responseEntity.getBody());
//    }
//
//    @Test
//    void testDeleteReportInstanceConfig() throws SchedulerException, EntryNotFoundException {
//
//        ResponseEntity<?> responseEntity = reportSchedulerController.deleteReportInstanceConfig("1");
//
//        verify(reportSchedulerService, times(1)).deleteReportInstance("1");
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(new Success(), responseEntity.getBody());
//    }
//
//
//
//
////
////    @Test
////    void testScheduleReportInstance() throws SchedulerException, CronExpressionException, ParseException, EntryNotFoundException {
////
////        doReturn(1).when(reportSchedulerService).scheduleReportInstance(any(),any());
////
////        ResponseEntity<?> result = reportSchedulerController.scheduleReportInstanceConfig(DataUtil.generateWrapper());
////
////        assertEquals(HttpStatus.OK, result.getStatusCode());
////        assertEquals(new Success(), result.getBody());
////    }
////
////    @Test
////    void testUpdateReportInstance() throws SchedulerException, CronExpressionException, ParseException, EntryNotFoundException {
////
////        doNothing().when(reportSchedulerService).updateReportInstance(any(), any(), any());
////
////        ResponseEntity<?> result = reportSchedulerController.updateReportInstanceConfig("99", DataUtil.generateWrapper());
////
////        assertEquals(HttpStatus.OK, result.getStatusCode());
////        assertEquals(new Success(), result.getBody());
////    }
////
////    @Test
////    void testDeleteReportInstance() throws SchedulerException, EntryNotFoundException {
////
////        doNothing().when(reportSchedulerService).deleteReportInstance(any());
////
////        ResponseEntity<?> result = reportSchedulerController.deleteReportInstanceConfig("100");
////
////        assertEquals(HttpStatus.OK, result.getStatusCode());
////        assertEquals(new Success(), result.getBody());
////    }
//}
