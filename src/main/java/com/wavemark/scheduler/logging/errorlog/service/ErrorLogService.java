package com.wavemark.scheduler.logging.errorlog.service;

import java.sql.Timestamp;
import java.util.Calendar;

import com.wavemark.scheduler.logging.errorlog.entity.ErrorLog;
import com.wavemark.scheduler.logging.errorlog.service.repository.ErrorLogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ErrorLogService {

	private final ErrorLogFactory errorLogFactory;
	private final ErrorLogRepository errorLogRepository;

	@Transactional(propagation= Propagation.REQUIRES_NEW)
	public void logErrorLog(JoinPoint joinPoint, Throwable e)
	{
		log.info("insert Into Error Log");
		try {
			ErrorLog errorLog = errorLogFactory.getErrorLogInstance(joinPoint, e);
			errorLogRepository.insertIntoErrorLog(new Timestamp(Calendar.getInstance().getTime().getTime()), errorLog.getWmPackage(),
					errorLog.getSource(), errorLog.getMessage(), errorLog.getErrorCode(), errorLog.getStackMessage(), errorLog.getSeverity(),
					errorLog.getUsername(), errorLog.getDeviceId());
		}
		catch (Exception ex) {
			log.error("An unknown Exception has occurred", ex);
		}
	}
}
