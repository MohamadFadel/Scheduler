package com.wavemark.scheduler.schedule.repository;

import com.wavemark.scheduler.schedule.domain.entity.ReportInstanceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportInstanceRepository extends JpaRepository<ReportInstanceConfig, Long> {
    String REPORTINSTANCECONFIG_REPORTID_NEXTVAL = "SELECT REPORTINSTANCECONFIG_REPORTID_SEQ.NEXTVAL AS ReportId FROM DUAL";
    @Query(nativeQuery = true, value = REPORTINSTANCECONFIG_REPORTID_NEXTVAL)
    Long getReportIdNextValueSequence();
}
