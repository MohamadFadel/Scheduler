package com.wavemark.scheduler.common.config;

import static org.mockito.Mockito.doReturn;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
class AutoWiringSpringBeanJobFactoryTest {

	@Mock
	private AutowireCapableBeanFactory beanFactory;
	@Mock
	private ApplicationContext applicationContext;
	@Mock
	private TriggerFiredBundle bundle;

	@InjectMocks
	private AutoWiringSpringBeanJobFactory autoWiringSpringBeanJobFactory;


	@Test
	void setApplicationContext() {
		doReturn(beanFactory).when(applicationContext).getAutowireCapableBeanFactory();

		autoWiringSpringBeanJobFactory.setApplicationContext(applicationContext);
	}

//	@Test
//	void testCreateJobInstance() throws Exception {
//
//		doNothing().when(beanFactory).autowireBean(any());
//
////		autoWiringSpringBeanJobFactory = Mockito.spy(autoWiringSpringBeanJobFactory);
//		doReturn(new Object()).when(autoWiringSpringBeanJobFactory).superCreateJobInstance(any());
//
////		doNothing().when(beanFactory).autowireBean(any());
////		ReflectionSupport.invokeMethod(
////				((SpringBeanJobFactory) autoWiringSpringBeanJobFactory).getClass()
////						.getSuperclass()
////						.getDeclaredMethod("createJobInstance", TriggerFiredBundle.class),
////				doReturn(new Object()).when((SpringBeanJobFactory) autoWiringSpringBeanJobFactory), triggerFiredBundle);
//
//		autoWiringSpringBeanJobFactory.createJobInstance(bundle);
//	}
}