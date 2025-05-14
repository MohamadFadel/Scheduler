package com.wavemark.scheduler.schedule.controller;

import com.wavemark.scheduler.fire.task.ScheduledTask;

import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Profile({"local", "dev"})
public class FireController {

	private final ScheduledTask scheduledTask;

	@GetMapping(path = "/tasks/fire", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> fire(@RequestParam String jobName, @RequestParam String bodyParam)
			throws JobExecutionException {

		scheduledTask.fireTask(jobName, bodyParam);
		return new ResponseEntity<>("done", HttpStatus.OK);
	}

	@GetMapping(path = "/tasks/fire", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> fire(@RequestParam String jobName, @RequestParam String bodyParam)
			throws JobExecutionException {

		scheduledTask.fireTask(jobName, bodyParam);
		return new ResponseEntity<>("done", HttpStatus.OK);
	}
}
