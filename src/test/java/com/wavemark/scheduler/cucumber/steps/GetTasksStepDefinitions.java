package com.wavemark.scheduler.cucumber.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.wavemark.scheduler.cucumber.CucumberSpringConfiguration;
import com.wavemark.scheduler.schedule.dto.response.TaskResponse;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GetTasksStepDefinitions extends CucumberSpringConfiguration {

    private final StepDefinitions stepDefinitions;

    private List<TaskResponse> taskResponseList = new ArrayList<>();


    @When("I call get tasks api")
    public void callGetTasksApi() {
        taskResponseList = stepDefinitions.schedulerAPIClient.getTasks();

        taskResponseList = taskResponseList.stream()
                .filter(task -> stepDefinitions.tasksIdMap.containsValue(task.getTaskId()))
                .collect(Collectors.toList());
    }

    @Then("{int} task(s) should be returned")
    public void tasksShouldBeReturned(int size) {
        assertEquals(size, taskResponseList.size());
    }

}