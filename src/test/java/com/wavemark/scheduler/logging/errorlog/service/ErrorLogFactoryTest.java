package com.wavemark.scheduler.logging.errorlog.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

import com.wavemark.scheduler.logging.WMException;
import com.wavemark.scheduler.logging.errorlog.entity.ErrorLog;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ErrorLogFactoryTest {

	@Mock
	private ProceedingJoinPoint joinPoint;

	@InjectMocks
	private ErrorLogFactory errorLogFactory;

	@Test
	void testConstructErrorLogFactory()
	{
		ReflectionTestUtils.setField(errorLogFactory, "moduleName", null);
		assertThrows(RuntimeException.class, () -> errorLogFactory.constructErrorLogFactory());
	}

	@Test
	void getErrorLogInstance() {

		ReflectionTestUtils.setField(errorLogFactory, "moduleName", "Scheduler");
		Object target = new Object();
		Mockito.when(joinPoint.getTarget()).thenReturn(target);

		Throwable cause = new Throwable("Cause");
		WMException e = new WMException(505, "Message", cause);

		try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {

			securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
			securityUtils.when(SecurityUtilsV2::getCurrentAuthUser).thenReturn("user");

			ErrorLog errorLog = errorLogFactory.getErrorLogInstance(joinPoint, e);
			assertNotNull(errorLog);
		}
	}
}