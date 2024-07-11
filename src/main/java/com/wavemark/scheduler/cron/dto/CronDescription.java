package com.wavemark.scheduler.cron.dto;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.constant.Month;
import com.wavemark.scheduler.schedule.dto.request.TaskFrequencyInput;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CronDescription {

    private Frequency frequency;
    private List<Integer> minutes;
    private List<Integer> hours;
    private List<Month> months;
    private List<DayOfMonth> daysOfMonth;
    private List<DayOfWeek> daysOfWeek;

    public CronDescription(TaskFrequencyInput taskFrequencyInput) {
        Frequency cronFrequency = Frequency.get(taskFrequencyInput.getFrequency());

        List<DayOfMonth> dayOfMonthList = taskFrequencyInput.getDaysOfMonth().stream()
                .map(DayOfMonth::get)
                .collect(Collectors.toList());

        List<DayOfWeek> dayOfWeekList = taskFrequencyInput.getDaysOfWeek().stream()
                .map(DayOfWeek::get)
                .collect(Collectors.toList());

        this.hours = taskFrequencyInput.getHours();
        this.frequency = cronFrequency;
        this.minutes = taskFrequencyInput.getMinutes();

        switch (cronFrequency) {
            case WEEKLY:
                this.daysOfWeek = dayOfWeekList;
                break;
            case MONTHLY:
                this.daysOfMonth = dayOfMonthList;
                break;
        }
    }

}
