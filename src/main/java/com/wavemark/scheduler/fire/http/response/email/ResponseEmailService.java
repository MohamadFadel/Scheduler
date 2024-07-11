package com.wavemark.scheduler.fire.http.response.email;

import com.cardinalhealth.service.support.models.Email;
import com.cardinalhealth.service.support.service.email.impl.EmailServiceImpl;
import com.wavemark.scheduler.fire.http.response.HttpResponse;
import com.wavemark.scheduler.schedule.domain.entity.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseEmailService {

    private final ResponseEmailFactory responseEmailFactory;
    private final EmailServiceImpl emailService;

    public void sendEmailMessage(Task task, HttpResponse httpResponse, String endpointName) {

        if (!httpResponse.isSuccess() && StringUtils.isNotBlank(task.getEmailToList())) {
            try {
                Email responseCustomerEmail = responseEmailFactory.generateResponseCustomerEmail(task, httpResponse.getCode(), endpointName);

                emailService.send(responseCustomerEmail);

                log.info("[SUCCESS] Customer email sent.");
            } catch (Exception ex) {
                log.error("[ERROR] Customer email not sent.");
            }
        }

        try {
            Email responseSupportEmail = responseEmailFactory.generateResponseSupportEmail(task, httpResponse, endpointName);

            emailService.send(responseSupportEmail);

            log.info("[SUCCESS] Support email sent.");
        } catch (Exception ex) {
            log.error("[ERROR] Support email not sent.");
        }
    }

}