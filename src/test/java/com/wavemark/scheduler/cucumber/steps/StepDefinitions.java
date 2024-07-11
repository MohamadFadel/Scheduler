package com.wavemark.scheduler.cucumber.steps;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wavemark.scheduler.cucumber.CucumberSpringConfiguration;
import com.wavemark.scheduler.cucumber.api.SchedulerAPIClient;
import com.wavemark.scheduler.cucumber.api.SchedulerCleanClient;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskRepository;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@Slf4j
public class StepDefinitions extends CucumberSpringConfiguration {

	protected Map<String, Integer> tasksIdMap = new HashMap<>();
	protected List<TaskInput> tasks = new ArrayList<>();
	protected String error;

	protected final SchedulerAPIClient schedulerAPIClient;

	protected final SchedulerCleanClient schedulerCleanClient;

	protected final TaskRepository taskRepository;


	@Before
	public void tearUp() {
		this.tasksIdMap = new HashMap<>();
	}

	@After
	public void tearDown() throws SchedulerException, EntryNotFoundException {
		cleanUp();
		this.tasksIdMap.clear();
	}

	@Given("I have the following task(s)")
	public void departmentIsChosen(List<TaskInput> tasks) {
		this.tasks = tasks;
	}

	@When("I (re)schedule them/it with given Id(s)")
	public void scheduleTheFollowingTasksWithIds(List<String> taskIds) {

		try {
			for (int i = 0; i< tasks.size(); i++) {
				ResponseEntity<String> response = schedulerAPIClient.post(tasks.get(i), null);

				Map<String, String> headerMap = response.getHeaders().toSingleValueMap();
				this.tasksIdMap.put(taskIds.get(i), new Integer(headerMap.get("id")));

				log.info("Calling the schedule-task API for [" + tasks.get(i).getDescription() + "] returned: " + response.getStatusCode().name());
			}
		} catch (Exception e) {
			error = e.getMessage();
		}
	}

	@When("I (re)schedule them/it")
	public void scheduleTheFollowingTasks() {

		try {
			for (int i = 0; i< tasks.size(); i++) {
				ResponseEntity<String> response = schedulerAPIClient.post(tasks.get(i), null);

				Map<String, String> headerMap = response.getHeaders().toSingleValueMap();
				this.tasksIdMap.put(String.valueOf(i), new Integer(headerMap.get("id")));

				log.info("Calling the schedule-task API for [" + tasks.get(i).getDescription() + "] returned: " + response.getStatusCode().name());
			}
		} catch (Exception e) {
			error = e.getMessage();
		}
	}

	@Then("they/it should be created correctly")
	public void shouldBeCreatedCorrectly() {
		List<Task> taskList = getDBTasks();
		Assertions.assertEquals(tasks.size(), taskList.size());
	}

	@Then("an error should be returned")
	public void errorShouldBeReturned() {
		log.info("Error: " + error);

		assertNotNull(error);
	}

	@Then("an error should not be returned")
	public void errorShouldNotBeReturned() {
		log.info("There is no Error ");

		assertNull(error);
	}

	protected List<Task> getDBTasks() {
		Collection<Integer> idList = this.tasksIdMap.values();

		return idList.size()> 0 ? taskRepository.findByTaskIdIn(idList) : new ArrayList<>();
	}

	private void cleanUp() throws SchedulerException, EntryNotFoundException {
		List<Integer> tasksIds = new ArrayList<>(this.tasksIdMap.values());

		log.info("Calling deleteTasks function in SchedulerService");
		schedulerCleanClient.deleteTasks(tasksIds);

	}
}
