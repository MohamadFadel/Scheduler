package com.wavemark.scheduler.schedule.controller;

import java.text.ParseException;

import com.wavemark.scheduler.common.config.SwaggerConfiguration;
import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.logging.performancelogging.constant.LogType;
import com.wavemark.scheduler.schedule.constant.Success;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.TaskStateService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = SwaggerConfiguration.TASK_STATE_TAG)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskStateController {

    private final TaskStateService taskStateService;

    @PostMapping("/task/{id}/pause")
    @LogPerformanceTime(logType = LogType.APP)
    public ResponseEntity<?> pauseTask(@PathVariable Integer id) throws SchedulerException, EntryNotFoundException {

        taskStateService.pauseTask(id);
        return new ResponseEntity<>(new Success(), HttpStatus.OK);
    }

    @PostMapping("/task/{id}/resume")
    @LogPerformanceTime(logType = LogType.APP)
    public ResponseEntity<?> resumeTask(@PathVariable Integer id) throws SchedulerException, EntryNotFoundException, ParseException {

        taskStateService.resumeTask(id);
        return new ResponseEntity<>(new Success(), HttpStatus.OK);
    }

}
