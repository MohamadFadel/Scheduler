package com.wavemark.scheduler.cron.dto;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FrequencyDescriptionTest {

	@Mock
	private CronExpressionService cronExpressionService;

	@InjectMocks FrequencyDescription frequencyDescription;

	@Test
	void testDescribe() throws CronExpressionException {
		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.WEEKLY);
		when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		when(cronExpressionService.createCronExpressionStr(any())).thenReturn("0 30 11 ? * 2,4,5 *");

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

			String frequencyDescribed = frequencyDescription.describe("0 30 11 ? * 2,4,5 *");

			assertNotNull(frequencyDescribed);
			assertEquals("Weekly, At 11:30 AM, only on Monday, Wednesday and Thursday (EEST)", frequencyDescribed);
		}
	}

	@Test
	void testDescribeDoesNtThrowException() throws CronExpressionException {
		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.WEEKLY);
		when(cronExpressionService.reverseCronExpression(any(), any())).thenThrow(new CronExpressionException("Exception!!"));

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
			securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());

			String frequencyDescribed = frequencyDescription.describe("0 30 11 ? * 1,3,4 *");

			assertNotNull(frequencyDescribed);
			assertDoesNotThrow(() -> frequencyDescription.describe("0 30 11 ? * 1,3,4 *"));
		}
	}
}