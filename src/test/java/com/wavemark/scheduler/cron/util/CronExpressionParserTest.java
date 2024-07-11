package com.wavemark.scheduler.cron.util;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.constant.Month;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CronExpressionParserTest {

    @Test
    void testBuildMinutesCronExpression_frequencyMinutes() throws CronExpressionException {
        List<Integer> minutes = Collections.singletonList(1);
        Frequency frequency = Frequency.MINUTES;

        String result = CronExpressionParser.buildMinutesCronExpression(minutes, frequency);

        assertEquals("0/1", result);
    }

    @Test
    void testBuildMinutesCronExpression_frequencyDaily() throws CronExpressionException {
        List<Integer> minutes = Arrays.asList(1, 2);
        Frequency frequency = Frequency.DAILY;

        String result = CronExpressionParser.buildMinutesCronExpression(minutes, frequency);

        assertEquals("1,2", result);
    }

    @Test
    void testBuildMinutesCronExpressionThrowsException_frequencyDaily() {
        List<Integer> minutes = Collections.singletonList(-1);
        Frequency frequency = Frequency.DAILY;

        assertThrows(CronExpressionException.class,
                () -> CronExpressionParser.buildMinutesCronExpression(minutes, frequency));
    }

    @Test
    void testBuildMinutesCronExpression_frequencyQuarterHour() throws CronExpressionException {
        Frequency frequency = Frequency.QUARTER_HOUR;

        String result = CronExpressionParser.buildMinutesCronExpression(null, frequency);

        assertEquals("0/15", result);
    }

    @Test
    void testBuildHoursCronExpression() throws CronExpressionException {
        List<Integer> hours = Arrays.asList(1, 2);
        Frequency frequency = Frequency.DAILY;

        String result = CronExpressionParser.buildHoursCronExpression(hours, frequency);

        assertEquals("1,2", result);
    }

    @Test
    void testBuildHoursCronExpressionThrowsException() {
        List<Integer> hours = Arrays.asList(24, -1);
        Frequency frequency = Frequency.DAILY;


        assertThrows(CronExpressionException.class,
                () -> CronExpressionParser.buildHoursCronExpression(hours, frequency));
    }

    @Test
    void testBuildHoursCronExpression_frequencyQuarterHour() throws CronExpressionException {
        List<Integer> hours = Arrays.asList(1, 2);
        Frequency frequency = Frequency.QUARTER_HOUR;

        String result = CronExpressionParser.buildHoursCronExpression(hours, frequency);

        assertEquals("*", result);
    }

    @Test
    void testBuildHoursCronExpression_hoursNull() throws CronExpressionException {
        Frequency frequency = Frequency.DAILY;

        String result = CronExpressionParser.buildHoursCronExpression(null, frequency);

        assertEquals("*", result);
    }

    @Test
    void testBuildHoursCronExpression_hoursEmpty() throws CronExpressionException {
        List<Integer> hours = Collections.emptyList();
        Frequency frequency = Frequency.DAILY;

        String result = CronExpressionParser.buildHoursCronExpression(hours, frequency);

        assertEquals("*", result);
    }

    @Test
    void testBuildDaysOfMonthCronExpression() {
        List<DayOfMonth> daysOfMonth = Arrays.asList(DayOfMonth.Day_1, DayOfMonth.Day_2);
        Frequency frequency = Frequency.MONTHLY;

        String result = CronExpressionParser.buildDaysOfMonthCronExpression(daysOfMonth, frequency);

        assertEquals("1,2", result);
    }

    @Test
    void testBuildDaysOfMonthCronExpression_frequencyQuarterHour() {
        List<DayOfMonth> daysOfMonth = Arrays.asList(DayOfMonth.Day_1, DayOfMonth.Day_2);
        Frequency frequency = Frequency.QUARTER_HOUR;

        String result = CronExpressionParser.buildDaysOfMonthCronExpression(daysOfMonth, frequency);

        assertEquals("1/1", result);
    }

    @Test
    void testBuildDaysOfMonthCronExpression_daysOfMonthNull() {
        Frequency frequency = Frequency.MONTHLY;

        String result = CronExpressionParser.buildDaysOfMonthCronExpression(null, frequency);

        assertEquals("?", result);
    }

    @Test
    void testBuildDaysOfMonthCronExpression_daysOfMonthEmpty() {
        List<DayOfMonth> daysOfMonth = Collections.emptyList();
        Frequency frequency = Frequency.MONTHLY;

        String result = CronExpressionParser.buildDaysOfMonthCronExpression(daysOfMonth, frequency);

        assertEquals("?", result);
    }

    @Test
    void testBuildMonthsCronExpression() {
        List<Month> months = Arrays.asList(Month.March, Month.April);

        String result = CronExpressionParser.buildMonthsCronExpression(months, null);

        assertEquals("3,4", result);
    }

    @Test
    void testBuildMonthsCronExpression_monthsNull() {
        String result = CronExpressionParser.buildMonthsCronExpression(null, null);

        assertEquals("*", result);
    }

    @Test
    void testBuildMonthsCronExpression_monthsEmpty() {
        List<Month> months = Collections.emptyList();

        String result = CronExpressionParser.buildMonthsCronExpression(months, null);

        assertEquals("*", result);
    }

    @Test
    void testBuildDaysOfWeekCronExpression() {
        List<DayOfWeek> daysOfWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Tuesday);
        List<DayOfMonth> daysOfMonth = Collections.emptyList();
        Frequency frequency = Frequency.WEEKLY;

        String result = CronExpressionParser.buildDaysOfWeekCronExpression(daysOfWeek, daysOfMonth, frequency);

        assertEquals("2,3", result);
    }

    @Test
    void testBuildDaysOfWeekCronExpression_frequencyQuarterHour() {
        List<DayOfWeek> daysOfWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Tuesday);
        List<DayOfMonth> daysOfMonth = Arrays.asList(DayOfMonth.Day_1, DayOfMonth.Day_2);
        Frequency frequency = Frequency.QUARTER_HOUR;

        String result = CronExpressionParser.buildDaysOfWeekCronExpression(daysOfWeek, daysOfMonth, frequency);

        assertEquals("?", result);
    }

    @Test
    void testBuildDaysOfWeekCronExpression_daysOfWeekNull() {
        Frequency frequency = Frequency.WEEKLY;

        String result = CronExpressionParser.buildDaysOfWeekCronExpression(null, null, frequency);

        assertEquals("*", result);
    }

    @Test
    void testBuildDaysOfWeekCronExpression_daysOfWeekEmpty() {
        List<DayOfWeek> daysOfWeek = Collections.emptyList();
        Frequency frequency = Frequency.WEEKLY;

        String result = CronExpressionParser.buildDaysOfWeekCronExpression(daysOfWeek, null, frequency);

        assertEquals("*", result);
    }

}