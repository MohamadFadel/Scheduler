package com.wavemark.scheduler.schedule.controller;

import java.util.List;

import com.wavemark.scheduler.common.config.SwaggerConfiguration;
import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.logging.performancelogging.constant.LogType;
import com.wavemark.scheduler.schedule.dto.response.TaskRunLogResponse;
import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;
import com.wavemark.scheduler.schedule.service.ReportLogService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = SwaggerConfiguration.REPORT_LOG_TAG)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportLogController {

	private final ReportLogService reportLogService;

	@GetMapping("/tasks/runlog")
	@LogPerformanceTime(logType = LogType.APP)
	public ResponseEntity<List<TaskRunLogResponse>> tasksRunLog() {
		return new ResponseEntity<>(reportLogService.getTasksRunLogResponse(), HttpStatus.OK);
	}

	@GetMapping("/tasks/updatelog")
	@LogPerformanceTime(logType = LogType.APP)
	public ResponseEntity<List<TaskUpdateLogResponse>> tasksUpdateLog() {
		return new ResponseEntity<>(reportLogService.getTaskUpdateLogResponse(), HttpStatus.OK);
	}
}
