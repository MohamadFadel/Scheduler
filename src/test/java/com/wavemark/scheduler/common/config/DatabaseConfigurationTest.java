package com.wavemark.scheduler.common.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class DatabaseConfigurationTest {

	@Mock
	private Environment environment;

	@InjectMocks
	private DatabaseConfiguration databaseConfiguration;

	@Test
	public void testDatabaseConfiguration()
	{
		databaseConfiguration = new DatabaseConfiguration(environment);
		assertNotNull(databaseConfiguration);
	}

//	@Test
//	public void testClientDatasource() throws NamingException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//
//		DataSource dataSource = Mockito.mock(DataSource.class);
//
//		Method method = DatabaseConfiguration.class.getSuperclass().getDeclaredMethod("generateDatasource", String.class);
//		method.setAccessible(true);
////		return method.invoke(targetObject, argObjects);
//
//		when(method.invoke(databaseConfiguration, "anyString()")).thenReturn(dataSource);
////		ReflectionSupport.invokeMethod(
////				((DatabaseConfigurationSupport) databaseConfiguration).getClass()
////						.getSuperclass()
////						.getDeclaredMethod("generateDatasource", String.class),
////				doReturn(dataSource).when((DatabaseConfigurationSupport) databaseConfiguration), "anyString()");
//
//		databaseConfiguration.clientDatasource();
//
//	}

}