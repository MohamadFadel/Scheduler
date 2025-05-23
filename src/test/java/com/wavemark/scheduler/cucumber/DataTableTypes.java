package com.wavemark.scheduler.cucumber;

import java.util.Collections;
import java.util.Map;

import com.wavemark.scheduler.schedule.dto.request.TaskFrequencyInput;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;

import io.cucumber.java.DataTableType;

public class DataTableTypes {

    @DataTableType
    public TaskInput taskInputEntry(Map<String, String> entry) {
        TaskFrequencyInput taskFrequencyInput = new TaskFrequencyInput(
                entry.get("taskFrequencyInput.frequency"),
                Collections.singletonList(Integer.valueOf(entry.get("taskFrequencyInput.minutes[0]"))),
                Collections.singletonList(Integer.valueOf(entry.get("taskFrequencyInput.hours[0]"))),
                Collections.singletonList(Integer.valueOf(entry.get("taskFrequencyInput.months[0]"))),
                Collections.singletonList(entry.get("taskFrequencyInput.daysOfMonth[0]")),
                Collections.singletonList(entry.get("taskFrequencyInput.daysOfWeek[0]")),
                entry.get("taskFrequencyInput.timezone")
        );

        return new TaskInput(
                entry.get("taskType"),
                entry.get("description"),
                entry.get("bodyParam"),
                taskFrequencyInput,
                entry.get("emails")
        );
    }

}
