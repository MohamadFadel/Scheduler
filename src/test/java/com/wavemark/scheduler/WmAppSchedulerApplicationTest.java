package com.wavemark.scheduler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class WmAppSchedulerApplicationTest {

    @Test
    void testMain() {
        try (MockedStatic<SpringApplication> application = Mockito.mockStatic(SpringApplication.class)) {
            WmAppSchedulerApplication.main(new String[]{"test"});

            application.verify(() -> SpringApplication.run((Class<?>) any(), anyString()));
        }
    }

}