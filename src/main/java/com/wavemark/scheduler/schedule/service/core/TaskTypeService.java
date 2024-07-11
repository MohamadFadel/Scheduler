package com.wavemark.scheduler.schedule.service.core;

import java.util.Objects;
import java.util.Optional;

import com.wavemark.scheduler.schedule.domain.entity.TaskType;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskTypeService {

    private final TaskTypeRepository taskTypeRepository;

    public int getTaskTypeId(String taskType) throws EntryNotFoundException {
        TaskType type = taskTypeRepository.findByTaskType(taskType);

        if (Objects.nonNull(type))
            return type.getTaskTypeId();

        log.error("Task Type is wrong!");
        throw new EntryNotFoundException();
    }

    public String getTaskType(Integer taskTypeId) throws EntryNotFoundException {
        Optional<TaskType> taskType = taskTypeRepository.findById(taskTypeId);

        return taskType.map(TaskType::getTaskType).orElseThrow(EntryNotFoundException::new);
    }
}
