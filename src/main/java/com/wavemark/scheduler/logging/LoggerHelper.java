package com.wavemark.scheduler.logging;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

public class LoggerHelper {

	public static String getQueryName(ProceedingJoinPoint joinPoint)
	{
		Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

		String queryName = method.getName() + getParamsValue(joinPoint);
		if (method.isAnnotationPresent(Query.class))
			queryName += " - Query: " + method.getAnnotation(Query.class).value();

		return queryName;
	}

	public static String getParamsValue(JoinPoint joinPoint)
	{
		if (Objects.nonNull(joinPoint.getArgs()))
			return " - Params are: " + Arrays.toString(joinPoint.getArgs());
		return "";
	}

	public static String getParams(MethodSignature methodSignature)
	{
		if (Objects.nonNull(methodSignature.getParameterNames()))
			return String.join(",", methodSignature.getParameterNames());
		return "";
	}

	public static String getAPIUrl(JoinPoint joinPoint)
	{
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();

		if (method.isAnnotationPresent(GetMapping.class)) {
			GetMapping getMapping = method.getAnnotation(GetMapping.class);
			return " - API URL is: " +
					(!Arrays.toString(getMapping.value()).equals("[]") ? Arrays.toString(getMapping.value()) : Arrays.toString(getMapping.path()));
		} else if (method.isAnnotationPresent(PostMapping.class)) {
			PostMapping postMapping = method.getAnnotation(PostMapping.class);
			return " - API URL is: " +
					(!Arrays.toString(postMapping.value()).equals("[]") ? Arrays.toString(postMapping.value()) : Arrays.toString(postMapping.path()));
		} else if (method.isAnnotationPresent(DeleteMapping.class)) {
			DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
			return " - API URL is: " +
					(!Arrays.toString(deleteMapping.value()).equals("[]") ? Arrays.toString(deleteMapping.value()) : Arrays.toString(deleteMapping.path()));
		}

		return "";
	}

}
