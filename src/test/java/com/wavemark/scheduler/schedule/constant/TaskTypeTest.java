package com.wavemark.scheduler.schedule.constant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTypeTest {

    @Test
    void testGet() {
        assertNotNull(TaskType.get("Auto-Order"));
    }

    @Test
    void testGet_invalidInput() {
        assertNull(TaskType.get("test"));
    }

}