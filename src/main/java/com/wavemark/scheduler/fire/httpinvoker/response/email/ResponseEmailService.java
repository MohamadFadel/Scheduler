package com.wavemark.scheduler.fire.httpinvoker.response.email;

import com.wavemark.scheduler.fire.httpinvoker.response.HttpResponse;
import com.wavemark.scheduler.schedule.domain.entity.Task;

import com.cardinalhealth.service.support.models.Email;
import com.cardinalhealth.service.support.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseEmailService {

    private final ResponseEmailFactory responseEmailFactory;
    private final EmailService emailService;

    public void sendEmailMessage(Task task, HttpResponse httpResponse, String endpointName) {

        if (!httpResponse.isSuccess() && StringUtils.isNotBlank(task.getEmailToList())) {
            try {
                Email responseCustomerEmail = responseEmailFactory.generateResponseCustomerEmailInstance(task, httpResponse.getCode(), endpointName);

                emailService.send(responseCustomerEmail);

                log.info("[SUCCESS] Customer email sent.");
            } catch (Exception ex) {
                log.error("[ERROR] Customer email not sent.");
            }
        }

        try {
            Email responseSupportEmail = responseEmailFactory.generateResponseSupportEmailInstance(task, httpResponse, endpointName);

            emailService.send(responseSupportEmail);

            log.info("[SUCCESS] Support email sent.");
        } catch (Exception ex) {
            log.error("[ERROR] Support email not sent.");
        }
    }

}