package com.wavemark.scheduler.schedule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.wavemark.catalog.security.userdetail.UserDetails;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskRepository;
import com.wavemark.scheduler.schedule.service.core.TaskService;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;
import com.wavemark.scheduler.testing.util.DataUtil;

import com.cardinalhealth.service.support.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskTypeService taskTypeService;

    @Mock
    private CronExpressionService cronExpressionService;

    @InjectMocks
	TaskService taskService;

    @Test
    void testSaveTask() {
        when(taskRepository.save(any())).thenReturn(new Task());

        taskService.saveTask(new Task());

        verify(taskRepository).save(any());
    }

    @Test
    void testFindActiveTasksByEndpointId() {
        when(taskRepository.findBySourceIdEndpointId(any())).thenReturn(DataUtil.generateTaskList());

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentAuthDepartment).thenReturn("testDep");

            List<Task> result = taskService.findActiveTasksByEndpointId();

            assertEquals(1, result.size());
        }
    }

    @Test
    void testFindActiveTaskById() throws EntryNotFoundException {
        when(taskRepository.findByTaskIdAndSourceIdEndpointId(any(), any())).thenReturn(DataUtil.generateOptionalTask(String.valueOf(State.ACTIVE)));

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentAuthDepartment).thenReturn("testDep");
            Task result = taskService.findActiveTaskById(1);

            assertEquals(1, result.getTaskId());
            assertEquals("testName", result.getTaskName());
        }
    }

    @Test
    void testFindActiveTaskByIdThrowsEntryNotFoundException() {
        when(taskRepository.findByTaskIdAndSourceIdEndpointId(any(), any())).thenReturn(DataUtil.generateOptionalTask(String.valueOf(State.DEACTIVATED)));
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(EntryNotFoundException.class, () -> taskService.findActiveTaskById(5));
        }
    }

    @Test
    void testFindActiveTaskByTaskName() throws EntryNotFoundException {
        when(taskRepository.findByTaskName(any())).thenReturn(DataUtil.generateTaskListWithState(String.valueOf(State.ACTIVE)));

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentAuthDepartment).thenReturn("testDep");
            Task result = taskService.findActiveTaskByTaskName("testName");

            assertEquals(1, result.getTaskId());
            assertEquals("testName", result.getTaskName());
        }
    }

    @Test
    void testFindActiveTaskByTaskNameThrowsEntryNotFoundException() {
        when(taskRepository.findByTaskName(any())).thenReturn(DataUtil.generateTaskListWithState(String.valueOf(State.DEACTIVATED)));
        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getCurrentAuthDepartment).thenReturn("testDep");

            assertThrows(EntryNotFoundException.class, () -> taskService.findActiveTaskByTaskName("testName"));
        }
    }

    @Test
    void testFindTaskById() throws EntryNotFoundException {
        when(taskRepository.findById(any())).thenReturn(DataUtil.generateOptionalTask(String.valueOf(State.ACTIVE)));

        Task result = taskService.findTaskById(1);

        assertEquals(1, result.getTaskId());
        assertEquals("testName", result.getTaskName());
    }

    @Test
    void testFindTaskByIdThrowsEntryNotFoundException() {
        when(taskRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> taskService.findTaskById(5));
    }

    @Test
    void testBuildTask() throws CronExpressionException, ParseException, EntryNotFoundException {
        when(cronExpressionService.generateCronExpression(any())).thenReturn("0 0/1 * * * ?");
        when(taskTypeService.getTaskTypeId(any())).thenReturn(1);

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getTimezone).thenReturn(ZoneId.systemDefault());
            securityUtils.when(SecurityUtils::getCurrentAuthDepartment).thenReturn("testDep");
            UserDetails userMock = mock(UserDetails.class);
            securityUtils.when(SecurityUtils::getWebAppUserInfo).thenReturn(userMock);

            when(userMock.getFirstName()).thenReturn("userFirstName");
            when(userMock.getLastName()).thenReturn("userLastName");

            Task result = taskService.buildTask(DataUtil.generateTaskInput());

            assertNotNull(result);
            assertEquals("Auto-Order_testDep", result.getTaskName());
            assertEquals(1, result.getTaskTypeId());
        }
    }

    @Test
    void testUpdateTask() throws CronExpressionException, EntryNotFoundException, ParseException {

        TaskService taskServiceMock = spy(taskService);
        Mockito.doReturn(DataUtil.generateTask()).when(taskServiceMock).findActiveTaskById(any());

        when(cronExpressionService.generateCronExpression(DataUtil.generateTaskFrequencyInput()))
                .thenReturn("0 0/1 * * * ?");

        try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
            securityUtils.when(SecurityUtils::getTimezone).thenReturn(ZoneId.systemDefault());
            UserDetails userMock = mock(UserDetails.class);
            securityUtils.when(SecurityUtils::getWebAppUserInfo).thenReturn(userMock);

            when(userMock.getFirstName()).thenReturn("userMock");
            when(userMock.getLastName()).thenReturn("userMock");

            Task result = taskServiceMock.updateTask(1, DataUtil.generateTaskInputUpdated());

            assertNotNull(result);
        }
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(any());

        taskService.deleteTask(1);

        verify(taskRepository, Mockito.times(1)).deleteById(any());
    }

}