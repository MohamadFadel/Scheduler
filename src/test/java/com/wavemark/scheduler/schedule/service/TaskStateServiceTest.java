package com.wavemark.scheduler.schedule.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import com.wavemark.scheduler.logging.recordlog.service.RecordLogService;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.testing.util.DataUtil;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;

@ExtendWith(MockitoExtension.class)
class TaskStateServiceTest {

	@Mock
	private Scheduler clusteredScheduler;
	@Mock
	private TaskService taskService;
	@Mock
	private RecordLogService recordLogService;

	@InjectMocks
	private TaskStateService taskStateService;

	@Test
	void testPauseTask() throws SchedulerException, EntryNotFoundException {

		when(taskService.findActiveTaskById(any())).thenReturn(DataUtil.generateTask());
		when(taskService.saveTask(any())).thenReturn(null);

		doNothing().when(clusteredScheduler).pauseJob(any());
		doReturn(1).when(recordLogService).logDiffableRecordLog(any(), any(), any());

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
			taskStateService.pauseTask(5);

			verify(taskService).saveTask(any());
			verify(clusteredScheduler).pauseJob(any());
		}
	}

	@Test
	void testPauseTaskThrowsEntryNotFoundException() throws EntryNotFoundException {

		when(taskService.findActiveTaskById(any())).thenThrow(new EntryNotFoundException());

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

			assertThrows(EntryNotFoundException.class, () -> taskStateService.pauseTask(5));
		}
	}

	@Test
	void testResumeTask() throws SchedulerException, EntryNotFoundException, ParseException {

		when(taskService.findActiveTaskById(any())).thenReturn(DataUtil.generateTask());
		when(taskService.saveTask(any())).thenReturn(null);

		doNothing().when(clusteredScheduler).resumeJob(any());
		doReturn(1).when(recordLogService).logDiffableRecordLog(any(), any(), any());

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
			taskStateService.resumeTask(5);

			verify(taskService).saveTask(any());
			verify(clusteredScheduler).resumeJob(any());
		}
	}

	@Test
	void testResumeTaskThrowsEntryNotFoundException() throws EntryNotFoundException {

		when(taskService.findActiveTaskById(any())).thenThrow(new EntryNotFoundException());

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");

			assertThrows(EntryNotFoundException.class, () -> taskStateService.resumeTask(5));
		}
	}
}