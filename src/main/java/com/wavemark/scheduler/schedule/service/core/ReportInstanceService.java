package com.wavemark.scheduler.schedule.service.core;

import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import com.wavemark.scheduler.schedule.exception.EntryNotFoundException;
import com.wavemark.scheduler.schedule.repository.ReportInstanceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReportInstanceService {
    private final ReportInstanceRepository reportInstanceRepository;

    public void saveReportInstance(ReportInstanceConfig reportInstanceConfig) {
        reportInstanceRepository.save(reportInstanceConfig);
    }

    public Long getNewReportId(){
        return reportInstanceRepository.getReportIdNextValueSequence();
    }

    public ReportInstanceConfig findReportById(Long reportId) throws EntryNotFoundException {
        return reportInstanceRepository.findById(reportId).orElseThrow(EntryNotFoundException::new);
    }

    public void deleteReportInstance(String reportId){

    }

}


