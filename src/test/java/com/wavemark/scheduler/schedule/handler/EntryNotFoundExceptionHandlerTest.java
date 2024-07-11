package com.wavemark.scheduler.schedule.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class EntryNotFoundExceptionHandlerTest {

    @Test
    void testProcessException() {
        EntryNotFoundExceptionHandler entryNotFoundExceptionHandler = new EntryNotFoundExceptionHandler();

        ResponseEntity<String> result = entryNotFoundExceptionHandler.processException(new EntryNotFoundException(""));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testProcessExceptionWithCause() {
        EntryNotFoundExceptionHandler entryNotFoundExceptionHandler = new EntryNotFoundExceptionHandler();

        ResponseEntity<String> result = entryNotFoundExceptionHandler.processException(new EntryNotFoundException("", null));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testProcessExceptionWithErrorCode() {
        EntryNotFoundExceptionHandler entryNotFoundExceptionHandler = new EntryNotFoundExceptionHandler();

        ResponseEntity<String> result = entryNotFoundExceptionHandler.processException(new EntryNotFoundException(510, "", null));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

}