package com.wavemark.scheduler.logging.performancelogging.service.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

import com.wavemark.scheduler.logging.performancelogging.entity.PerformanceTiming;
import com.wavemark.scheduler.logging.performancelogging.service.PerformanceTimingFactory;
import com.wavemark.scheduler.logging.performancelogging.service.PerformanceTimingService;
import com.wavemark.scheduler.logging.performancelogging.service.repository.PerformanceTimingRepository;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

@ExtendWith(MockitoExtension.class)
class PerformanceTimingServiceTest {

	@Mock
	private PerformanceTimingRepository performanceTimingRepository;
	@Mock
	private PerformanceTimingFactory performanceTimingFactory;
	@Mock
	private StopWatch stopWatch;
	@Mock
	private ProceedingJoinPoint joinPoint;

	@InjectMocks
	private PerformanceTimingService performanceTimingService;

	@Test
	void testLogExecutionTime() throws Exception {
		Mockito.when(performanceTimingFactory.getInstance(any(), any())).thenReturn(new PerformanceTiming());
		doNothing().when(performanceTimingRepository).insertIntoPerformanceTiming(any(), any(), any(), any(),
				any(), any(), any(), anyLong(), any(), any());

		performanceTimingService.logPerformanceTime(stopWatch, joinPoint);

		Mockito.verify(performanceTimingRepository, times(1)).insertIntoPerformanceTiming(any(), any(), any(), any(),
				any(), any(), any(), any(), any(), any());
	}

	@Test
	void testLogExecutionTimeDoesntThrowAnException() throws Exception {
		Mockito.when(performanceTimingFactory.getInstance(any(), any())).thenReturn(new PerformanceTiming());
		Mockito.doThrow(new Exception()).when(performanceTimingRepository).insertIntoPerformanceTiming(any(), any(), any(), any(),
				any(), any(), any(), anyLong(), any(), any());

		assertDoesNotThrow(() -> performanceTimingService.logPerformanceTime(stopWatch, joinPoint));
	}
}