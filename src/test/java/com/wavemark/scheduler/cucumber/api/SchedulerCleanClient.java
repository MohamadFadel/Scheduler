package com.wavemark.scheduler.cucumber.api;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import java.util.List;

import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.service.SchedulerService;

import lombok.AllArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(SCOPE_CUCUMBER_GLUE)
@AllArgsConstructor
public class SchedulerCleanClient {

	private final SchedulerService schedulerService;

	public void deleteTasks(List<Integer> tasksIds) throws SchedulerException, EntryNotFoundException {
		schedulerService.deleteTasks(tasksIds);
	}
}
