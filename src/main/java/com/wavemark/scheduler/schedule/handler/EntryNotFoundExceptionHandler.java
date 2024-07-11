package com.wavemark.scheduler.schedule.handler;

import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class EntryNotFoundExceptionHandler {

    @ExceptionHandler(EntryNotFoundException.class)
    @ResponseBody
    public ResponseEntity<String> processException(EntryNotFoundException ex) {
        String message = "Specified entity not found";

        log.error(message, ex);

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}