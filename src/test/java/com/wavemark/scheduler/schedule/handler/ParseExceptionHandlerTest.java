package com.wavemark.scheduler.schedule.handler;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParseExceptionHandlerTest {

    @Test
    void testProcessException() {
        ParseExceptionHandler parseExceptionHandler = new ParseExceptionHandler();

        ResponseEntity<String> result = parseExceptionHandler.processException(new ParseException("", 0));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

}