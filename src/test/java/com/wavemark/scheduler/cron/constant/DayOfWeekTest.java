package com.wavemark.scheduler.cron.constant;

import org.junit.jupiter.api.Test;

import static com.wavemark.scheduler.cron.constant.DayOfWeek.Sunday;
import static org.junit.jupiter.api.Assertions.*;

class DayOfWeekTest {

    @Test
    void testGet() {
        assertEquals(Sunday, DayOfWeek.get("1"));
    }

}