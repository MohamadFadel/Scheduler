package com.wavemark.scheduler.fire.httpinvoker.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    @Test
    void testGetInstance() {
        assertNotNull(HttpClient.getInstance());
    }

    @Test
    void testGetClient() {
        assertNotNull(HttpClient.getInstance().getClient());
    }

}