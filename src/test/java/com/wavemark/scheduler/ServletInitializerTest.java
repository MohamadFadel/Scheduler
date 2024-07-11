package com.wavemark.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;

import static org.junit.jupiter.api.Assertions.*;

class ServletInitializerTest {

    @Test
    void testConfigure() {
        ServletInitializer servletInitializer = new ServletInitializer();
        assertNotNull(servletInitializer.configure(new SpringApplicationBuilder()));
    }

}