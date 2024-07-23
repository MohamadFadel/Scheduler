//package com.wavemark.scheduler.schedule.service.core;
//
//import java.util.List;
//
//import com.wavemark.scheduler.schedule.domain.projection.SchdTaskRunLog;
//import com.wavemark.scheduler.schedule.repository.SchdTaskRunLogRepository;
//
//import com.cardinalhealth.service.support.security.SecurityUtils;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class SchdTaskRunLogService {
//
//	private final SchdTaskRunLogRepository schdTaskRunLogRepository;
//
//	public List<SchdTaskRunLog> getSchdTaskRunLog()
//	{
//		return schdTaskRunLogRepository.getSchdTaskRunLog(SecurityUtils.getCurrentAuthDepartment());
//	}
//}
