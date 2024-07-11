package com.wavemark.scheduler.logging.performancelogging.aspect;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import com.wavemark.scheduler.logging.performancelogging.aspect.PerformanceAspect;
import com.wavemark.scheduler.logging.performancelogging.service.PerformanceTimingService;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PerformanceAspectTest {

	@Mock
	private PerformanceTimingService performanceTimingService;

	@InjectMocks
	private PerformanceAspect performanceAspect;


	@Test
	void testLogExecutionTime() throws Throwable {

		doNothing().when(performanceTimingService).logPerformanceTime(any(), any());
		MethodSignature methodSignature = mock(MethodSignature.class);
		Method method = PerformanceAspect.class.getDeclaredMethod("logPerformanceTime", ProceedingJoinPoint.class);

		when(methodSignature.getDeclaringType()).thenReturn(method.getDeclaringClass());
		when(methodSignature.getName()).thenReturn("MethodName");

		ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
		when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
		when(proceedingJoinPoint.proceed()).thenReturn("ProceedCalled");

		Object proceed = performanceAspect.logPerformanceTime(proceedingJoinPoint);

		assertNotNull(proceed);
		Assertions.assertEquals("ProceedCalled", proceed.toString());
	}

	@Test
	void testLogExecutionTimeDoesntThrowException() throws Throwable {

		MethodSignature methodSignature = mock(MethodSignature.class);
		Method method = PerformanceAspect.class.getDeclaredMethod("logPerformanceTime", ProceedingJoinPoint.class);

		when(methodSignature.getDeclaringType()).thenReturn(method.getDeclaringClass());
		when(methodSignature.getName()).thenReturn("MethodName");

		ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
		when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);

		doThrow(new Throwable()).when(proceedingJoinPoint).proceed();

		assertThrows(Throwable.class, () -> performanceAspect.logPerformanceTime(proceedingJoinPoint));
	}
}