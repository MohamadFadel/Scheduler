package com.wavemark.scheduler.cron.constant;

import org.junit.jupiter.api.Test;

import static com.wavemark.scheduler.cron.constant.Month.January;
import static org.junit.jupiter.api.Assertions.*;

class MonthTest {

    @Test
    void testGet() {
        assertEquals(January, Month.get("1"));
    }

}