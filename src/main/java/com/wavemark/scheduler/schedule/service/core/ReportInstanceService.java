package com.wavemark.scheduler.schedule.service.core;

import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.dto.request.ReportInstanceInput;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.ReportInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReportInstanceService {
    private final ReportInstanceRepository reportInstanceRepository;

    public ReportInstanceConfig buildReportInstanceConfig(ReportInstanceInput reportInstanceInput){
        return  ReportInstanceConfig.builder()
                .actionname(reportInstanceInput.getActionName())
                .classname(reportInstanceInput.getClassName())
                .emailformat(reportInstanceInput.getEmailFormat())
                .emailifempty(reportInstanceInput.getEmailIfEmpty())
                .comments(reportInstanceInput.getComments())
                .emailrecipients(reportInstanceInput.getEmailRecipients())
                .id(reportInstanceInput.getReportInstanceId())
                .status(reportInstanceInput.getStatus())
                .reportstate(reportInstanceInput.getReportState().toCharArray())
                .reportinstancename(reportInstanceInput.getReportInstanceName())
                .reportname(reportInstanceInput.getReportName())
                .timezonename(reportInstanceInput.getTimezone())
                .lastupdateddate(new Timestamp(new Date().getTime()))
                .userid(SecurityUtilsV2.getCurrentAuthUser())
                .endpointid(SecurityUtilsV2.getCurrentAuthDepartment())
                .build();
    }

    public ReportInstanceConfig updateReportInstanceConfig(ReportInstanceConfig dbReportInstanceConfig, ReportInstanceInput reportInstanceInput){

        ReportInstanceConfig reportInstanceConfig = new ReportInstanceConfig();
//        String cronExpression = cronExpressionService.generateCronExpression(reportInstanceInput.getTaskFrequencyInput());

        reportInstanceConfig.setId(dbReportInstanceConfig.getId());
        reportInstanceConfig.setReportname(reportInstanceInput.getReportName());
        reportInstanceConfig.setLastRunLogId(dbReportInstanceConfig.getLastRunLogId());
        reportInstanceConfig.setLastSuccessfulRunLogId(dbReportInstanceConfig.getLastSuccessfulRunLogId());
        reportInstanceConfig.setEndpointid(dbReportInstanceConfig.getEndpointid());

        reportInstanceConfig.setStatus(dbReportInstanceConfig.getStatus());
        reportInstanceConfig.setLogId(dbReportInstanceConfig.getLogId());

        reportInstanceConfig.setComments(reportInstanceInput.getComments());
        reportInstanceConfig.setLastupdateddate(new Timestamp(new Date().getTime()));
//        reportInstanceConfig.setLastUpdatedBy(SecurityUtilsV2.getWebAppUserInfo().getFullName());
//        reportInstanceConfig.setConfiguration(reportInstanceInput.getBodyParam().toCharArray());
//        reportInstanceConfig.setCronExpression(cronExpression);
//        reportInstanceConfig.setCreatedBy(dbReportInstanceConfig.getCreatedBy());
//        reportInstanceConfig.setCreatedOn(dbReportInstanceConfig.getCreatedOn());
//        reportInstanceConfig.setTaskTypeId(dbReportInstanceConfig.getTaskTypeId());

        reportInstanceConfig.setEmailrecipients(reportInstanceInput.getEmailRecipients());
        reportInstanceConfig.setTimezonename(reportInstanceInput.getTimezone());
        reportInstanceConfig.setActionname(reportInstanceInput.getActionName());
        reportInstanceConfig.setClassname(reportInstanceInput.getClassName());
        reportInstanceConfig.setEmailformat(reportInstanceInput.getEmailFormat());
        reportInstanceConfig.setEmailifempty(reportInstanceInput.getEmailIfEmpty());
        reportInstanceConfig.setReportstate(reportInstanceInput.getReportState().toCharArray());
        reportInstanceConfig.setReportinstancename(reportInstanceInput.getReportInstanceName());
        reportInstanceConfig.setUserid(SecurityUtilsV2.getCurrentAuthUser());

        return reportInstanceConfig;
    }

    public void saveReportInstance(ReportInstanceConfig reportInstanceConfig) {
        reportInstanceRepository.save(reportInstanceConfig);
    }

    public Long getNewReportId(){
        return reportInstanceRepository.getReportIdNextValueSequence();
    }

    public ReportInstanceConfig findReportById(Long reportId) throws EntryNotFoundException {
        return reportInstanceRepository.findById(reportId).orElseThrow(EntryNotFoundException::new);
    }

}


