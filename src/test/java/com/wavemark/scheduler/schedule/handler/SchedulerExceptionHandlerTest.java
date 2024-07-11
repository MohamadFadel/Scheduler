package com.wavemark.scheduler.schedule.handler;

import org.junit.jupiter.api.Test;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchedulerExceptionHandlerTest {

    @Test
    void testProcessException() {
        SchedulerExceptionHandler schedulerExceptionHandler = new SchedulerExceptionHandler();

        ResponseEntity<String> result = schedulerExceptionHandler.processException(new SchedulerException());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

}