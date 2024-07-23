package com.wavemark.scheduler.fire.httpinvoker.response.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import com.wavemark.scheduler.cron.constant.Frequency;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.fire.httpinvoker.response.HttpResponse;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.testing.util.DataUtil;

import com.cardinalhealth.service.support.configuration.EmailConfiguration;
import com.cardinalhealth.service.support.models.Email;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResponseEmailFactoryTest {

	@Mock
	private CronExpressionService cronExpressionService;
	@Mock
	private EmailConfiguration emailConfiguration;

	@InjectMocks
	private ResponseEmailFactory responseEmailFactory;

	@Test
	void generateResponseCustomerEmailInstance() throws CronExpressionException {
		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		Mockito.when(emailConfiguration.getFromEmailAddress()).thenReturn("");

		Email email = responseEmailFactory.generateResponseCustomerEmailInstance(DataUtil.generateTask(), 200, "endpointName");

		assertNotNull(email);
		assertEquals(email.getEmailSubject(), "[Wavemark] (Task Success) testName Run Successfully for department endpointName");
	}

	@Test
	void generateResponseCustomerEmailInstance_Failure() throws CronExpressionException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		Mockito.when(emailConfiguration.getFromEmailAddress()).thenReturn("");

		Task task = DataUtil.generateTask();
		task.setDescription(null);
		Email email = responseEmailFactory.generateResponseCustomerEmailInstance(task, 401, "endpointName");

		assertNotNull(email);
		assertEquals(email.getEmailSubject(), "[Wavemark] (Task Failure) testName Failed to Run for department endpointName");
	}

	@Test
	void generateResponseDeveloperEmailInstance() throws CronExpressionException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);
		Mockito.when(emailConfiguration.getFromEmailAddress()).thenReturn("");
		Mockito.when(emailConfiguration.getSupportEmails()).thenReturn("");

		Email email = responseEmailFactory.generateResponseSupportEmailInstance(DataUtil.generateTask(),
				HttpResponse.builder().code(412).message("Null pointer Exception").cause("Null pointer Exception").build(), "endpointName");

		assertNotNull(email);
		assertEquals(email.getEmailSubject(), "[Wavemark - Support] (Task Failure) testName Failed to Run for department endpointName");
	}

	@Test
	void testGenerateMessage_ResponseMessage()
	{
		HttpResponse httpResponse = HttpResponse.builder().code(400).message("Unexpected error").cause("Unexpected error").build();
		ResponseEmailFactory.Message message = responseEmailFactory.generateMessage(httpResponse);

		assertNotNull(message);
		assertEquals(message.getType(), "Code is: 400");
		assertEquals(message.getTypeDescription(), "Unexpected error");
		assertEquals(message.getTypeCause(), "Unexpected error");
	}

	@Test
	void testGenerateMessage_Default()
	{
		ResponseEmailFactory.Message message = responseEmailFactory.generateMessage(400);

		assertNotNull(message);
		assertEquals(message.getType(), "Unexpected error");
		assertEquals(message.getTypeDescription(), "Task has failed due to an unexpected error. " + "\n"
				+ "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
	}

	@Test
	void testGenerateMessage_401()
	{
		ResponseEmailFactory.Message message = responseEmailFactory.generateMessage(401);

		assertNotNull(message);
		assertEquals(message.getType(), "Department authentication error");
		assertEquals(message.getTypeDescription(), "Task has failed due to an authentication error. " + "\n"
				+ "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
	}

	@Test
	void testGenerateMessage_408()
	{
		ResponseEmailFactory.Message message = responseEmailFactory.generateMessage(408);

		assertNotNull(message);
		assertEquals(message.getType(), "Timeout error");
		assertEquals(message.getTypeDescription(), "Task has failed due to a timeout error. " + "\n"
				+ "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
	}

	@Test
	void testGenerateMessage_412()
	{
		ResponseEmailFactory.Message message = responseEmailFactory.generateMessage(412);

		assertNotNull(message);
		assertEquals(message.getType(), "Department configuration error");
		assertEquals(message.getTypeDescription(), "Task has failed because your department is not configured to run this task type. " + "\n"
				+ "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
	}

	@Test
	void testGenerateMessage_503()
	{
		ResponseEmailFactory.Message message = responseEmailFactory.generateMessage(503);

		assertNotNull(message);
		assertEquals(message.getType(), "Service unavailable error");
		assertEquals(message.getTypeDescription(), "Task has failed due to service unavailability. " + "\n"
				+ "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
	}

	@Test
	void testGenerateMessage_505()
	{
		ResponseEmailFactory.Message message = responseEmailFactory.generateMessage(505);

		assertNotNull(message);
		assertEquals(message.getType(), "Database error");
		assertEquals(message.getTypeDescription(), "Task has failed due to a database error. " + "\n"
				+ "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
	}

	@Test
	void testGenerateMessage_506()
	{
		ResponseEmailFactory.Message message = responseEmailFactory.generateMessage(506);

		assertNotNull(message);
		assertEquals(message.getType(), "Database connection error");
		assertEquals(message.getTypeDescription(), "Task has failed due to a database connection error. " + "\n"
				+ "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
	}

	@Test
	void testGetFrequency() throws CronExpressionException {

		CronDescription cronDescription = new CronDescription();
		cronDescription.setFrequency(Frequency.DAILY);
		Mockito.when(cronExpressionService.reverseCronExpression(any(), any())).thenReturn(cronDescription);

		String frequency = responseEmailFactory.getFrequency("", "");
		assertEquals(Frequency.DAILY.getCronExpression(), frequency);
	}

}