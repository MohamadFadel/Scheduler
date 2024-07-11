package com.wavemark.scheduler.cron.constant;

import org.junit.jupiter.api.Test;

import static com.wavemark.scheduler.cron.constant.Frequency.HOURLY;
import static org.junit.jupiter.api.Assertions.*;

class FrequencyTest {

    @Test
    void testGet() {
        assertEquals(HOURLY, Frequency.get("hourly"));
    }

}