package com.wavemark.scheduler.logging.performancelogging.constant;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RUNTIME)
public @interface LogPerformanceTime {
	LogType logType() default LogType.DB;
	String queryName() default "";
}
