package com.wavemark.scheduler.common.aspect;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wavemark.scheduler.logging.WMException;
import com.wavemark.scheduler.logging.aspect.LoggerAspect;
import com.wavemark.scheduler.logging.errorlog.service.ErrorLogService;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@ExtendWith(MockitoExtension.class)
class LoggerAspectTest {

    @Mock
    private Environment env;
    @Mock
    private ErrorLogService errorLogService;
    @Mock
    private Logger log;

    @InjectMocks
    private LoggerAspect loggerAspect;

    @Test
    void testSpringBeanPointcut() {
        loggerAspect.springBeanPointcut();
    }

    @Test
    void testLogAfterThrowingWhenDevProfile() {
        when(env.acceptsProfiles(Profiles.of("dev"))).thenReturn(true);

        JoinPoint joinPoint = mock(JoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(methodSignature.getDeclaringTypeName()).thenReturn("declaringTypeName");
        when(methodSignature.getName()).thenReturn("MethodName");

        when(joinPoint.getSignature()).thenReturn(methodSignature);

        Throwable cause = new Throwable("Cause");
        WMException e = new WMException(501, "Message", cause);

        doNothing().when(errorLogService).logErrorLog(any(), any());

        loggerAspect.logAfterThrowing(joinPoint, e);

        verify(log).error("Exception in {} - {}() with cause = '{}' and exception = '{}'", "declaringTypeName", "MethodName", cause, "Message", e);

    }

    @Test
    void testLogAfterThrowingWhenNotDevProfile() {
        when(env.acceptsProfiles(Profiles.of("dev"))).thenReturn(false);

        JoinPoint joinPoint = mock(JoinPoint.class);
        MethodSignature methodSignature = mock(MethodSignature.class);

        when(methodSignature.getDeclaringTypeName()).thenReturn("declaringTypeName");
        when(methodSignature.getName()).thenReturn("MethodName");

        when(joinPoint.getSignature()).thenReturn(methodSignature);

        Throwable cause = new Throwable("Cause");
        Exception e = new Exception("Message", cause);

        doNothing().when(errorLogService).logErrorLog(any(), any());

        loggerAspect.logAfterThrowing(joinPoint, e);

        verify(log).error("Exception in {} - {}() with cause = {}", "declaringTypeName", "MethodName", cause);
    }

    @Test
    void testLogAroundWhenDebugDisabled() throws Throwable {
        lenient().when(log.isDebugEnabled()).thenReturn(false);

        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        when(proceedingJoinPoint.proceed()).thenReturn("ProceedCalled");

        Object proceed = loggerAspect.logAround(proceedingJoinPoint);

        Assertions.assertEquals("ProceedCalled", proceed.toString());
    }

    @Test
    void testLogAroundWhenDebugEnabled() throws Throwable {
        when(log.isDebugEnabled()).thenReturn(true);

        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getName()).thenReturn("MethodName");

        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(proceedingJoinPoint.proceed()).thenReturn("ProceedCalled");

        Object proceed = loggerAspect.logAround(proceedingJoinPoint);

        Assertions.assertEquals("ProceedCalled", proceed.toString());
    }

    @Test
    void testLogAroundWhenDebugEnabledCatch() throws Throwable {
        when(log.isDebugEnabled()).thenReturn(false);

        MethodSignature methodSignature = mock(MethodSignature.class);
        when(methodSignature.getName()).thenReturn("MethodName");

        ProceedingJoinPoint proceedingJoinPoint = mock(ProceedingJoinPoint.class);
        when(proceedingJoinPoint.getSignature()).thenReturn(methodSignature);
        when(proceedingJoinPoint.proceed()).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> loggerAspect.logAround(proceedingJoinPoint));
    }

}