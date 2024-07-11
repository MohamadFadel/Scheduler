package com.wavemark.scheduler.logging.performancelogging.service;

import com.wavemark.scheduler.logging.performancelogging.entity.PerformanceTiming;
import com.wavemark.scheduler.logging.performancelogging.service.repository.PerformanceTimingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

@Service
@Slf4j
@RequiredArgsConstructor
public class PerformanceTimingService
{
    private final PerformanceTimingRepository performanceTimingRepository;
    private final PerformanceTimingFactory performanceTimingFactory;

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void logPerformanceTime(StopWatch stopWatch, ProceedingJoinPoint joinPoint) {

        log.info("insert Into Performance Timing Log");
        try {
            PerformanceTiming performanceTiming = performanceTimingFactory.getInstance(stopWatch, joinPoint);
            performanceTimingRepository.insertIntoPerformanceTiming(performanceTiming.getUserId(), performanceTiming.getSecurityId(),
                    performanceTiming.getRequestId(), performanceTiming.getActionName(),
                    performanceTiming.getDateTime(), performanceTiming.getLogType(),
                    performanceTiming.getLogContext(), performanceTiming.getLogDuration(),
                    performanceTiming.getBrowser(), performanceTiming.getSessionId());
        }
        catch (Exception e) {
            log.error("An unknown Exception has occurred", e);
        }
    }

}