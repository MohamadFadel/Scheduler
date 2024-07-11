package com.wavemark.scheduler.schedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskFrequencyResponse {

    private String frequency;
    private String frequencyDescribed;
    private List<Integer> minutes;
    private List<Integer> hours;
    private List<Integer> months;
    private List<String> daysOfMonth;
    private List<String> daysOfWeek;
    private String timezone;
}
