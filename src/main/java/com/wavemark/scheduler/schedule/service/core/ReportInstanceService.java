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
                .reportstate(reportInstanceInput.getReportState().toString().toCharArray())
                .reportinstancename(reportInstanceInput.getReportInstanceName())
                .reportname(reportInstanceInput.getReportName())
                .timezonename(reportInstanceInput.getTimezone())
                .lastupdateddate(new Timestamp(new Date().getTime()))
                .userid(SecurityUtilsV2.getCurrentAuthUser())
                .endpointid(SecurityUtilsV2.getCurrentAuthDepartment())
                .build();
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


