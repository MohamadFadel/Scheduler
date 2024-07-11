package com.wavemark.scheduler.cron.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import com.wavemark.scheduler.cron.constant.DayOfMonth;
import com.wavemark.scheduler.cron.constant.DayOfWeek;
import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.testing.util.DataUtil;

import org.junit.jupiter.api.Test;

class CronExpressionUtilTest {

    @Test
    void testDetermineFrequencyReturnsHourly() {
        List<Integer> hoursList = Arrays.asList(1, 2);

        Frequency result = CronExpressionUtil.determineFrequency(hoursList, null, null);

        assertEquals(Frequency.HOURLY, result);
    }

    @Test
    void testDetermineFrequencyReturnsMonthly() {
        List<Integer> hoursList = Collections.emptyList();
        List<DayOfMonth> daysOfMonth = Arrays.asList(DayOfMonth.Day_1, DayOfMonth.Day_2);

        Frequency result = CronExpressionUtil.determineFrequency(hoursList, daysOfMonth, null);

        assertEquals(Frequency.MONTHLY, result);
    }

    @Test
    void testDetermineFrequencyReturnsWeekly() {
        List<Integer> hoursList = Collections.emptyList();
        List<DayOfMonth> daysOfMonth = Collections.emptyList();
        List<DayOfWeek> daysOfWeek = Arrays.asList(DayOfWeek.Sunday, DayOfWeek.Monday);

        Frequency result = CronExpressionUtil.determineFrequency(hoursList, daysOfMonth, daysOfWeek);

        assertEquals(Frequency.WEEKLY, result);
    }

    @Test
    void testDetermineFrequencyReturnsQuarterHour() {
        List<Integer> hoursList = Collections.emptyList();
        List<DayOfMonth> daysOfMonth = Collections.emptyList();
        List<DayOfWeek> daysOfWeek = Collections.emptyList();

        Frequency result = CronExpressionUtil.determineFrequency(hoursList, daysOfMonth, daysOfWeek);

        assertEquals(Frequency.QUARTER_HOUR, result);
    }

    @Test
    void testDetermineFrequencyReturnsDaily() {
        List<Integer> hoursList = Collections.singletonList(1);
        List<DayOfMonth> daysOfMonth = Collections.emptyList();
        List<DayOfWeek> daysOfWeek = Collections.emptyList();

        Frequency result = CronExpressionUtil.determineFrequency(hoursList, daysOfMonth, daysOfWeek);

        assertEquals(Frequency.DAILY, result);
    }

    @Test
    void testGetDaysDifferenceAndHourBetweenTimezones() throws ParseException {
        int[] expected = {0, 6};

        int[] result = CronExpressionUtil.getDaysDifferenceAndHourBetweenTimezones(2, 6);

        assertArrayEquals(expected, result);
    }

    @Test
    void testGetTimezoneHoursDifference() {
        int expectedInMillis = -3600000;

        int result = CronExpressionUtil.getTimezoneHoursDifference(TimeZone.getTimeZone("GMT+1"),
                TimeZone.getTimeZone("GMT"));

        assertEquals(expectedInMillis, result);
    }

    @Test
    void testGetConvertedDayHourArray() throws CronExpressionException {
        CronDescription cronDescription = new CronDescription(DataUtil.generateTaskFrequencyInput());
        cronDescription.setDaysOfWeek(Arrays.asList(DayOfWeek.Sunday, DayOfWeek.Monday));

        String[] expected = {"Sunday", "09", "Monday", "09"};

        String[] result = CronExpressionUtil
                .getConvertedDayHourArray(cronDescription, -3600000);

        assertArrayEquals(expected, result);
    }

    @Test
    void testGetConvertedHours() throws CronExpressionException {
        List<Integer> result = CronExpressionUtil
                .getConvertedHours(new CronDescription(DataUtil.generateTaskFrequencyInput()),
                        -3600000);

        assertEquals(9, result.get(0));
    }

    @Test
    void testGetDayHourString() throws CronExpressionException {
        String result = CronExpressionUtil
                .getDayHourString("d HH", "1 10", -3600000);

        assertEquals("1 09", result);
    }

    @Test
    void testGetDayHourStringThrowsException() {
        assertThrows(CronExpressionException.class, () -> CronExpressionUtil
                .getDayHourString("d HH", "11111", -3600000));
    }

}