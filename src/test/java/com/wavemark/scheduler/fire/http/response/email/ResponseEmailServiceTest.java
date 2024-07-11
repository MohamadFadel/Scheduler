package com.wavemark.scheduler.fire.http.response.email;

import com.cardinalhealth.service.support.models.Email;
import com.cardinalhealth.service.support.service.email.impl.EmailServiceImpl;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import com.wavemark.scheduler.testing.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.mail.MailSendException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class ResponseEmailServiceTest {

    @Mock
    private ResponseEmailFactory responseEmailFactory;

    @Mock
    private EmailServiceImpl emailService;

    @Mock
    private Logger log;

    @InjectMocks
    private ResponseEmailService responseEmailService;

    @Test
    void testSendEmailMessage() throws Exception {
        Email email = DataUtil.generateEmail();
        Email developerEmail = DataUtil.generateDeveloperEmail();

        Mockito.doReturn(email).when(responseEmailFactory).generateResponseCustomerEmail(any(), anyInt(), any());
        Mockito.doReturn(developerEmail).when(responseEmailFactory)
                .generateResponseSupportEmail(any(), any(), any());

        Mockito.doNothing().when(emailService).send(any(Email.class));

        responseEmailService.sendEmailMessage(DataUtil.generateTask(), DataUtil.generateResponse(), "endpointName");

        verify(log).info("[SUCCESS] Customer email sent.");
        verify(log).info("[SUCCESS] Support email sent.");
        verifyNoMoreInteractions(log);
    }

    @Test
    void testSendEmailMessage_customerEmailFails() throws Exception {
        Email email = DataUtil.generateEmail();
        Email developerEmail = DataUtil.generateDeveloperEmail();

        Mockito.doReturn(email).when(responseEmailFactory).generateResponseCustomerEmail(any(), anyInt(), any());
        Mockito.doReturn(developerEmail).when(responseEmailFactory)
                .generateResponseSupportEmail(any(), any(), any());

        Mockito.doThrow(new MailSendException("")).when(emailService).send(email);
        Mockito.doNothing().when(emailService).send(developerEmail);

        responseEmailService.sendEmailMessage(DataUtil.generateTask(), DataUtil.generateResponse(), "endpointName");

        verify(log).error("[ERROR] Customer email not sent.");
        verify(log).info("[SUCCESS] Support email sent.");
        verifyNoMoreInteractions(log);
    }

    @Test
    void testSendEmailMessage_supportEmailFails() throws Exception {
        Email email = DataUtil.generateEmail();
        Email developerEmail = DataUtil.generateDeveloperEmail();

        Mockito.doReturn(email).when(responseEmailFactory).generateResponseCustomerEmail(any(), anyInt(), any());
        Mockito.doReturn(developerEmail).when(responseEmailFactory)
                .generateResponseSupportEmail(any(), any(), any());

        Mockito.doNothing().when(emailService).send(email);
        Mockito.doThrow(new MailSendException("")).when(emailService).send(developerEmail);

        responseEmailService.sendEmailMessage(DataUtil.generateTask(), DataUtil.generateResponse(), "endpointName");

        verify(log).info("[SUCCESS] Customer email sent.");
        verify(log).error("[ERROR] Support email not sent.");
        verifyNoMoreInteractions(log);
    }

    @Test
    void testSendEmailMessage_responseCode200() throws Exception {
        Email developerEmail = DataUtil.generateDeveloperEmail();

        Mockito.doReturn(developerEmail).when(responseEmailFactory)
                .generateResponseSupportEmail(any(), any(), any());

        Mockito.doNothing().when(emailService).send(developerEmail);

        responseEmailService.sendEmailMessage(DataUtil.generateTask(), DataUtil.generateSuccessResponse(), "endpointName");

        verify(log).info("[SUCCESS] Support email sent.");
        verifyNoMoreInteractions(log);
    }

    @Test
    void testSendEmailMessage_noEmails() throws Exception {
        Email developerEmail = DataUtil.generateDeveloperEmail();
        Task task = DataUtil.generateTask();
        task.setEmailToList("");

        Mockito.doReturn(developerEmail).when(responseEmailFactory)
                .generateResponseSupportEmail(any(), any(), any());

        Mockito.doNothing().when(emailService).send(developerEmail);

        responseEmailService.sendEmailMessage(task, DataUtil.generateResponse(), "endpointName");

        verify(log).info("[SUCCESS] Support email sent.");
        verifyNoMoreInteractions(log);
    }

    @Test
    void testSendEmailMessage_allEmailsFail() throws Exception {
        Email email = DataUtil.generateEmail();
        Email developerEmail = DataUtil.generateDeveloperEmail();

        Mockito.doReturn(email).when(responseEmailFactory).generateResponseCustomerEmail(any(), anyInt(), any());
        Mockito.doReturn(developerEmail).when(responseEmailFactory)
                .generateResponseSupportEmail(any(), any(), any());

        Mockito.doThrow(new MailSendException("")).when(emailService).send(email);
        Mockito.doThrow(new MailSendException("")).when(emailService).send(developerEmail);

        responseEmailService.sendEmailMessage(DataUtil.generateTask(), DataUtil.generateResponse(), "endpointName");

        verify(log).error("[ERROR] Customer email not sent.");
        verify(log).error("[ERROR] Support email not sent.");
        verifyNoMoreInteractions(log);
    }

}