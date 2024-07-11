package com.wavemark.scheduler.schedule.service;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.dto.FrequencyDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

	@Mock
	private TaskService taskService;
	@Mock
	private TaskRunLogService taskRunLogService;
	@Mock
	private CronExpressionService cronExpressionService;
	@Mock
	private TaskTypeService taskTypeService;
	@Mock
	private FrequencyDescription frequencyDescription;
	private static MockedStatic<SecurityUtilsV2> securityUtils;

	@InjectMocks
	private ReportService reportService;

	@BeforeAll
	static void beforeAll() {
		securityUtils = Mockito.mockStatic(SecurityUtilsV2.class);
	}

	@AfterAll
	static void afterAll() {
		securityUtils.close();
	}

	@Test
	void testGetTasks() throws CronExpressionException, EntryNotFoundException {

		when(taskService.findActiveTasksByEndpointId()).thenReturn(DataUtil.generateTaskList());
		when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");
		when(taskRunLogService.getLastRun(any())).thenReturn(new TaskRunLog());
		when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(new CronDescription(DataUtil.generateTaskFrequencyInput()));
		Mockito.when(frequencyDescription.describe((CronDescription) any())).thenReturn("");

		securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

		List<TaskResponse> result = reportService.getTasks();

		assertEquals(2, result.size());
	}

	@Test
	void testGetTask() throws CronExpressionException, EntryNotFoundException {

		when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");
		when(taskRunLogService.getLastRun(any())).thenReturn(new TaskRunLog());
		when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(new CronDescription(DataUtil.generateTaskFrequencyInput()));
		Mockito.when(frequencyDescription.describe((CronDescription) any())).thenReturn("");

		securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

		assertNotNull(reportService.getTask(DataUtil.generateTask()));
	}

	@Test
	void testGetTasks_noTasks() throws CronExpressionException, EntryNotFoundException {

		when(taskService.findActiveTasksByEndpointId()).thenReturn(null);
		List<TaskResponse> result = reportService.getTasks();

		assertEquals(0, result.size());
	}

}