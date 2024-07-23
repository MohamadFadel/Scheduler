package com.wavemark.scheduler.common.aspect;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggerAspect {

    private final Environment env;
    public static final String METHOD_INVOKED = "Method invoked: ";
    public static final String METHOD_ENDED = "Method ended: ";

    /**
     * Pointcut that matches all repositories, services and Web REST endpoints.
     */
    @Pointcut("within(@org.springframework.stereotype.Repository *)" + " || within(@org.springframework.stereotype.Service *)" +
            " || within(@org.springframework.stereotype.Component *)" + " ||  within(@org.springframework.web.bind.annotation.RestController *)")
    public void springBeanPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    /**
     * Advice that logs methods throwing exceptions.
     *
     * @param joinPoint join point for advice
     * @param e         exception
     */
    @AfterThrowing(pointcut = "springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            log.error("Exception in {} - {}() with cause = '{}' and exception = '{}'",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);

        } else {
            log.error("Exception in {} - {}() with cause = {}",
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                    e.getCause() != null ? e.getCause() : "NULL");
        }
    }

    /**
     * Advice that logs when a method is entered and exited.
     *
     * @param joinPoint join point for advice
     * @return result
     * @throws Throwable throws IllegalArgumentException
     */
    @Around("springBeanPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        if (log.isDebugEnabled()) {
            log.debug(METHOD_INVOKED + "{} - {}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        }
        try {
            long time = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            if (log.isDebugEnabled()) {
                log.debug(METHOD_ENDED + "{} - {}() with result = {} - in total time = {} ms", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(), result, System.currentTimeMillis() - time);
            }
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument: {} in {} - {}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
	
}