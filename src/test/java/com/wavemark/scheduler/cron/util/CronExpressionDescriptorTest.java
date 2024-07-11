package com.wavemark.scheduler.cron.util;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.constant.Month;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CronExpressionDescriptorTest {

    @Test
    void testReverseMinuteCronExpression() {
        String expression = "1,2";

        List<Integer> result = CronExpressionDescriptor.reverseMinuteCronExpression(expression);

        assertEquals(Arrays.asList(1, 2), result);
    }

    @Test
    void testReverseHourCronExpression() {
        String expression = "1,2";

        List<Integer> result = CronExpressionDescriptor.reverseHourCronExpression(expression);

        assertEquals(Arrays.asList(1, 2), result);
    }

    @Test
    void testReverseDayOfMonthCronExpression() {
        String expression = "1,2";

        List<DayOfMonth> result = CronExpressionDescriptor.reverseDayOfMonthCronExpression(expression);

        assertEquals(Arrays.asList(DayOfMonth.Day_1, DayOfMonth.Day_2), result);
    }

    @Test
    void testReverseMonthCronExpression() {
        String expression = "1,2";

        List<Month> result = CronExpressionDescriptor.reverseMonthCronExpression(expression);

        assertEquals(Arrays.asList(Month.January, Month.February), result);
    }

    @Test
    void testReverseDayOfWeekCronExpression() {
        String expression = "1,2";

        List<DayOfWeek> result = CronExpressionDescriptor.reverseDayOfWeekCronExpression(expression);

        assertEquals(Arrays.asList(DayOfWeek.Sunday, DayOfWeek.Monday), result);
    }

}