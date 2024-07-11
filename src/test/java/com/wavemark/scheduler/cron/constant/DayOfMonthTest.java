package com.wavemark.scheduler.cron.constant;

import org.junit.jupiter.api.Test;

import static com.wavemark.scheduler.cron.constant.DayOfMonth.Day_1;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DayOfMonthTest {

    @Test
    void testGet() {
        assertEquals(Day_1, DayOfMonth.get("1"));
    }

}