package com.wavemark.scheduler.schedule.validation.validator;

import com.wavemark.scheduler.schedule.constant.TaskType;
import com.wavemark.scheduler.schedule.validation.annotation.TaskTypeValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaskTypeValidator implements ConstraintValidator<TaskTypeValidation, String> {

    @Override
    public boolean isValid(String taskType, ConstraintValidatorContext context) {
        TaskType type = TaskType.get(taskType);

        return type != null;
    }

}
