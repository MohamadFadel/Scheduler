package com.wavemark.scheduler.logging;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.Query;

@ExtendWith(MockitoExtension.class)
class LoggerHelperTest {

	@Mock
	private MethodSignature methodSignature;
	@Mock
	private ProceedingJoinPoint joinPoint;

	@Test
	void testGetQueryNameWithQueryAnnotation() throws NoSuchMethodException {

		Method mockMethod = this.getClass().getMethod("methodMockedWithQueryAnnotation");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		String[] args = {"id","name"};
		when(joinPoint.getArgs()).thenReturn(args);

		String queryName = LoggerHelper.getQueryName(joinPoint);
		assertNotNull(queryName);
	}

	@LogPerformanceTime
	@Query(value = "SELECT * FROM MOCK_VIEW")
	public void methodMockedWithQueryAnnotation() {
	}

	@Test
	void testGetParams() {
		String[] paramsName = {"id","name"};
		when(methodSignature.getParameterNames()).thenReturn(paramsName);

		String params = LoggerHelper.getParams(methodSignature);
		assertNotNull(params);
	}

}