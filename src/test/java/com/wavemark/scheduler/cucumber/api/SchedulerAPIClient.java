package com.wavemark.scheduler.cucumber.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wavemark.scheduler.cucumber.authentication.AuthenticationService;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.wavemark.scheduler.cucumber.api.APIEndpoint.*;
import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
public class SchedulerAPIClient {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate;

    private final String accessToken;

    public SchedulerAPIClient(RestTemplate restTemplate, AuthenticationService authenticationService) throws JsonProcessingException {
        this.restTemplate = restTemplate;
        accessToken = authenticationService.getWebAppToken();
    }

    private String getAPIEndpoint(String apiEndpoint) {
        return SERVER_URL + ":" + port + apiEndpoint + "?access_token=" + accessToken;
    }

    public List<TaskResponse> getTasks() {
        return Arrays.asList(Objects.requireNonNull(restTemplate.getForEntity(getAPIEndpoint(GET_TASKS_ENDPOINT), TaskResponse[].class).getBody()));
    }

    public ResponseEntity<String> post(TaskInput job, Integer id) {
        if (id != null)
            return restTemplate.postForEntity(getAPIEndpoint(POST_TASK_ENDPOINT + "/" + id), job, String.class);

        return restTemplate.postForEntity(getAPIEndpoint(POST_TASK_ENDPOINT), job, String.class);
    }

    public HttpStatus deleteTask(Integer id) {
        return restTemplate.exchange(getAPIEndpoint(DELETE_TASK_ENDPOINT + "/" + id), HttpMethod.DELETE, null, String.class).getStatusCode();
    }

    public void pauseTask(Integer id) {
        restTemplate.postForEntity(getAPIEndpoint(POST_TASK_ENDPOINT + "/" + id + PAUSE_TASK_ENDPOINT), null, String.class);
    }

    public void resumeTask(Integer id) {
        restTemplate.postForEntity(getAPIEndpoint(POST_TASK_ENDPOINT + "/" + id + RESUME_TASK_ENDPOINT), null, String.class);
    }

}
