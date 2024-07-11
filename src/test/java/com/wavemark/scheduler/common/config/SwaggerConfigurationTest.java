package com.wavemark.scheduler.common.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import springfox.documentation.spring.web.plugins.Docket;

class SwaggerConfigurationTest {

	@Test
	void testApi() {

		SwaggerConfiguration swaggerConfiguration = new SwaggerConfiguration();

		Docket docket = swaggerConfiguration.api();
		assertNotNull(docket);
	}
}