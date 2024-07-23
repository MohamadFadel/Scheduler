package com.wavemark.scheduler.fire.httpinvoker.property;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.wavemark.scheduler.testing.util.DataUtil;

import org.junit.jupiter.api.Test;

class HttpPropertyTest {

    @Test
    void testFetchRequestProperty() {
        HttpProperty result = DataUtil.generateHttpProperty();

        assertEquals("taskName", result.getTaskName());
        assertEquals("endpointName", result.getEndpointName());
        assertEquals("http://localhost:8081/url", result.getUrl());
        assertEquals("{ testBodyParam: test }", result.getBodyParam());
    }

}