package com.wavemark.scheduler.common.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import com.cardinalhealth.service.support.configuration.HttpConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@ExtendWith(MockitoExtension.class)
class Oauth2ConfigurationTest {

	@Mock
	private HttpConfiguration httpConfiguration;

	@InjectMocks
	private Oauth2Configuration oauth2Configuration;


	@Test
	void testConfigure() throws Exception {

		doNothing().when(httpConfiguration).configure(any(), any(), any());
		HttpSecurity http = mock(HttpSecurity.class);

		oauth2Configuration.configure(http);
	}
}