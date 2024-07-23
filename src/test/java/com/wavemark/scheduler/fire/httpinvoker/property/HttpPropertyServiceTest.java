package com.wavemark.scheduler.fire.httpinvoker.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.TaskTypeRepository;
import com.wavemark.scheduler.schedule.service.core.TaskTypeService;
import com.wavemark.scheduler.testing.util.DataUtil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HttpPropertyServiceTest {

	@Mock
	TaskTypeService taskTypeService;

	@Mock
	TaskTypeRepository taskTypeRepository;

	@InjectMocks
	HttpPropertyService httpPropertyService;


	@Test
	void testFetchTaskRequestProperty() throws EntryNotFoundException {

		HttpPropertyService httpPropertyServiceSpy = Mockito.spy(httpPropertyService);
		Mockito.doReturn("url").when(httpPropertyServiceSpy).getTaskUrlInstance(any());

		HttpProperty httpProperty = httpPropertyServiceSpy.fetchTaskRequestProperty(DataUtil.generateJobDataMap(), "authToken");

		assertNotNull(httpProperty);
		assertEquals("taskName", httpProperty.getTaskName());
		assertEquals("{ testBodyParam: test }", httpProperty.getBodyParam());
		assertEquals("url?access_token=authToken", httpProperty.getUrl());
		assertEquals("endpointName", httpProperty.getEndpointName());
	}

	@Test
	void testFetchAuthTokenRequestProperty() {

		HttpProperty httpProperty = httpPropertyService.fetchAuthTokenRequestProperty(DataUtil.generateJobDataMap());

		assertNotNull(httpProperty);
		assertEquals("oauth", httpProperty.getTaskName());
		assertEquals("nullwarden/oauth/token?username=endpointId&grant_type=password", httpProperty.getUrl());
	}

    @Test
    void testGetTaskUrlInstance() {

        when(taskTypeRepository.findByTaskType(any())).thenReturn(DataUtil.generateTaskType());
        String url = httpPropertyService.getTaskUrlInstance("Auto-Order_endpointId");

        assertNotNull(url);
		assertEquals("null/test", url);
	}
}