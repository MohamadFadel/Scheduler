package com.wavemark.scheduler.logging.errorlog.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.wavemark.scheduler.logging.WMException;
import com.wavemark.scheduler.logging.errorlog.service.repository.ErrorLogRepository;
import com.wavemark.scheduler.testing.util.DataUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ErrorLogServiceTest {

	@Mock
	private ErrorLogRepository errorLogRepository;
	@Mock
	private ErrorLogFactory errorLogFactory;
	@Mock
	private ProceedingJoinPoint joinPoint;
	@Mock
	private WMException e;

	@InjectMocks
	private ErrorLogService errorLogService;

	@Test
	void testLogErrorLog() throws Exception {

		when(errorLogFactory.getErrorLogInstance(any(), any())).thenReturn(DataUtil.generateErrorLog());
		doNothing().when(errorLogRepository).insertIntoErrorLog(any(),any(),any(),any(),any(),any(),any(),any(),any());

		assertDoesNotThrow(() -> errorLogService.logErrorLog(joinPoint, e));
	}

	@Test
	void testLogErrorLogDoesNotThrowError() throws Exception {

		when(errorLogFactory.getErrorLogInstance(any(), any())).thenReturn(DataUtil.generateErrorLog());
		Mockito.doThrow(new Exception()).when(errorLogRepository).insertIntoErrorLog(any(),any(),any(),any(),any(),any(),any(),any(),any());

		assertDoesNotThrow(() -> errorLogService.logErrorLog(joinPoint, e));
	}
}