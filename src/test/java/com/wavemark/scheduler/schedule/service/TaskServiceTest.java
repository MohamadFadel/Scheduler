//package com.wavemark.scheduler.schedule.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.mockStatic;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.text.ParseException;
//import java.time.ZoneId;
//import java.util.List;
//import java.util.Optional;
//
//import com.wavemark.scheduler.cron.exception.CronExpressionException;
//import com.wavemark.scheduler.cron.service.CronExpressionService;
//import com.wavemark.scheduler.schedule.constant.State;
//import com.wavemark.scheduler.schedule.domain.entity.Task;
//import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
//import com.wavemark.scheduler.schedule.repository.TaskRepository;
//import com.wavemark.scheduler.schedule.service.core.TaskService;
//import com.wavemark.scheduler.schedule.service.core.TaskTypeService;
//import com.wavemark.scheduler.testing.util.DataUtil;
//
//import com.cardinalhealth.service.support.security.SecurityUtilsV2;
//import com.warden.oauth2.common.userdetail.webbappuser.WebappUserDetails;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class TaskServiceTest {
//
//    @Mock
//    private TaskRepository taskRepository;
//
//    @Mock
//    private TaskTypeService taskTypeService;
//
//    @Mock
//    private CronExpressionService cronExpressionService;
//
//    @InjectMocks
//    TaskService taskService;
//
//    @Test
//    void testSaveTask() {
//        when(taskRepository.save(any())).thenReturn(new Task());
//
//        taskService.saveTask(new Task());
//
//        verify(taskRepository).save(any());
//    }
//
//    @Test
//    void testFindTasksByEndpointId() {
//        when(taskRepository.findBySourceIdEndpointId(any())).thenReturn(DataUtil.generateTaskList());
//
//        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
//            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
//
//            List<Task> result = taskService.findTasksByEndpointId();
//
//            assertEquals(2, result.size());
//        }
//    }
//
//    @Test
//    void testFindActiveTasksByEndpointId() {
//        when(taskRepository.findBySourceIdEndpointId(any())).thenReturn(DataUtil.generateTaskList());
//
//        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
//            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
//
//            List<Task> result = taskService.findActiveTasksByEndpointId();
//
//            assertEquals(1, result.size());
//        }
//    }
//
//    @Test
//    void testFindActiveTaskById() throws EntryNotFoundException {
//        when(taskRepository.findByTaskIdAndSourceIdEndpointId(any(), any())).thenReturn(DataUtil.generateOptionalTask(String.valueOf(State.ACTIVE)));
//
//        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
//            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
//            Task result = taskService.findActiveTaskById(1);
//
//            assertEquals(1, result.getTaskId());
//            assertEquals("testName", result.getTaskName());
//        }
//    }
//
//    @Test
//    void testFindActiveTaskByIdThrowsEntryNotFoundException() {
//        when(taskRepository.findByTaskIdAndSourceIdEndpointId(any(), any())).thenReturn(DataUtil.generateOptionalTask(String.valueOf(State.DEACTIVATED)));
//        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
//            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
//
//            assertThrows(EntryNotFoundException.class, () -> taskService.findActiveTaskById(5));
//        }
//    }
//
//    @Test
//    void testFindActiveTaskByTaskName() throws EntryNotFoundException {
//        when(taskRepository.findByTaskName(any())).thenReturn(DataUtil.generateTaskListWithState(String.valueOf(State.ACTIVE)));
//
//        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
//            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
//            Task result = taskService.findActiveTaskByTaskName("testName");
//
//            assertEquals(1, result.getTaskId());
//            assertEquals("testName", result.getTaskName());
//        }
//    }
//
//    @Test
//    void testFindActiveTaskByTaskNameThrowsEntryNotFoundException() {
//        when(taskRepository.findByTaskName(any())).thenReturn(DataUtil.generateTaskListWithState(String.valueOf(State.DEACTIVATED)));
//        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
//            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
//
//            assertThrows(EntryNotFoundException.class, () -> taskService.findActiveTaskByTaskName("testName"));
//        }
//    }
//
//    @Test
//    void testFindTaskById() throws EntryNotFoundException {
//        when(taskRepository.findById(any())).thenReturn(DataUtil.generateOptionalTask(String.valueOf(State.ACTIVE)));
//
//        Task result = taskService.findTaskById(1);
//
//        assertEquals(1, result.getTaskId());
//        assertEquals("testName", result.getTaskName());
//    }
//
//    @Test
//    void testFindTaskByIdThrowsEntryNotFoundException() {
//        when(taskRepository.findById(any())).thenReturn(Optional.empty());
//
//        assertThrows(EntryNotFoundException.class, () -> taskService.findTaskById(5));
//    }
//
//    @Test
//    void testBuildTask() throws CronExpressionException, ParseException, EntryNotFoundException {
//        when(cronExpressionService.generateCronExpression(any())).thenReturn("0 0/1 * * * ?");
//        when(taskTypeService.getTaskTypeId(any())).thenReturn(1);
//
//        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
//            securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());
//            securityUtils.when(SecurityUtilsV2::getCurrentAuthDepartment).thenReturn("testDep");
//            WebappUserDetails userMock = mock(WebappUserDetails.class);
//            securityUtils.when(SecurityUtilsV2::getWebAppUserInfo).thenReturn(userMock);
//
//            when(userMock.getFullName()).thenReturn("name");
//
//            Task result = taskService.buildTask(DataUtil.generateTaskInput());
//
//            assertNotNull(result);
//            assertEquals("Auto-Order_testDep", result.getTaskName());
//            assertEquals(1, result.getTaskTypeId());
//        }
//    }
//
//    @Test
//    void testUpdateTask() throws CronExpressionException, ParseException {
//
//        TaskService taskServiceMock = spy(taskService);
//
//        when(cronExpressionService.generateCronExpression(DataUtil.generateTaskFrequencyInput()))
//                .thenReturn("0 0/1 * * * ?");
//
//        try (MockedStatic<SecurityUtilsV2> securityUtils = mockStatic(SecurityUtilsV2.class)) {
//            securityUtils.when(SecurityUtilsV2::getTimezone).thenReturn(ZoneId.systemDefault());
//            WebappUserDetails userMock = mock(WebappUserDetails.class);
//            securityUtils.when(SecurityUtilsV2::getWebAppUserInfo).thenReturn(userMock);
//
//            when(userMock.getFullName()).thenReturn("name");
//
//            Task result = taskServiceMock.updateTask(DataUtil.generateTask(), DataUtil.generateTaskInputUpdated());
//
//            assertNotNull(result);
//        }
//    }
//
//    @Test
//    void testDeleteTask() {
//        doNothing().when(taskRepository).deleteById(any());
//
//        taskService.deleteTask(1);
//
//        verify(taskRepository, Mockito.times(1)).deleteById(any());
//    }
//
//}