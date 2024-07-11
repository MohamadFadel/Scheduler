package com.wavemark.scheduler.schedule.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.wavemark.scheduler.fire.task.ScheduledTask;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class FireControllerTest {

	@Mock
	private ScheduledTask scheduledTask;

	@InjectMocks
	private FireController fireController;

	@Test
	void fire() throws JobExecutionException {

		doNothing().when(scheduledTask).fireTask(any(), any());
		ResponseEntity<?> result = fireController.fire("jobName", "bodyParam");

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
}