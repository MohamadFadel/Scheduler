package com.wavemark.scheduler.fire.task.service;

import com.wavemark.scheduler.fire.http.client.HttpClient;
import com.wavemark.scheduler.fire.http.property.HttpProperty;
import com.wavemark.scheduler.fire.http.response.ResponseHandler;
import com.wavemark.scheduler.schedule.domain.entity.TaskType;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskTypeRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.wavemark.scheduler.common.constant.DataMapProperty.*;

@Service
@RequiredArgsConstructor
public class ScheduledTaskService {

    private final TaskTypeRepository taskTypeRepository;
    private final ResponseHandler responseHandler;

    @Value("${application.host}")
    private String host;

    public HttpProperty fetchTaskRequestProperty(JobDataMap jobDataMap, String authToken) throws EntryNotFoundException {
        String url = getTaskUrlInstance(jobDataMap.getString(NAME)) + "?access_token=" + authToken;

        return HttpProperty.builder()
                .taskName(jobDataMap.getString(NAME))
                .endpointName(jobDataMap.getString(ENDPOINT_NAME))
                .url(url)
                .bodyParam(jobDataMap.getString(BODY_PARAM))
                .build();
    }

    public void postTask(HttpProperty httpProperty) throws SchedulerException, EntryNotFoundException, IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(httpProperty.getBodyParam(), mediaType);

        Request request = new Request.Builder()
                .url(httpProperty.getUrl())
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        long time = System.currentTimeMillis();
        try (Response response = HttpClient.getInstance().getClient().newCall(request).execute()) {
            responseHandler.handle(httpProperty, response, System.currentTimeMillis() - time);
        } catch (Exception e) {
            if (!e.getMessage().startsWith("[FIRED]"))
                responseHandler.handleError(httpProperty, e, System.currentTimeMillis() - time);
            throw new JobExecutionException(e);
        }
    }

    private String getTaskUrlInstance(String taskName) {
        String type = StringUtils.substringBefore(taskName, '_');

        TaskType taskType = taskTypeRepository.findByTaskType(type);
        return host + taskType.getApiEndpoint();
    }

}
