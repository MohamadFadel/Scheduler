package com.wavemark.scheduler.fire.httpinvoker.response.email;

import java.util.Objects;

import com.wavemark.scheduler.common.constant.DateUtil;
import com.wavemark.scheduler.cron.dto.CronDescription;
import com.wavemark.scheduler.cron.exception.CronExpressionException;
import com.wavemark.scheduler.cron.service.CronExpressionService;
import com.wavemark.scheduler.fire.httpinvoker.response.HttpResponse;
import com.wavemark.scheduler.schedule.domain.entity.Task;

import com.cardinalhealth.service.support.configuration.EmailConfiguration;
import com.cardinalhealth.service.support.constant.ContentType;
import com.cardinalhealth.service.support.models.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseEmailFactory {

    private final CronExpressionService cronExpressionService;
    private final EmailConfiguration emailConfiguration;

    public Email generateResponseCustomerEmailInstance(Task task, int responseCode, String endpointName) throws CronExpressionException {
        return Email.builder()
                .fromEmailAddress(emailConfiguration.getFromEmailAddress())
                .toEmailAddresses(task.getEmailToList())
                .emailSubject(getCustomerEmailSubject(task, responseCode, endpointName))
                .emailMessage(getEmailMessage(task, responseCode, generateMessage(responseCode)))
                .emailContentType(ContentType.PLAIN)
                .build();
    }

    public Email generateResponseSupportEmailInstance(Task task, HttpResponse httpResponse, String endpointName)
            throws CronExpressionException {

        int responseCode = httpResponse.getCode();
        return Email.builder()
                .fromEmailAddress(emailConfiguration.getFromEmailAddress())
                .toEmailAddresses(emailConfiguration.getSupportEmails())
                .emailSubject(getSupportEmailSubject(task, responseCode, endpointName))
                .emailMessage(getEmailMessage(task, responseCode, generateMessage(httpResponse)))
                .emailContentType(ContentType.PLAIN)
                .build();
    }

    protected String getCustomerEmailSubject(Task task, int responseCode, String endpointName) {
        return "[Wavemark] (Task " + stateMessage(responseCode) + ") " + task.getTaskName().split("_")[0] + " "
                + stateMessagePast(responseCode) + " for department " + endpointName;
    }

    protected String getSupportEmailSubject(Task task, int responseCode, String endpointName) {
        return "[Wavemark - Support] (Task " + stateMessage(responseCode) + ") " + task.getTaskName().split("_")[0] +
                " " + stateMessagePast(responseCode) + " for department " + endpointName;
    }

    protected String stateMessage(int responseCode) {
        return responseCode == 200 ? "Success" : "Failure";
    }

    protected String stateMessagePast(int responseCode) {
        return responseCode == 200 ? "Run Successfully" : "Failed to Run";
    }

    protected String getEmailMessage(Task task, int responseCode, Message message) throws CronExpressionException {

        String emailMessage = "The scheduled task " + task.getTaskName().split("_")[0] + " " + stateMessagePast(responseCode) + " as planned.\n";
        emailMessage += "Below are the details of the task:\n";
        emailMessage += "\n" + "\n" + "Response Type: " + message.getType();
        emailMessage += "\n" + "Task ID: " + task.getTaskId();
        emailMessage += "\n" + "Task Type: " + task.getTaskName().split("_")[0];
        emailMessage += "\n" + "Description: " + (task.getDescription() != null ? task.getDescription() : "None") ;
        emailMessage += "\n" + "Frequency: " + getFrequency(task.getCronExpression(), task.getHospitalDepartmentTimeZone().getID());
        emailMessage += "\n" + "Next Scheduled Run: " + DateUtil.convertToZonedDateTime(task.getNextScheduledRun(), task.getHospitalDepartmentTimeZone());
        emailMessage += "\n" + "State: " + task.getTaskStatus();
        emailMessage += "\n" + "\n" + "\n" + message.getTypeDescription();
        if (Objects.nonNull(message.typeCause))
            emailMessage += "\n" + "\n" + "Failure Cause: "  + message.getTypeCause();
        emailMessage += "\n" + "\n" + "\n" + "Thank you for your attention to this matter.";
        emailMessage += "\n" + "\n" + "\n" + "WaveMark Support Team";

        return emailMessage;
    }

//    protected String getSuccessEmailMessage(Task task) throws CronExpressionException {
//
//        String emailMessage = "The scheduled task" + task.getName().split("_")[0] + " has run successfully as planned.\n";
//        emailMessage += "Below are the details of the task:\n";
//        emailMessage += "\n" + "Task ID: " + task.getId();
//        emailMessage += "\n" + "\n" + "Task Type: " + task.getName().split("_")[0];
//        emailMessage += "\n" + "Description: " + task.getDescription();
//        emailMessage += "\n" + "Frequency: " + getFrequency(task.getCronExpression(), task.getTimeZone().getID());
//        emailMessage += "\n" + "Next Scheduled Run: " + DateUtil.convertToZonedDateTime(task.getNextScheduledRun(), task.getTimeZone());
//        emailMessage += "\n" + "State: " + task.getState();
//        emailMessage += "\n" + "\n" + "\n" + "Thank you for your attention to this matter.";
//        emailMessage += "\n" + "\n" + "\n" + "WaveMark Support Team";
//
//        return emailMessage;
//    }

    protected Message generateMessage(int responseCode) {

        switch (responseCode) {
            case 401:
                return new Message("Department authentication error",
                        "Task has failed due to an authentication error. " + "\n"
                                + "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
            case 408:
                return new Message("Timeout error",
                        "Task has failed due to a timeout error. " + "\n"
                                + "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
            case 412:
                return new Message("Department configuration error",
                        "Task has failed because your department is not configured to run this task type. " + "\n"
                                + "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
            case 503:
                return new Message("Service unavailable error",
                        "Task has failed due to service unavailability. " + "\n"
                                + "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
            case 505:
                return new Message("Database error",
                        "Task has failed due to a database error. " + "\n"
                                + "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
            case 506:
                return new Message("Database connection error",
                        "Task has failed due to a database connection error. " + "\n"
                                + "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
            default:
                return new Message("Unexpected error", "Task has failed due to an unexpected error. " + "\n"
                        + "You can still order products from the Smart Order tool or reach out to our support team at support@wavemark.net if the issue persists.");
        }
    }

    protected Message generateMessage(HttpResponse httpResponse) {

        return new Message("Code is: " + httpResponse.getCode(), httpResponse.getMessage(), httpResponse.getCause());
    }

    protected String getFrequency(String cronExpressionString, String timezone) throws CronExpressionException {
        CronDescription cronDescription = cronExpressionService.reverseCronExpression(cronExpressionString, timezone);
        return cronDescription.getFrequency().getCronExpression();
    }

    @Data
    @AllArgsConstructor
    protected static class Message {
        private String type;
        private String typeDescription;
        private String typeCause;

        public Message(String type, String typeDescription)
        {
            this.type = type;
            this.typeDescription = typeDescription;
        }
    }

}
