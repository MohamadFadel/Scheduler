package com.wavemark.scheduler.schedule.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.schedule.domain.entity.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @LogPerformanceTime
    List<Task> findBySourceIdEndpointId(String endpointId);

    List<Task> findByTaskIdIn(Collection<Integer> ids);

    @LogPerformanceTime
    Optional<Task> findByTaskIdAndSourceIdEndpointId(Integer id, String endpointId);

    List<Task> findByTaskName(String name);
}
