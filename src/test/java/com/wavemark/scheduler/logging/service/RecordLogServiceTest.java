package com.wavemark.scheduler.logging.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.wavemark.scheduler.logging.recordlog.entity.RecordLog;
import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.logging.recordlog.service.repository.RecordLogRepository;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.testing.util.DataUtil;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.warden.oauth2.common.userdetail.webbappuser.WebappUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordLogServiceTest {

	@Mock
	private RecordLogRepository recordLogRepository;
	@Mock
	private TaskService taskService;

	@InjectMocks
	private RecordLogService recordLogService;


	@Test
	void testGetTaskRecordLogsReturnsCorrectMap() {
		when(taskService.findTasksByEndpointId()).thenReturn(DataUtil.generateTaskList());
		when(recordLogRepository.findFirst100ByLogIdInOrderByUpdatedDateDesc(any())).thenReturn(DataUtil.generateRecordLogs());

		HashMap<Task, List<RecordLog>> recordLogMap = recordLogService.getRecordLogs();

		assertNotNull(recordLogMap);
		assertEquals(2, recordLogMap.size());
	}


	@Test
	void testGetTaskRecordLogs() {
		when(recordLogRepository.findFirst100ByLogIdInOrderByUpdatedDateDesc(any())).thenReturn(DataUtil.generateRecordLogs());

		List<RecordLog> recordLogList = recordLogService.getRecordLogs(DataUtil.generateTaskList().stream().map(Task::getLogId).collect(Collectors.toList()));

		assertNotNull(recordLogList);
		assertEquals(1, recordLogList.size());
	}

	@Test
	void logTaskUpdateRecordLogWithPausedStatus() {

		doNothing().when(recordLogRepository).insertIntoRecordLog(any(),any(),any(),any(),any(),any(),any(),any(),any());

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			WebappUserDetails userMock = mock(WebappUserDetails.class);
			securityUtils.when(SecurityUtilsV2::getWebAppUserInfo).thenReturn(userMock);
			when(userMock.getFullName()).thenReturn("name");

			Integer logId = recordLogService.logDiffableRecordLog(1, DataUtil.generateActiveTaskUpdateLogDTO(),
					DataUtil.generatePausedTaskUpdateLogDTO());

			assertNotNull(logId);
			assertEquals(1, logId);
		}
	}

	@Test
	void logTaskUpdateRecordLogWithResumeStatus() {

		doNothing().when(recordLogRepository).insertIntoRecordLog(any(),any(),any(),any(),any(),any(),any(),any(),any());

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			WebappUserDetails userMock = mock(WebappUserDetails.class);
			securityUtils.when(SecurityUtilsV2::getWebAppUserInfo).thenReturn(userMock);
			when(userMock.getFullName()).thenReturn("name");

			Integer logId = recordLogService.logDiffableRecordLog(1, DataUtil.generatePausedTaskUpdateLogDTO(),
					DataUtil.generateActiveTaskUpdateLogDTO());

			assertNotNull(logId);
			assertEquals(1, logId);
		}
	}

	@Test
	void logTaskUpdateRecordLogWithNullLogId() {

		doNothing().when(recordLogRepository).insertIntoRecordLog(any(),any(),any(),any(),any(),any(),any(),any(),any());
		when(recordLogRepository.getLogIdNextValueSequence()).thenReturn(new BigDecimal(5));

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			WebappUserDetails userMock = mock(WebappUserDetails.class);
			securityUtils.when(SecurityUtilsV2::getWebAppUserInfo).thenReturn(userMock);
			when(userMock.getFullName()).thenReturn("name");

			Integer logId = recordLogService.logDiffableRecordLog(null, DataUtil.generateActiveTaskUpdateLogDTO(),
					DataUtil.generateDeactivatedTaskUpdateLogDTO());

			assertNotNull(logId);
			assertEquals(5, logId);
		}
	}
}