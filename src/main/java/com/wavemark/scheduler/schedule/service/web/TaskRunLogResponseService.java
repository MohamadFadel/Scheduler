//package com.wavemark.scheduler.schedule.service.web;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import com.wavemark.scheduler.cron.dto.CronDescription;
//import com.wavemark.scheduler.cron.exception.CronExpressionException;
//import com.wavemark.scheduler.cron.service.CronExpressionService;
//import com.wavemark.scheduler.schedule.domain.projection.SchdTaskRunLog;
//import com.wavemark.scheduler.schedule.dto.response.TaskRunLogResponse;
//import com.wavemark.scheduler.schedule.service.core.SchdTaskRunLogService;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class TaskRunLogResponseService {
//
//	private final SchdTaskRunLogService schdTaskRunLogService;
//	private final CronExpressionService cronExpressionService;
//
//	public List<TaskRunLogResponse> buildTasksRunLogResponse() {
//
//		List<SchdTaskRunLog> schdTaskRunLogList = schdTaskRunLogService.getSchdTaskRunLog();
//
//		return schdTaskRunLogList.stream().map(schdTaskRunLog -> {
//			try {
//				return buildTaskRunLogResponse(schdTaskRunLog);
//			} catch (CronExpressionException e) {
//				throw new RuntimeException(e);
//			}
//		}).collect(Collectors.toList());
//	}
//
//	protected TaskRunLogResponse buildTaskRunLogResponse(SchdTaskRunLog schdTaskRunLog) throws CronExpressionException {
//
//		log.info("Building TaskRunLogResponse Object");
//
//		TaskRunLogResponse taskRunLogResponse = new TaskRunLogResponse();
//		taskRunLogResponse.setTaskId(schdTaskRunLog.getTaskId());
//		taskRunLogResponse.setTaskType(schdTaskRunLog.getTaskType());
//		taskRunLogResponse.setTaskDescription(schdTaskRunLog.getDescription());
//
//		String cronExpressionString = schdTaskRunLog.getCronExpression();
//		CronDescription cronDescription = cronExpressionService.reverseCronExpression(cronExpressionString, schdTaskRunLog.getTimeZone().getID());
//		taskRunLogResponse.setTaskFrequency(cronDescription.getFrequency().getCronExpression());
//
//		taskRunLogResponse.setStartDateTime(schdTaskRunLog.getRunStartDate());
//		taskRunLogResponse.setEndDateTime(schdTaskRunLog.getRunEndDate());
//		taskRunLogResponse.setStatus(schdTaskRunLog.getRunState());
//		taskRunLogResponse.setResponseMessage(schdTaskRunLog.getResponseMessage());
//
//		return taskRunLogResponse;
//	}
//}
