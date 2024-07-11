package com.wavemark.scheduler.fire.http.response.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import com.wavemark.scheduler.common.constant.DateUtil;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.fire.http.response.HttpResponse;
import com.wavemark.scheduler.fire.http.response.message.ResponseMessage;
import com.wavemark.scheduler.fire.http.response.message.ResponseMessageFactory;
import com.wavemark.scheduler.schedule.domain.entity.Task;

import com.cardinalhealth.service.support.configuration.EmailConfiguration;
import com.cardinalhealth.service.support.constant.ContentType;
import com.cardinalhealth.service.support.models.Email;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseEmailFactory {

    private final CronExpressionService cronExpressionService;
    private final EmailConfiguration emailConfiguration;

    private final Environment env;
    @Value("${application.host}")
    private String host;

    public Email generateResponseCustomerEmail(Task task, int responseCode, String endpointName)
            throws CronExpressionException, IOException {
        return Email.builder()
                .fromEmailAddress(emailConfiguration.getFromEmailAddress())
                .toEmailAddresses(task.getEmailToList())
                .emailSubject(getCustomerEmailSubject(task, responseCode, endpointName))
                .emailMessage(getCustomerEmailBody(task, ResponseMessageFactory.generateMessage(responseCode)))
                .emailContentType(ContentType.HTML)
                .build();
    }

    public Email generateResponseSupportEmail(Task task, HttpResponse httpResponse, String endpointName)
            throws CronExpressionException {
        int responseCode = httpResponse.getCode();

        return Email.builder()
                .fromEmailAddress(emailConfiguration.getFromEmailAddress())
                .toEmailAddresses(emailConfiguration.getSupportEmails())
                .emailSubject(getSupportEmailSubject(task, responseCode, endpointName))
                .emailMessage(getSupportEmailBody(task, responseCode, ResponseMessageFactory.generateMessage(httpResponse)))
                .emailContentType(ContentType.PLAIN)
                .build();
    }

    protected String getCustomerEmailSubject(Task task, int responseCode, String endpointName) {
        return "[Wavemark] (Task " + stateMessage(responseCode) + ") " + task.getTaskName().split("_")[0] + " "
                + stateMessagePast(responseCode) + " for department " + endpointName;
    }

    protected String getSupportEmailSubject(Task task, int responseCode, String endpointName) {
        String[] activeProfiles = env.getActiveProfiles();
        String activeProfile = activeProfiles.length > 0 ? activeProfiles[0] : "";
        String hostName = Objects.nonNull(host) ? host : "";
        String serverName = !activeProfile.equals("prod") ? " - Using host: " + hostName : "";

        return "[Wavemark - Support] (Task " + stateMessage(responseCode) + ") " + task.getTaskName().split("_")[0] +
                " " + stateMessagePast(responseCode) + " for department " + endpointName + serverName;
    }

    protected String stateMessage(int responseCode) {
        return responseCode == 200 ? "Success" : "Failure";
    }

    protected String stateMessagePast(int responseCode) {
        return responseCode == 200 ? "Run Successfully" : "Failed to Run";
    }

    protected String getCustomerEmailBody(Task task, ResponseMessage responseMessage)
            throws CronExpressionException, IOException {
        InputStream file = new ClassPathResource("email.html").getInputStream();
        String emailHtml = IOUtils.toString(file, Charsets.UTF_8);

        emailHtml = emailHtml.replace("${ERROR_TYPE}", responseMessage.getType());
        emailHtml = emailHtml.replace("${TASK_ID}", task.getTaskId().toString());
        emailHtml = emailHtml.replace("${TASK_TYPE}", task.getTaskName().split("_")[0]);
        emailHtml = emailHtml.replace("${DESCRIPTION}",
                task.getDescription() != null ? task.getDescription() : "None");
        emailHtml = emailHtml.replace("${FREQUENCY}", getFrequency(task));
        emailHtml = emailHtml.replace("${NEXT_SCHEDULED_RUN}",
                DateUtil.convertToZonedDateTime(task.getNextScheduledRun(), task.getHospitalDepartmentTimeZone()));
        emailHtml = emailHtml.replace("${STATE}", task.getTaskStatus());
        emailHtml = emailHtml.replace("${ERROR_DESCRIPTION}", responseMessage.getDescription());

        return emailHtml;
    }

    protected String getSupportEmailBody(Task task, int responseCode, ResponseMessage responseMessage) throws CronExpressionException {
        String emailBody = "The scheduled task " + task.getTaskName().split("_")[0] + " " + stateMessagePast(responseCode) + " as planned.\n";
        emailBody += "Below are the details of the task:\n";
        emailBody += "\n" + "\n" + "Response Type: " + responseMessage.getType();
        emailBody += "\n" + "Task ID: " + task.getTaskId();
        emailBody += "\n" + "Task Type: " + task.getTaskName().split("_")[0];
        emailBody += "\n" + "Description: " + (task.getDescription() != null ? task.getDescription() : "None");
        emailBody += "\n" + "Frequency: " + getFrequency(task);
        emailBody += "\n" + "Next Scheduled Run: " + DateUtil.convertToZonedDateTime(task.getNextScheduledRun(), task.getHospitalDepartmentTimeZone());
        emailBody += "\n" + "State: " + task.getTaskStatus();
        emailBody += "\n" + "\n" + "\n" + responseMessage.getDescription();
        if (Objects.nonNull(responseMessage.getCause()))
            emailBody += "\n" + "\n" + "Failure Cause: " + responseMessage.getCause();
        emailBody += "\n" + "\n" + "\n" + "Thank you for your attention to this matter.";
        emailBody += "\n" + "\n" + "\n" + "WaveMark Support Team";

        return emailBody;
    }

    protected String getFrequency(Task task) throws CronExpressionException {
        CronDescription cronDescription = cronExpressionService.reverseCronExpression(task.getCronExpression(), task.getHospitalDepartmentTimeZone().getID());
        return cronDescription.getFrequency().getCapitalizedCronExpression();
    }

}
