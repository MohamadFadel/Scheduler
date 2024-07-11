package com.wavemark.scheduler.logging.performancelogging.service;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.wavemark.scheduler.logging.LoggerHelper;
import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.logging.performancelogging.entity.PerformanceTiming;
import com.wavemark.scheduler.logging.performancelogging.util.UserContextUtils;
import com.wavemark.scheduler.logging.performancelogging.constant.LogType;

import com.cardinalhealth.service.support.context.UserContextContextHolder;
import com.cardinalhealth.service.support.models.TypedValue;
import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Component
public class PerformanceTimingFactory
{
    @Value("${spring.application.name}")
    private String moduleName;

    @PostConstruct
    protected void constructPerformanceTimingFactory()
    {
        if(StringUtils.isEmpty(moduleName))
            throw new RuntimeException("Module Name must be defined in the application.");
    }

    public PerformanceTiming getInstance(StopWatch stopWatch, ProceedingJoinPoint joinPoint)
    {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        LogType logType = methodSignature.getMethod().getAnnotation(LogPerformanceTime.class).logType();

        if (logType.equals(LogType.DB))
            return initializeDBPerformanceTiming(stopWatch, joinPoint);
        return initializeAppPerformanceTiming(stopWatch, joinPoint);
    }

    protected PerformanceTiming initializeDBPerformanceTiming(StopWatch stopWatch, ProceedingJoinPoint joinPoint)
    {
        log.info("Initializing DB Log");
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String queryName = method.getAnnotation(LogPerformanceTime.class).queryName();

        if (queryName.isEmpty())
            queryName = LoggerHelper.getQueryName(joinPoint);

        String userContextString = "DB: " + queryName;
        if (Objects.nonNull(UserContextContextHolder.getUserContext())) {
            List<TypedValue> userContext = UserContextUtils.excludePricing(UserContextContextHolder.getUserContext().getContext());
            userContextString += " - " + userContext.toString();
            if (userContextString.length() > 2000)
                userContextString = userContextString.substring(0, 1995) + "...";
        }

        PerformanceTiming performanceTimingDB = initializeCommonAttributes(stopWatch, joinPoint);
        performanceTimingDB.setLogContext(userContextString);
        performanceTimingDB.setLogType(String.valueOf(LogType.DB));

        return performanceTimingDB;
    }

    protected PerformanceTiming initializeAppPerformanceTiming(StopWatch stopWatch, ProceedingJoinPoint joinPoint)
    {
        log.info("Initializing APP Log");
        String logContextString = "DEFAULT";

        String apiUrl = LoggerHelper.getAPIUrl(joinPoint) + LoggerHelper.getParamsValue(joinPoint);

        PerformanceTiming performanceTimingApp = initializeCommonAttributes(stopWatch, joinPoint);
        performanceTimingApp.setLogContext(apiUrl.isEmpty() ? logContextString : apiUrl);
        performanceTimingApp.setLogType(String.valueOf(LogType.APP));

        return performanceTimingApp;
    }

    protected PerformanceTiming initializeCommonAttributes(StopWatch stopWatch, ProceedingJoinPoint joinPoint)
    {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        PerformanceTiming performanceTiming = new PerformanceTiming();
        performanceTiming.setRequestId("UNKNOWN");
        performanceTiming.setUserId(SecurityUtilsV2.getCurrentAuthUser());
        performanceTiming.setSecurityId(SecurityUtilsV2.getCurrentAuthDepartment());
        performanceTiming.setSessionId(null);
        performanceTiming.setActionName(moduleName + " - " + methodSignature.getDeclaringType().getSimpleName() + " - " + methodSignature.getName() + "(" + LoggerHelper.getParams(methodSignature) + ")");
        performanceTiming.setDateTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
        performanceTiming.setLogDuration(stopWatch.getTotalTimeMillis());
        performanceTiming.setBrowser("N/A");
        return performanceTiming;
    }

}
