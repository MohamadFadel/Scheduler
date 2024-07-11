package com.wavemark.scheduler.logging.errorlog.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import javax.annotation.PostConstruct;

import com.wavemark.scheduler.logging.WMException;
import com.wavemark.scheduler.logging.errorlog.entity.ErrorLog;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorLogFactory {

	@Value("${spring.application.name}")
	private String moduleName;

	@PostConstruct
	protected void constructErrorLogFactory()
	{
		if(StringUtils.isEmpty(moduleName))
			throw new RuntimeException("Module Name must be defined in the application.");
	}

	public ErrorLog getErrorLogInstance(JoinPoint joinPoint, Throwable e)
	{
		int errorCode = 1007;
		if (e instanceof WMException)
			errorCode = ((WMException) e).getErrorCode();

		ErrorLog errorLog = new ErrorLog();
		errorLog.setUsername(SecurityUtilsV2.getCurrentAuthUser());
//		errorLog.setDeviceId(SecurityUtilsV2.getCurrentAuthDepartment());
		errorLog.setErrorCode(String.valueOf(errorCode));
		errorLog.setMessage(Objects.nonNull(e.getCause()) ? e.getCause().toString() : "");
		errorLog.setSource(joinPoint.getTarget().getClass().getSimpleName());
		errorLog.setWmPackage(moduleName);
		errorLog.setSeverity("Error");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String stackMessage = sw.toString();
		errorLog.setStackMessage(stackMessage.getBytes());

		return errorLog;
	}
}
