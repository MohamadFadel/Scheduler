package com.wavemark.scheduler.schedule.controller;

import java.text.ParseException;

import javax.validation.Valid;

import com.wavemark.scheduler.common.config.SwaggerConfiguration;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.logging.performancelogging.constant.LogType;
import com.wavemark.scheduler.schedule.constant.Success;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.SchedulerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = SwaggerConfiguration.SCHEDULER_TAG)
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SchedulerController {

    private final SchedulerService schedulerService;

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad request (includes invalid input)")})
    @PostMapping("/task")
    @LogPerformanceTime(logType = LogType.APP)
    public ResponseEntity<?> createTask(@Valid @RequestBody TaskInput taskInput)
            throws SchedulerException, CronExpressionException, ParseException, EntryNotFoundException {

        Integer createdTaskId = schedulerService.scheduleTask(taskInput);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("id", createdTaskId.toString());

        return new ResponseEntity<>(new Success(), headers, HttpStatus.OK);
    }

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad request (includes invalid input)")})
    @PostMapping("/task/{id}")
    @LogPerformanceTime(logType = LogType.APP)
    public ResponseEntity<?> updateTask(@Valid @RequestBody TaskInput taskInput, @PathVariable Integer id)
            throws SchedulerException, CronExpressionException, ParseException, EntryNotFoundException {

        schedulerService.updateScheduledTask(id, taskInput);
        return new ResponseEntity<>(new Success(), HttpStatus.OK);
    }

    @DeleteMapping("/task/{id}")
    @LogPerformanceTime(logType = LogType.APP)
    public ResponseEntity<?> deleteTask(@PathVariable Integer id)
            throws SchedulerException, EntryNotFoundException {

        schedulerService.deleteTask(id);
        return new ResponseEntity<>(new Success(), HttpStatus.OK);
    }

}