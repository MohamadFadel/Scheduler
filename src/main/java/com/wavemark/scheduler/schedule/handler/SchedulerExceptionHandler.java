package com.wavemark.scheduler.schedule.handler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class SchedulerExceptionHandler {

    @ExceptionHandler(SchedulerException.class)
    @ResponseBody
    public ResponseEntity<String> processException(SchedulerException ex) {
        String message = "Unknown scheduler exception";

        log.error(message, ex);

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}