package com.wavemark.scheduler.schedule.handler;

import java.text.ParseException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ParseExceptionHandler {

    @ExceptionHandler(ParseException.class)
    @ResponseBody
    public ResponseEntity<String> processException(ParseException ex) {
        String message = "Unknown parse exception";

        log.error(message, ex);

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}