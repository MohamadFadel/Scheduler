package com.wavemark.scheduler.schedule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskTypeRepository;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;
import com.wavemark.scheduler.testing.util.DataUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskTypeServiceTest {

    @Mock
    private TaskTypeRepository taskTypeRepository;

    @InjectMocks
    private TaskTypeService taskTypeService;

    @Test
    void testGetTaskTypeId() throws EntryNotFoundException {
        when(taskTypeRepository.findByTaskType(any())).thenReturn(DataUtil.generateTaskType());

        Integer result = taskTypeService.getTaskTypeId("Auto-Order");

        assertEquals(1, result);
    }

    @Test
    void testGetTaskTypeId_nullType() {
        when(taskTypeRepository.findByTaskType(any())).thenReturn(null);

        assertThrows(EntryNotFoundException.class, () -> taskTypeService.getTaskTypeId("Auto-Order"));
    }

    @Test
    void testGetTaskType() throws EntryNotFoundException {
        when(taskTypeRepository.findById(any())).thenReturn(DataUtil.generateTaskTypeOptional());

        String result = taskTypeService.getTaskType(1);

        assertEquals("Auto-Order", result);
    }

    @Test
    void testGetTaskType_nullType() {
        when(taskTypeRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntryNotFoundException.class, () -> taskTypeService.getTaskType(1));
    }

}