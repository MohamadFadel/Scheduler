package com.wavemark.scheduler.schedule.repository;

import com.wavemark.scheduler.schedule.domain.entity.TaskType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Integer> {
    TaskType findByTaskType(String type);
}
