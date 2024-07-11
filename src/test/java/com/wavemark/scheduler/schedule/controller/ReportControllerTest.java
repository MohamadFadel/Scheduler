package com.wavemark.scheduler.schedule.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.ReportService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

	@Mock
	private ReportService reportService;

	@InjectMocks
	private ReportController reportController;

	@Test
	void testPing() {
		ResponseEntity<String> result = reportController.ping();
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	void testTasks() throws CronExpressionException, EntryNotFoundException {

		when(reportService.getTasks()).thenReturn(new ArrayList<>());
		ResponseEntity<List<TaskResponse>> result = reportController.tasks();

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
}