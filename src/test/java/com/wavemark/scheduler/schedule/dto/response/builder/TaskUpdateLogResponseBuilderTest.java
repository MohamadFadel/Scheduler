package com.wavemark.scheduler.schedule.dto.response.builder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.ZoneId;
import java.util.List;

import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.dto.FrequencyDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.logging.recordlog.entity.RecordLog;
import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;
import com.wavemark.scheduler.testing.util.DataUtil;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskUpdateLogResponseBuilderTest {

	@Mock
	private CronExpressionService cronExpressionService;
	@Mock
	private FrequencyDescription frequencyDescription;
	@Mock
	private TaskTypeService taskTypeService;

	@InjectMocks
	TaskUpdateLogResponseBuilder taskUpdateLogResponseBuilder;

	@Test
	void testBuildTaskUpdateLogResponseInstanceWithDescription() throws CronExpressionException, EntryNotFoundException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

			List<TaskUpdateLogResponse> taskUpdateLogResponseList =
					taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(DataUtil.generateRecordLog(), DataUtil.generateTask());

			assertNotNull(taskUpdateLogResponseList);
		}
	}

	@Test
	void testBuildTaskUpdateLogResponseInstanceWithCreatedStatus() throws CronExpressionException, EntryNotFoundException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

			List<TaskUpdateLogResponse> taskUpdateLogResponseList =
					taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(DataUtil.generateCreatedStatusRecordLog(), DataUtil.generateTask());

			assertNotNull(taskUpdateLogResponseList);
		}
	}

	@Test
	void testBuildTaskUpdateLogResponseInstanceWithDeletedStatus() throws CronExpressionException, EntryNotFoundException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

			List<TaskUpdateLogResponse> taskUpdateLogResponseList =
					taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(DataUtil.generateDeletedStatusRecordLog(), DataUtil.generateTask());

			assertNotNull(taskUpdateLogResponseList);
		}
	}

	@Test
	void testBuildTaskUpdateLogResponseInstanceWithCronExpression() throws CronExpressionException, EntryNotFoundException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

			List<TaskUpdateLogResponse> taskUpdateLogResponseList =
					taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(DataUtil.generateFrequencyRecordLog(), DataUtil.generateTask());
			assertNotNull(taskUpdateLogResponseList);
		}
	}

	@Test
	void testBuildTaskUpdateLogResponseInstanceWithConfiguration() throws CronExpressionException, EntryNotFoundException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

			List<TaskUpdateLogResponse> taskUpdateLogResponseList =
					taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(DataUtil.generateConfigurationRecordLog(), DataUtil.generateTask());
			assertNotNull(taskUpdateLogResponseList);
		}
	}

	@Test
	void testBuildTaskUpdateLogResponseInstanceWithInvalidConfiguration() throws CronExpressionException, EntryNotFoundException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		when(taskTypeService.getTaskType(any())).thenReturn("Auto-Order");

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

			RecordLog recordLog = DataUtil.generateConfigurationRecordLog();
			recordLog.setOldValue("asdasdasd");
			List<TaskUpdateLogResponse> taskUpdateLogResponseList =
					taskUpdateLogResponseBuilder.buildTaskUpdateLogResponseInstance(recordLog, DataUtil.generateTask());
			assertNotNull(taskUpdateLogResponseList);
		}
	}
}