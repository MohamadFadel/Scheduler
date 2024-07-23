package com.wavemark.scheduler.fire.httpinvoker.property;

import static com.wavemark.scheduler.common.constant.DataMapProperty.BODY_PARAM;
import static com.wavemark.scheduler.common.constant.DataMapProperty.ENDPOINT_ID;
import static com.wavemark.scheduler.common.constant.DataMapProperty.ENDPOINT_NAME;
import static com.wavemark.scheduler.common.constant.DataMapProperty.NAME;

import com.wavemark.scheduler.schedule.domain.entity.TaskType;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskTypeRepository;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HttpPropertyService {

    @Value("${application.host}")
    private String host;

    private final TaskTypeRepository taskTypeRepository;

    public HttpProperty fetchTaskRequestProperty(JobDataMap jobDataMap, String authToken) throws EntryNotFoundException {

        String url = getTaskUrlInstance(jobDataMap.getString(NAME)) + "?access_token=" + authToken;

        return HttpProperty.builder()
                .taskName(jobDataMap.getString(NAME))
                .endpointName(jobDataMap.getString(ENDPOINT_NAME))
                .url(url)
                .bodyParam(jobDataMap.getString(BODY_PARAM))
                .build();
    }

    protected String getTaskUrlInstance(String taskName) {
        String type = StringUtils.substringBefore(taskName, '_');

        TaskType taskType = taskTypeRepository.findByTaskType(type);
        return host + taskType.getApiEndpoint();
    }

    public HttpProperty fetchAuthTokenRequestProperty(JobDataMap jobDataMap) {

        String url = host + "warden/oauth/token?username=" + jobDataMap.getString(ENDPOINT_ID) + "&grant_type=password";
        return HttpProperty.builder()
                .taskName("oauth")
                .url(url)
                .build();
    }

}
