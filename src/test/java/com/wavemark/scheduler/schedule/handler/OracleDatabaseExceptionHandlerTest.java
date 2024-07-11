package com.wavemark.scheduler.schedule.handler;

import oracle.jdbc.OracleDatabaseException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OracleDatabaseExceptionHandlerTest {

    @Test
    void testProcessException() {
        OracleDatabaseExceptionHandler oracleDatabaseExceptionHandler = new OracleDatabaseExceptionHandler();

        ResponseEntity<String> result = oracleDatabaseExceptionHandler
                .processException(new OracleDatabaseException(0, 0, "", "", ""));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

}