package com.wavemark.scheduler.schedule.controller;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.logging.performancelogging.constant.LogType;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@Api(tags = SwaggerConfiguration.REPORT_TAG)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

	private final ReportService reportService;

	@GetMapping("/ping")
//	@LogPerformanceTime(logType = LogType.APP)
	public ResponseEntity<String> ping() {
		return new ResponseEntity<>("Pong", HttpStatus.OK);
	}

	@GetMapping("/tasks")
	@LogPerformanceTime(logType = LogType.APP)
	public ResponseEntity<List<TaskResponse>> tasks() throws CronExpressionException, EntryNotFoundException {
		return new ResponseEntity<>(reportService.getTasks(), HttpStatus.OK);
	}
}