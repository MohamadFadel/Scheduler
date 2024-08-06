//package com.wavemark.scheduler.fire.http.response.email;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//
//import java.io.IOException;
//
//import com.wavemark.scheduler.cron.constant.Frequency;
//import com.wavemark.scheduler.cron.dto.CronDescription;
//import com.wavemark.scheduler.cron.exception.CronExpressionException;
//import com.wavemark.scheduler.cron.service.CronExpressionService;
//import com.wavemark.scheduler.fire.http.response.HttpResponse;
//import com.wavemark.scheduler.fire.http.response.message.ResponseMessage;
//import com.wavemark.scheduler.fire.http.response.message.ResponseMessageFactory;
//import com.wavemark.scheduler.schedule.domain.entity.Task;
//import com.wavemark.scheduler.testing.util.DataUtil;
//
//import com.cardinalhealth.service.support.configuration.EmailConfiguration;
//import com.cardinalhealth.service.support.models.Email;
//import org.apache.commons.lang3.StringUtils;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.core.env.Environment;
//import org.springframework.test.util.ReflectionTestUtils;
//
//@ExtendWith(MockitoExtension.class)
//class ResponseEmailFactoryTest {
//
//	@Mock
//	private CronExpressionService cronExpressionService;
//	@Mock
//	private EmailConfiguration emailConfiguration;
//	@Mock
//	private Environment env;
//
//	@InjectMocks
//	private ResponseEmailFactory responseEmailFactory;
//
//	@Test
//	void generateResponseCustomerEmailInstance() throws CronExpressionException, IOException {
//		CronDescription cronDescription = new CronDescription();
//		cronDescription.setFrequency(Frequency.DAILY);
//		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
//		Mockito.when(emailConfiguration.getFromEmailAddress()).thenReturn("");
//
//		Email email = responseEmailFactory.generateResponseCustomerEmail(DataUtil.generateTask(), 200, "endpointName");
//
//		assertNotNull(email);
//		assertEquals(email.getEmailSubject(), "[Wavemark] (Task Success) testName Run Successfully for department endpointName");
//	}
//
//	@Test
//	void generateResponseCustomerEmailInstance_Failure() throws CronExpressionException, IOException {
//
//		CronDescription cronDescription = new CronDescription();
//		cronDescription.setFrequency(Frequency.DAILY);
//		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
//		Mockito.when(emailConfiguration.getFromEmailAddress()).thenReturn("");
//
//		Task task = DataUtil.generateTask();
//		task.setDescription(null);
//		Email email = responseEmailFactory.generateResponseCustomerEmail(task, 401, "endpointName");
//
//		assertNotNull(email);
//		assertEquals(email.getEmailSubject(), "[Wavemark] (Task Failure) testName Failed to Run for department endpointName");
//	}
//
//	@Test
//	void generateResponseDeveloperEmailInstance() throws CronExpressionException {
//
//		String[] activeProfiles = {"stage"};
//		Mockito.when(env.getActiveProfiles()).thenReturn(activeProfiles);
//		ReflectionTestUtils.setField(responseEmailFactory, "host", "hostName");
//
//		CronDescription cronDescription = new CronDescription();
//		cronDescription.setFrequency(Frequency.DAILY);
//		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
//		Mockito.when(emailConfiguration.getFromEmailAddress()).thenReturn("");
//		Mockito.when(emailConfiguration.getSupportEmails()).thenReturn("");
//
//		Email email = responseEmailFactory.generateResponseSupportEmail(DataUtil.generateTask(),
//				HttpResponse.builder().code(412).message("Null pointer Exception").cause("Null pointer Exception").build(), "endpointName");
//
//		assertNotNull(email);
//		assertEquals(email.getEmailSubject(), "[Wavemark - Support] (Task Failure) testName Failed to Run for department endpointName - Using host: hostName");
//	}
//
//	@Test
//	void testGenerateMessage_ResponseMessage()
//	{
//		HttpResponse httpResponse = HttpResponse.builder().code(400).message("Unexpected error").cause("Unexpected error").build();
//		ResponseMessage responseMessage = ResponseMessageFactory.generateMessage(httpResponse);
//
//		assertNotNull(responseMessage);
//		assertEquals(responseMessage.getType(), "Code is: 400");
//		assertEquals(responseMessage.getDescription(), "Unexpected error");
//		assertEquals(responseMessage.getCause(), "Unexpected error");
//	}
//
//	@Test
//	void testGetFrequency() throws CronExpressionException {
//
//		CronDescription cronDescription = new CronDescription();
//		cronDescription.setFrequency(Frequency.DAILY);
//		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
//
//		String frequency = responseEmailFactory.getFrequency(DataUtil.generateTask());
//		assertEquals(StringUtils.capitalize(Frequency.DAILY.getCronExpression()), frequency);
//	}
//
//}