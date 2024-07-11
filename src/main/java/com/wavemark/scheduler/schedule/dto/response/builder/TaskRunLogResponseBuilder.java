package com.wavemark.scheduler.schedule.dto.response.builder;

import com.wavemark.scheduler.common.constant.DateUtil;
import com.wavemark.scheduler.fire.constant.TriggerState;
import com.wavemark.scheduler.fire.http.response.message.ResponseMessage;
import com.wavemark.scheduler.fire.http.response.message.ResponseMessageFactory;
import com.wavemark.scheduler.schedule.domain.projection.TaskRunLogRP;
import com.wavemark.scheduler.schedule.dto.response.TaskRunLogResponse;

public class TaskRunLogResponseBuilder {

    public static TaskRunLogResponse buildTaskRunLogResponse(TaskRunLogRP taskRunLogRP, String frequency) {

        ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(taskRunLogRP.getResponseCode());
        String fullMessage = taskRunLogRP.getRunStatus().equals(String.valueOf(TriggerState.SUCCESS)) ? responseMessage.getDescription()
                : responseMessage.getDescription() + " Please contact support for more information.";

        return TaskRunLogResponse.builder()
          .taskId(taskRunLogRP.getTaskId())
          .taskType(taskRunLogRP.getTaskType())
          .taskDescription(taskRunLogRP.getDescription())
          .taskFrequency(frequency)
          .startDateTime(DateUtil.convertToZonedDateTime(taskRunLogRP.getRunStartDate()))
          .status(taskRunLogRP.getRunStatus().equals(String.valueOf(TriggerState.SUCCESS)) ? "COMPLETED" : "FAILED")
          .responseMessage(fullMessage)
          .build();
    }
}
