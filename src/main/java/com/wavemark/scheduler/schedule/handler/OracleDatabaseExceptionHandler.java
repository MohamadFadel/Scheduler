package com.wavemark.scheduler.schedule.handler;

import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleDatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class OracleDatabaseExceptionHandler {

    @ExceptionHandler(OracleDatabaseException.class)
    @ResponseBody
    public ResponseEntity<String> processException(final OracleDatabaseException ex) {
        log.error(ex.getMessage(), ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}