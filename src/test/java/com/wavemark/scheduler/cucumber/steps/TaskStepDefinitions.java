package com.wavemark.scheduler.cucumber.steps;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.wavemark.scheduler.cucumber.CucumberSpringConfiguration;
import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.domain.entity.Task;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@RequiredArgsConstructor
public class TaskStepDefinitions extends CucumberSpringConfiguration {

	private final StepDefinitions stepDefinitions;

	private List<String> modifiedTaskIds;


	@When("I update them/it with given Id(s)")
	public void updateTheFollowingTasks(List<String> ids) {
		this.modifiedTaskIds = ids;
		try {
			for (int i = 0; i< this.stepDefinitions.tasks.size(); i++) {
				int taskId = stepDefinitions.tasksIdMap.get(ids.get(i));
				HttpStatus status = this.stepDefinitions.schedulerAPIClient.post(this.stepDefinitions.tasks.get(i), taskId).getStatusCode();

				log.info("Calling the schedule-task API to update task with id: [" + taskId + "] for [" + this.stepDefinitions.tasks.get(i).getDescription() + "] returned: " + status.name());
			}
		} catch (Exception e) {
			this.stepDefinitions.error = e.getMessage();
		}
	}

	@When("I delete a task(s) with given Id(s)")
	public void deleteTheFollowingTasks(List<String> ids) {
		this.modifiedTaskIds = ids;
		try {
			for (String id : ids) {
				int taskId = stepDefinitions.tasksIdMap.get(id);
				HttpStatus status = this.stepDefinitions.schedulerAPIClient.deleteTask(taskId);

				log.info("Calling the delete API to delete task with id: [" + taskId + "] returned: " + status.name());
			}
		} catch (Exception e) {
			this.stepDefinitions.error = e.getMessage();
		}
	}

	@When("I pause a task(s) with given Id(s)")
	public void pauseTheFollowingTasks(List<String> ids) {
		this.modifiedTaskIds = ids;
		try {
			for (String id : ids) {
				int taskId = stepDefinitions.tasksIdMap.get(id);
				this.stepDefinitions.schedulerAPIClient.pauseTask(taskId);

				log.info("Calling the pause API to pause task with id: [" + taskId + "]");
			}
		} catch (Exception e) {
			this.stepDefinitions.error = e.getMessage();
		}
	}

	@When("I resume a task(s) with given Id(s)")
	public void resumeTheFollowingTasks(List<String> ids) {
		this.modifiedTaskIds = ids;
		try {
			for (String id : ids) {
				int taskId = stepDefinitions.tasksIdMap.get(id);
				this.stepDefinitions.schedulerAPIClient.resumeTask(taskId);

				log.info("Calling the resume API to resume task with id: [" + taskId + "]");
			}
		} catch (Exception e) {
			this.stepDefinitions.error = e.getMessage();
		}
	}

	@Then("they/it should be updated correctly")
	public void shouldBeUpdated() {
		List<Task> taskList = this.stepDefinitions.getDBTasks();

		for (int i = 0; i< this.stepDefinitions.tasks.size(); i++) {
			int taskId = stepDefinitions.tasksIdMap.get(this.modifiedTaskIds.get(i));
			Task dbTask = taskList.stream().filter(task -> task.getTaskId().equals(taskId)).findFirst().orElse(null);

			assertNotNull(dbTask);
			assertEquals(dbTask.getDescription(), this.stepDefinitions.tasks.get(i).getDescription());
			assertNotNull(dbTask.getLastUpdatedBy());
			assertNotNull(dbTask.getLastUpdatedOn());
		}
	}

	@Then("they/it should be deleted correctly")
	public void shouldBeDeleted() {
		List<Task> taskList = this.stepDefinitions.getDBTasks();

		for (String id : this.modifiedTaskIds) {
			int taskId = stepDefinitions.tasksIdMap.get(id);
			Task dbTask = taskList.stream().filter(task -> task.getTaskId().equals(taskId)).findFirst().orElse(null);

			assertNotNull(dbTask);
			assertEquals(String.valueOf(State.DEACTIVATED), dbTask.getTaskStatus());
		}
	}

	@Then("they/it should be paused correctly")
	public void shouldBePaused() {
		List<Task> taskList = this.stepDefinitions.getDBTasks();

		for (String id : this.modifiedTaskIds) {
			int taskId = stepDefinitions.tasksIdMap.get(id);
			Task dbTask = taskList.stream().filter(task -> task.getTaskId().equals(taskId)).findFirst().orElse(null);

			assertNotNull(dbTask);
			assertEquals(String.valueOf(State.PAUSED), dbTask.getTaskStatus());
		}
	}

	@Then("they/it should be resumed correctly")
	public void shouldBeResumed() {
		List<Task> taskList = this.stepDefinitions.getDBTasks();

		for (String id : this.modifiedTaskIds) {
			int taskId = stepDefinitions.tasksIdMap.get(id);
			Task dbTask = taskList.stream().filter(task -> task.getTaskId().equals(taskId)).findFirst().orElse(null);

			assertNotNull(dbTask);
			assertEquals(String.valueOf(State.ACTIVE), dbTask.getTaskStatus());
		}
	}

}
