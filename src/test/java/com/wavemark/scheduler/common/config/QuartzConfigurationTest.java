package com.wavemark.scheduler.common.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.cardinalhealth.service.support.configuration.DatasourceRoutingConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@ExtendWith(MockitoExtension.class)
class QuartzConfigurationTest {

	@Mock
	private ApplicationContext applicationContext;
	@InjectMocks
	private QuartzConfiguration quartzConfiguration;


	@Test
	void testInit() {
		assertDoesNotThrow(() -> quartzConfiguration.init());
	}

	@Test
	void testScheduler() throws SchedulerException {

		QuartzConfiguration quartzConfigurationMock = mock(QuartzConfiguration.class);
		quartzConfigurationMock = spy(quartzConfigurationMock);
		doReturn(new SpringBeanJobFactory()).when(quartzConfigurationMock).springBeanJobFactory();

		DatasourceRoutingConfiguration clientDatasource = mock(DatasourceRoutingConfiguration.class);
		SchedulerFactoryBean schedulerFactoryBean = quartzConfigurationMock.scheduler(clientDatasource);

		assertNotNull(schedulerFactoryBean);
	}

	@Test
	void testSpringBeanJobFactory() {

		SpringBeanJobFactory springBeanJobFactory = quartzConfiguration.springBeanJobFactory();
		assertNotNull(springBeanJobFactory);
	}
}