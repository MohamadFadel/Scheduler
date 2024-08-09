package com.wavemark.scheduler.schedule.domain.entity;

import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.dto.logdiffable.ReportInstanceDiffable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;
import java.util.TimeZone;

@Data
@Table(name = "Reportinstanceconfig")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportInstanceConfig implements Serializable, Schedulable{
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String reportname;
    @Column(nullable = false)
    private String actionname;
    @Column(nullable = false)
    private String classname;
    @Column(nullable = false)
    private String reportinstancename;
    @Column(nullable = false)
    private String userid;
    @Column(nullable = false)
    private char[] reportstate;
    @Column(nullable = false)
    private String cronschedule;
    @Column(nullable = false)
    private String emailformat;
    @Column(nullable = false)
    private String emailrecipients;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private String emailifempty;
    @Column(nullable = false)
    private Timestamp lastupdateddate;

    private String comments;
    private String endpointid;
    private String timezonename;
    private String wmcomment;

    @Transient
    private Integer logId;
    @Transient
    private Instant nextScheduledRun;
    @Transient
    private Integer lastRunLogId;
    @Transient
    private Integer lastSuccessfulRunLogId;


    public ReportInstanceConfig updateReportInstanceStatus(State jobStatus)
    {
        ReportInstanceConfig reportInstanceConfig = SerializationUtils.clone(this);
        reportInstanceConfig.setStatus(String.valueOf(jobStatus));
        return reportInstanceConfig;
    }

    public ReportInstanceDiffable mapToReportInstanceDiffable() {
        return ReportInstanceDiffable.builder()
                .id(this.getId())
//                .jobTypeId(this.getJobTypeId())
                .status(StringUtils.capitalize(this.getStatus().toLowerCase()))
                .comments(Objects.isNull(this.getComments()) ? "" : this.getComments())
//                .configuration(new String(this.getConfiguration()))
                .emailrecipients(Objects.isNull(this.getEmailrecipients()) ? "" : this.getEmailrecipients())
                .cronschedule(this.getCronschedule())
//                .hospitalDepartmentTimeZone(this.getTimezonename())
                .build();
    }

    @Override
    public Integer getSchedulableId() {
        return id.intValue();
    }

    @Override
    public String getTaskName() {
        return reportname;
    }

    @Override
    public String getDescription() {
        return comments;
    }

    @Override
    public String getCronExpression() {
        return cronschedule;
    }

    @Override
    public String getIdentification() {
        return id.toString();
    }

    @Override
    public void setLastSuccessfulRunLogId(int lastSuccessfulRunLogId) {
        this.lastSuccessfulRunLogId = lastSuccessfulRunLogId;
    }

    @Override
    public void setNextScheduledRun(Instant nextScheduledRun) {
        this.nextScheduledRun = nextScheduledRun;
    }

    @Override
    public String getEmailToList() {
        return emailrecipients;
    }

    @Override
    public String getTaskStatus() {
        return status;
    }

    @Override
    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(timezonename);
    }
}
