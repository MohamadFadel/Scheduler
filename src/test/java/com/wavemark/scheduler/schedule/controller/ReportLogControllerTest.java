package com.wavemark.scheduler.schedule.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.wavemark.scheduler.schedule.dto.response.TaskRunLogResponse;
import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;
import com.wavemark.scheduler.schedule.service.ReportLogService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ReportLogControllerTest {

	@Mock
	private ReportLogService reportLogService;

	@InjectMocks
	private ReportLogController reportLogController;


	@Test
	void testTasksRunLog() {

		when(reportLogService.getTasksRunLogResponse()).thenReturn(new ArrayList<>());
		ResponseEntity<List<TaskRunLogResponse>> result = reportLogController.tasksRunLog();

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	void testTasksUpdateLog() {

		when(reportLogService.getTaskUpdateLogResponse()).thenReturn(new ArrayList<>());
		ResponseEntity<List<TaskUpdateLogResponse>> result = reportLogController.tasksUpdateLog();

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}
}