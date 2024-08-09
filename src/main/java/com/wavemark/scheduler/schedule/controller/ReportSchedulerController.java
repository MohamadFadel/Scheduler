package com.wavemark.scheduler.schedule.controller;

import java.util.List;

import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.logging.performancelogging.constant.LogType;
import com.wavemark.scheduler.schedule.constant.Success;
import com.wavemark.scheduler.schedule.dto.request.ReportSchedulerInput;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.ReportSchedulerService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportSchedulerController {

    private final ReportSchedulerService reportSchedulerService;

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad request (includes invalid input)")})
    @PostMapping("/reportScheduler")
    @LogPerformanceTime(logType = LogType.APP)
    public ResponseEntity<?> scheduleReportInstanceConfig(@Valid @RequestBody ReportSchedulerInput reportSchedulerInput)
            throws SchedulerException, CronExpressionException {
        TaskInput taskInput = reportSchedulerInput.getTaskInput();
        ReportInstanceConfig reportInstanceConfig = reportSchedulerInput.getReportInstanceConfig();

        reportSchedulerService.scheduleReportInstance(reportInstanceConfig, taskInput);
        return new ResponseEntity<>(new Success(), HttpStatus.OK);
    }

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad request (includes invalid input)")})
    @PostMapping("/reportScheduler/{reportId}")
    @LogPerformanceTime(logType = LogType.APP)
    public ResponseEntity<?> updateReportInstanceConfig(@Valid @PathVariable String reportId, @RequestBody Object []wrapper)
            throws SchedulerException, CronExpressionException, EntryNotFoundException {
        TaskInput taskInput = (TaskInput) wrapper[0];
        ReportInstanceConfig reportInstanceConfig = (ReportInstanceConfig) wrapper[1];

        reportSchedulerService.updateReportInstance(reportId, reportInstanceConfig, taskInput);
        return new ResponseEntity<>(new Success(), HttpStatus.OK);
    }

    @ApiResponses(value = {@ApiResponse(code = 400, message = "Bad request (includes invalid input)")})
    @DeleteMapping("/reportScheduler/{reportId}")
    @LogPerformanceTime(logType = LogType.APP)
    public ResponseEntity<?> deleteReportInstanceConfig(@Valid @PathVariable String reportId)
            throws SchedulerException, EntryNotFoundException {

        reportSchedulerService.deleteReportInstance(reportId);
        return new ResponseEntity<>(new Success(), HttpStatus.OK);
    }
}

