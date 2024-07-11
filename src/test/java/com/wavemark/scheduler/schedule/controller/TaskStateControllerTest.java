package com.wavemark.scheduler.schedule.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.text.ParseException;

import com.wavemark.scheduler.schedule.constant.Success;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.TaskStateService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TaskStateControllerTest {

	@Mock
	private TaskStateService taskStateService;

	@InjectMocks
	private TaskStateController taskController;

	@Test
	void pauseTask() throws SchedulerException, EntryNotFoundException {

		doNothing().when(taskStateService).pauseTask(any());
		ResponseEntity<?> result = taskController.pauseTask(1);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(new Success(), result.getBody());
	}

	@Test
	void resumeTask() throws SchedulerException, EntryNotFoundException, ParseException {

		doNothing().when(taskStateService).resumeTask(any());
		ResponseEntity<?> result = taskController.resumeTask(1);

		assertEquals(HttpStatus.OK, result.getStatusCode());
		assertEquals(new Success(), result.getBody());
	}
}