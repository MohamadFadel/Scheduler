package com.wavemark.scheduler.fire.http.response;

import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.service.core.TaskRunLogService;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.schedule.service.quartz.TriggerService;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseLogServiceTest {

	@Mock
	private TaskRunLogService taskRunLogService;
	@Mock
	private TaskService taskService;
	@Mock
	private TriggerService triggerService;

	@InjectMocks
	private ResponseLogService responseLogService;


	@Test
	void logResponse() throws SchedulerException {

		doReturn(1).when(taskService).saveTask(any());

		Trigger trigger = Mockito.spy(Trigger.class);
		when(trigger.getNextFireTime()).thenReturn(new Date());
		Mockito.when(triggerService.getTrigger(any())).thenReturn(trigger);

		doReturn(DataUtil.generateTaskRunLog()).when(taskRunLogService).buildTaskRunLog(any(),any(),any(),anyLong());
		doReturn(2).when(taskRunLogService).saveTaskRunLog(any());

		assertDoesNotThrow(() -> responseLogService.logResponse(new Task(), DataUtil.generateSuccessResponse(), 54L));
	}

	@Test
	void logResponseError() throws SchedulerException {

		doReturn(1).when(taskService).saveTask(any());

		Trigger trigger = Mockito.spy(Trigger.class);
		when(trigger.getNextFireTime()).thenReturn(new Date());
		Mockito.when(triggerService.getTrigger(any())).thenReturn(trigger);

		doReturn(DataUtil.generateTaskRunLog()).when(taskRunLogService).buildTaskRunLog(any(),any(),any(),anyLong());
		doReturn(2).when(taskRunLogService).saveTaskRunLog(any());

		assertDoesNotThrow(() -> responseLogService.logResponseError(new Task(), DataUtil.generateResponse(), 87L));
	}
}