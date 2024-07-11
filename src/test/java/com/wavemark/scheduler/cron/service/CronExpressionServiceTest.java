package com.wavemark.scheduler.cron.service;

import static com.wavemark.scheduler.cron.constant.DayOfWeek.Sunday;
import static com.wavemark.scheduler.cron.constant.DayOfWeek.Tuesday;
import static com.wavemark.scheduler.cron.constant.DayOfWeek.Wednesday;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;

import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.util.CronExpressionUtil;
import com.wavemark.scheduler.schedule.dto.request.TaskFrequencyInput;
import com.wavemark.scheduler.testing.util.DataUtil;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class CronExpressionServiceTest {

    CronExpressionService cronExpressionService = new CronExpressionService();

    @Test
    void testGenerateCronExpression() throws CronExpressionException {
        String result = cronExpressionService.generateCronExpression(DataUtil.generateTaskFrequencyInput());

        assertThat(result).isIn("0 30 11 4 * ? *", "0 30 12 4 * ? *");
    }

    @Test
    void testGenerateCronExpression_frequencyWeekly() throws CronExpressionException {
        TaskFrequencyInput taskFrequencyInput = DataUtil.generateTaskFrequencyInput();
        taskFrequencyInput.setFrequency("weekly");
        taskFrequencyInput.setDaysOfMonth(Collections.emptyList());
        taskFrequencyInput.setDaysOfWeek(Arrays.asList("1", "3", "4"));

        String result = cronExpressionService.generateCronExpression(taskFrequencyInput);

        assertThat(result).isIn("0 30 11 ? * 1,3,4 *", "0 30 12 ? * 1,3,4 *");
    }

    @Test
    void testGenerateCronExpression_frequencyDaily() throws CronExpressionException {
        TaskFrequencyInput taskFrequencyInput = DataUtil.generateTaskFrequencyInput();
        taskFrequencyInput.setFrequency("daily");
        taskFrequencyInput.setDaysOfMonth(Collections.emptyList());
        taskFrequencyInput.setDaysOfWeek(Collections.emptyList());

        String result = cronExpressionService.generateCronExpression(taskFrequencyInput);

        assertThat(result).isIn("0 30 11 ? * * *", "0 30 12 ? * * *");
    }

    @Test
    void testGenerateCronExpression_lastDayOfMonth() throws CronExpressionException {
        TaskFrequencyInput taskFrequencyInput = DataUtil.generateTaskFrequencyInput();
        taskFrequencyInput.setDaysOfMonth(Collections.singletonList("L"));

        String result = cronExpressionService.generateCronExpression(taskFrequencyInput);

        assertThat(result).isIn("0 30 11 L * ? *", "0 30 12 L * ? *");
    }

    @Test
    void testGenerateCronExpression_lastDayOfMonthMinusOne() throws CronExpressionException {
        TaskFrequencyInput taskFrequencyInput = DataUtil.generateTaskFrequencyInput();
        taskFrequencyInput.setDaysOfMonth(Collections.singletonList("L-1"));

        String result = cronExpressionService.generateCronExpression(taskFrequencyInput);

        assertThat(result).isIn("0 30 11 L * ? *", "0 30 12 L * ? *");
    }

    @Test
    void testGenerateCronExpression_lastDayOfMonthMinusTwo() throws CronExpressionException {
        TaskFrequencyInput taskFrequencyInput = DataUtil.generateTaskFrequencyInput();
        taskFrequencyInput.setDaysOfMonth(Collections.singletonList("L-2"));

        String result = cronExpressionService.generateCronExpression(taskFrequencyInput);

        assertThat(result).isIn("0 30 11 L * ? *", "0 30 12 L * ? *");
    }

    @Test
    void testGenerateCronExpressionThrowsException_lastDayOfMonth() {
        TaskFrequencyInput taskFrequencyInput = DataUtil.generateTaskFrequencyInput();
        taskFrequencyInput.setDaysOfMonth(Collections.singletonList("L"));

        try (MockedStatic<CronExpressionUtil> util = Mockito.mockStatic(CronExpressionUtil.class)) {
            util.when(() -> CronExpressionUtil.getDaysDifferenceAndHourBetweenTimezones(anyInt(), anyInt())).thenThrow(ParseException.class);

            assertThrows(CronExpressionException.class, () -> cronExpressionService.generateCronExpression(taskFrequencyInput));
        }
    }

    @Test
    void testReverseCronExpression() throws CronExpressionException {
        CronDescription expected = new CronDescription(DataUtil.generateTaskFrequencyInput());
        expected.setMonths(Collections.emptyList());
        expected.setDaysOfWeek(Collections.emptyList());

        CronDescription resultGmt2 = cronExpressionService.reverseCronExpression("0 30 11 4 * ? *", "GMT+1");
        CronDescription resultGmt3 = cronExpressionService.reverseCronExpression("0 30 12 4 * ? *", "GMT+1");

        assertThat(expected).isIn(resultGmt2, resultGmt3);
    }

    @Test
    void testReverseCronExpressionWithListOfDays() throws CronExpressionException {
        CronDescription expected = new CronDescription(DataUtil.generateTaskFrequencyInput());
        expected.setMonths(Collections.emptyList());
        expected.setDaysOfMonth(Collections.emptyList());
        expected.setFrequency(Frequency.WEEKLY);
        expected.setDaysOfWeek(Arrays.asList(Sunday, Tuesday, Wednesday));

        CronDescription resultGmt2 = cronExpressionService.reverseCronExpression("0 30 11 ? * 1,3,4 *", "GMT+1");
        CronDescription resultGmt3 = cronExpressionService.reverseCronExpression("0 30 12 ? * 1,3,4 *", "GMT+1");

        assertThat(expected).isIn(resultGmt2, resultGmt3);
    }

//    @Test
//    void testReverseCronExpressionDifferentTimezone() throws CronExpressionException {
//        CronExpression expected = new CronExpression(DataUtil.generateTaskFrequencyInput());
//        expected.setMonths(Collections.emptyList());
//        expected.setDaysOfWeek(Collections.emptyList());
//
//        CronExpression result = cronExpressionService.reverseCronExpression("0 30 11 L * ? *", "GMT+1");
//
//        assertEquals(expected, result);
//    }

}