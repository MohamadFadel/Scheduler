package com.wavemark.scheduler.logging.performancelogging.aspect;

import com.wavemark.scheduler.logging.performancelogging.service.PerformanceTimingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class PerformanceAspect {

	private final PerformanceTimingService performanceTimingService;

	@Around("@annotation(com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime)")
	public Object logPerformanceTime(ProceedingJoinPoint joinPoint) throws Throwable {

		try {
			MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

			// Get intercepted method details
			String className = methodSignature.getDeclaringType().getSimpleName();
			String methodName = methodSignature.getName();

			// Measure method execution time
			StopWatch stopWatch = new StopWatch(className + "->" + methodName);
			stopWatch.start(methodName);
			Object proceed = joinPoint.proceed();
			stopWatch.stop();

			//log execution time in database - PerformanceTiming table
			log.info("log execution time in database - PerformanceTiming table");
			performanceTimingService.logPerformanceTime(stopWatch, joinPoint);

			return proceed;
		}
		catch (Throwable e) {
			log.error("An unknown Exception has occurred", e);
			throw e;
		}
	}
}
