//package com.wavemark.scheduler.schedule.service.web;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//
//import java.util.Collections;
//import java.util.List;
//
//import com.wavemark.scheduler.cron.constant.Frequency;
//import com.wavemark.scheduler.cron.dto.CronDescription;
//import com.wavemark.scheduler.cron.exception.CronExpressionException;
//import com.wavemark.scheduler.cron.service.CronExpressionService;
//import com.wavemark.scheduler.schedule.domain.projection.SchdTaskRunLog;
//import com.wavemark.scheduler.schedule.dto.response.TaskRunLogResponse;
//import com.wavemark.scheduler.schedule.service.core.SchdTaskRunLogService;
//import com.wavemark.scheduler.testing.util.DataUtil;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class TaskRunLogResponseServiceTest {
//
//	@Mock
//	private SchdTaskRunLogService schdTaskRunLogService;
//	@Mock
//	private CronExpressionService cronExpressionService;
//
//	@InjectMocks
//	TaskRunLogResponseService taskRunLogResponseService;
//
//
//	@Test
//	void buildBuildTasksRunLogResponse() throws CronExpressionException {
//
//		List<SchdTaskRunLog> schdTaskRunLogList = Collections.singletonList(DataUtil.generateSchdTaskRunLog());
//		Mockito.when(schdTaskRunLogService.getSchdTaskRunLog()).thenReturn(schdTaskRunLogList);
//
//		CronDescription mockedCronDescription = Mockito.mock(CronDescription.class);
//		Mockito.when(cronExpressionService.reverseCronExpression(any(),any())).thenReturn(mockedCronDescription);
//		Mockito.when(mockedCronDescription.getFrequency()).thenReturn(Frequency.MONTHLY);
//
//		List<TaskRunLogResponse> taskRunLogResponseList = taskRunLogResponseService.buildTasksRunLogResponse();
//		assertNotNull(taskRunLogResponseList);
//	}
//
//
//	@Test
//	void buildBuildTasksRunLogResponseThrowsException() throws CronExpressionException {
//
//		List<SchdTaskRunLog> schdTaskRunLogList = Collections.singletonList(DataUtil.generateSchdTaskRunLog());
//		Mockito.when(schdTaskRunLogService.getSchdTaskRunLog()).thenReturn(schdTaskRunLogList);
//		Mockito.when(cronExpressionService.reverseCronExpression(any(),any())).thenThrow(new CronExpressionException(""));
//
//		assertThrows(RuntimeException.class, () -> taskRunLogResponseService.buildTasksRunLogResponse());
//	}
//
//
//	@Test
//	void buildBuildTaskRunLogResponse() throws CronExpressionException {
//
//		SchdTaskRunLog schdTaskRunLog = DataUtil.generateSchdTaskRunLog();
//		CronDescription mockedCronDescription = Mockito.mock(CronDescription.class);
//		Mockito.when(cronExpressionService.reverseCronExpression(any(),any())).thenReturn(mockedCronDescription);
//		Mockito.when(mockedCronDescription.getFrequency()).thenReturn(Frequency.MONTHLY);
//
//		TaskRunLogResponse taskRunLogResponse = taskRunLogResponseService.buildTaskRunLogResponse(schdTaskRunLog);
//
//		assertNotNull(taskRunLogResponse);
//		assertEquals(schdTaskRunLog.getTaskId(), taskRunLogResponse.getTaskId());
//		assertEquals(schdTaskRunLog.getTaskType(), taskRunLogResponse.getTaskType());
//		assertEquals(schdTaskRunLog.getDescription(), taskRunLogResponse.getTaskDescription());
//		assertEquals(schdTaskRunLog.getRunState(), taskRunLogResponse.getStatus());
//		assertEquals(schdTaskRunLog.getResponseMessage(), taskRunLogResponse.getResponseMessage());
//		assertEquals("monthly", taskRunLogResponse.getTaskFrequency());
//	}
//}