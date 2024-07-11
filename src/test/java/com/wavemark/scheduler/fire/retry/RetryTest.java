package com.wavemark.scheduler.fire.retry;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.impl.JobDetailImpl;

@ExtendWith(MockitoExtension.class)
class RetryTest {

	@Mock
	private JobExecutionContext context;

	private Retry retry = new Retry();

	@Test
	void testRetry_withoutNotValid() {
		try (MockedStatic<RetryValidator> retryValidatorMockedStatic = mockStatic(RetryValidator.class)) {
			retryValidatorMockedStatic.when(() -> RetryValidator.isValid(any(), any()))
					.thenReturn(false);

			assertDoesNotThrow(retry::retry);
		}
	}

	@Test
	void testRetry_withException() throws SchedulerException {
		try (MockedStatic<RetryValidator> retryValidatorMockedStatic = mockStatic(RetryValidator.class)) {
			retryValidatorMockedStatic.when(() -> RetryValidator.isValid(any(), any()))
					.thenReturn(true);

			retry =  Mockito.spy(Retry.class);
			Mockito.doThrow(new SchedulerException()).when(retry).scheduleRetryTrigger();

			assertThrows(RuntimeException.class, retry::retry);
		}
	}

	@Test
	void testScheduleRetryTrigger_withoutTrigger() throws SchedulerException {

		Retry retryMock =  Mockito.spy(Retry.class);
		Mockito.doReturn(new JobDetailImpl()).when(context).getJobDetail();
		retryMock.setContext(context);

		String triggerKey = "triggerKey";
		Mockito.doReturn(triggerKey).when(retryMock).getTriggerName(any(), any());
		Mockito.doReturn(false).when(retryMock).canCreateTrigger(triggerKey);

		assertDoesNotThrow(retryMock::scheduleRetryTrigger);
	}
}