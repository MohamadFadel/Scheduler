package com.wavemark.scheduler.schedule.service;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.schedule.domain.projection.TaskRunLogRP;
import com.wavemark.scheduler.schedule.dto.response.TaskRunLogResponse;
import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;
import com.wavemark.scheduler.schedule.dto.response.builder.TaskUpdateLogResponseBuilder;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import com.wavemark.scheduler.schedule.service.core.TaskService;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportLogServiceTest {

	@Mock
	private TaskService taskService;
	@Mock
	private TaskRunLogService taskRunLogService;
	@Mock
	private CronExpressionService cronExpressionService;
	@Mock
	private RecordLogService recordLogService;
	@Mock
	private TaskUpdateLogResponseBuilder taskUpdateLogResponseBuilder;

	private static MockedStatic<SecurityUtilsV2> securityUtils;

	@InjectMocks
	private ReportLogService reportLogService;

	@BeforeAll
	static void beforeAll() {
		securityUtils = Mockito.mockStatic(SecurityUtilsV2.class);
	}

	@AfterAll
	static void afterAll() {
		securityUtils.close();
	}

	@Test
	void testGetTasksRunLog() throws CronExpressionException {

		securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

		List<TaskRunLogRP> taskRunLogRPList = Collections.singletonList(DataUtil.generateSchdTaskRunLog());
		Mockito.when(taskRunLogService.getTaskRunLogReport()).thenReturn(taskRunLogRPList);

		CronDescription mockedCronDescription = Mockito.mock(CronDescription.class);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(mockedCronDescription);
		Mockito.when(mockedCronDescription.getFrequency()).thenReturn(Frequency.MONTHLY);

		List<TaskRunLogResponse> taskRunLogResponseList = reportLogService.getTasksRunLogResponse();
		assertNotNull(taskRunLogResponseList);
	}

	@Test
	void testGetTasksRunLogThrowsException() throws CronExpressionException {

		securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

		List<TaskRunLogRP> taskRunLogRPList = Collections.singletonList(DataUtil.generateSchdTaskRunLog());
		Mockito.when(taskRunLogService.getTaskRunLogReport()).thenReturn(taskRunLogRPList);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenThrow(new CronExpressionException(""));

		assertThrows(RuntimeException.class, () -> reportLogService.getTasksRunLogResponse());
	}

	@Test
	void testGetTasksUpdateLogResponse() throws CronExpressionException, EntryNotFoundException {

		when(taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(any(), any())).thenReturn(DataUtil.genarateTaskUpdateLogResponseList());
		when(recordLogService.getRecordLogs()).thenReturn(DataUtil.generateRecordLogsMap());

		List<TaskUpdateLogResponse> tasksUpdateLogResponse = reportLogService.getTaskUpdateLogResponse();
		assertNotNull(tasksUpdateLogResponse);
	}

	@Test
	void testGetTasksUpdateLogResponseThrowsException() throws CronExpressionException, EntryNotFoundException {

		when(recordLogService.getRecordLogs()).thenReturn(DataUtil.generateRecordLogsMap());
		when(taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(any(), any())).thenThrow(new CronExpressionException("Exception!!"));

		assertThrows(RuntimeException.class, () -> reportLogService.getTaskUpdateLogResponse());
	}
}