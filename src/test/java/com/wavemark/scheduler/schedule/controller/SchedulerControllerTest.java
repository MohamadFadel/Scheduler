package com.wavemark.scheduler.schedule.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

import java.text.ParseException;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.schedule.constant.Success;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.SchedulerService;
import com.wavemark.scheduler.testing.util.DataUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SchedulerControllerTest {

    @Mock
    private SchedulerService schedulerService;

    @InjectMocks
    private SchedulerController schedulerController;

    @Test
    void testScheduleTask() throws SchedulerException, CronExpressionException, ParseException, EntryNotFoundException {

        doReturn(1).when(schedulerService).scheduleTask(any());

        ResponseEntity<?> result = schedulerController.createTask(DataUtil.generateTaskInput());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(new Success(), result.getBody());
    }

    @Test
    void testScheduleTask_edit() throws SchedulerException, CronExpressionException, ParseException, EntryNotFoundException {

        doNothing().when(schedulerService).updateScheduledTask(any(), any());

        ResponseEntity<?> result = schedulerController.updateTask(DataUtil.generateTaskInput(), 2);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(new Success(), result.getBody());
    }

    @Test
    void testDeleteTask() throws SchedulerException, EntryNotFoundException {

        doNothing().when(schedulerService).deleteTask(any());

        ResponseEntity<?> result = schedulerController.deleteTask(1);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(new Success(), result.getBody());
    }

}