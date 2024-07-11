package com.wavemark.scheduler.schedule.dto.request;

import java.util.List;

import com.wavemark.scheduler.schedule.validation.annotation.DaysOfMonthValidation;
import com.wavemark.scheduler.schedule.validation.annotation.DaysOfWeekValidation;
import com.wavemark.scheduler.schedule.validation.annotation.FrequencyValidation;
import com.wavemark.scheduler.schedule.validation.annotation.HoursValidation;
import com.wavemark.scheduler.schedule.validation.annotation.MinutesValidation;
import com.wavemark.scheduler.schedule.validation.annotation.MonthsValidation;
import com.wavemark.scheduler.schedule.validation.annotation.TimeZoneValidation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskFrequencyInput {

    @FrequencyValidation
    private String frequency;
    @MinutesValidation
    private List<Integer> minutes;
    @HoursValidation
    private List<Integer> hours;
    @MonthsValidation
    private List<Integer> months;
    @DaysOfMonthValidation
    private List<String> daysOfMonth;
    @DaysOfWeekValidation
    private List<String> daysOfWeek;
    @TimeZoneValidation
    private String timezone;
}
