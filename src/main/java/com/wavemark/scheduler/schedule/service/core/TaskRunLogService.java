package com.wavemark.scheduler.schedule.service.core;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.fire.constant.TriggerState;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.domain.projection.TaskRunLogRP;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskRunLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskRunLogService {

    private final SchedulerFactoryBean schedulerFactoryBean;

    private final TaskRunLogRepository taskRunLogRepository;

    public TaskRunLog getLastRun(Integer id) throws EntryNotFoundException {
        return taskRunLogRepository.findById(id).orElseThrow(EntryNotFoundException::new);
    }

    public List<TaskRunLogRP> getTaskRunLogReport() {
        return taskRunLogRepository.getTaskRunLogReport(SecurityUtilsV2.getCurrentAuthDepartment());
    }

    public TaskRunLog buildTaskRunLog(Task dbTask, String responseMessage, Integer responseCode, long time)
            throws SchedulerException {

        String instanceName = schedulerFactoryBean.getScheduler().getSchedulerInstanceId();

        if (Objects.nonNull(responseMessage) && responseMessage.length() > 4000)
            responseMessage = responseMessage.substring(0, 3950);

        return TaskRunLog.builder()
                .taskId(dbTask.getTaskId())
                .taskName(dbTask.getTaskName())
                .taskDescription(dbTask.getDescription())
                .taskCronExpression(dbTask.getCronExpression())
                .responseCode(responseCode)
                .runStartDate(Instant.now())
                .runEndDate(Instant.now().plusMillis(time))
                .runStatus(responseCode == 200 ? String.valueOf(TriggerState.SUCCESS) : String.valueOf(TriggerState.FAILURE))
                .responseMessage(responseMessage)
                .instanceName(instanceName)
                .runDuration(time)
                .build();
    }

    public Integer saveTaskRunLog(TaskRunLog taskRunLog) {
        TaskRunLog dbTaskRunLog = taskRunLogRepository.save(taskRunLog);
        return dbTaskRunLog.getTaskRunLogId();
    }

}
