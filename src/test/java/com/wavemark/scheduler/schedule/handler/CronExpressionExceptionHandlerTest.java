package com.wavemark.scheduler.schedule.handler;

import static org.junit.jupiter.api.Assertions.*;

import com.wavemark.scheduler.cron.exception.CronExpressionException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class CronExpressionExceptionHandlerTest {

	@Test
	void testProcessException() {
		CronExpressionExceptionHandler cronExpressionExceptionHandler = new CronExpressionExceptionHandler();

		ResponseEntity<String> result = cronExpressionExceptionHandler.processException(new CronExpressionException(""));

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
	}

	@Test
	void testProcessExceptionWithCode() {
		CronExpressionExceptionHandler cronExpressionExceptionHandler = new CronExpressionExceptionHandler();

		ResponseEntity<String> result = cronExpressionExceptionHandler.processException(new CronExpressionException(503, "", new Throwable("")));

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
	}
}