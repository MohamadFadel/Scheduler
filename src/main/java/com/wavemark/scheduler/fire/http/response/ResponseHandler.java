package com.wavemark.scheduler.fire.http.response;

import java.io.IOException;

import com.wavemark.scheduler.fire.http.property.HttpProperty;
import com.wavemark.scheduler.fire.http.response.email.ResponseEmailService;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.core.TaskService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.text.StringEscapeUtils;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseHandler {

    private final ResponseLogService responseLogService;
    private final TaskService taskService;
    private final ResponseEmailService responseEmailService;

    public void handle(HttpProperty httpProperty, Response response, long time)
            throws SchedulerException, IOException, EntryNotFoundException {

        HttpResponse httpResponse = ResponseFactory.initializeHttpResponse(response);
        Task dbTask = taskService.findActiveTaskByTaskName(httpProperty.getTaskName());

        responseLogService.logResponse(dbTask, httpResponse, time);
        responseEmailService.sendEmailMessage(dbTask, httpResponse, httpProperty.getEndpointName());

        if (!httpResponse.isSuccess() && httpResponse.getCode() != 412) {
            log.info("Api call " + StringEscapeUtils.escapeJava(httpResponse.getUrl())
                    + " Failed with response code "
                    + StringEscapeUtils.escapeJava(String.valueOf(httpResponse.getCode())));

            throw new JobExecutionException("[FIRED] - " + httpResponse.getCause());
        }
    }

    public void handleError(HttpProperty httpProperty, Exception exception, long time)
            throws SchedulerException, EntryNotFoundException {

        HttpResponse httpResponse = ResponseFactory.initializeHttpResponse(exception, httpProperty.getUrl());
        log.info("Api call " + StringEscapeUtils.escapeJava(httpProperty.getUrl()) + " Failed with response code "
                + StringEscapeUtils.escapeJava(String.valueOf(httpResponse.getCode())));
        Task dbTask = taskService.findActiveTaskByTaskName(httpProperty.getTaskName());

        responseLogService.logResponseError(dbTask, httpResponse, time);
        responseEmailService.sendEmailMessage(dbTask, httpResponse, httpProperty.getEndpointName());
    }

}
