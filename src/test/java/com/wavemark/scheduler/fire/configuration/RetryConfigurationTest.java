package com.wavemark.scheduler.fire.configuration;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetryConfigurationTest {

    @Test
    void testRetryPolicy() {
        assertNotNull(RetryConfiguration.retryPolicy());
    }

}