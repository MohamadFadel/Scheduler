package com.wavemark.scheduler.schedule.dto.logdiffable;

import com.wavemark.scheduler.logging.recordlog.LogDiffable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportInstanceDiffable implements Serializable, LogDiffable<ReportInstanceDiffable> {

    private static final Map<String, String> fieldsDisplayName = ReportInstanceDiffable.getFieldNames();

    private Long id;
//    private Integer reportTypeId;
    private String status;
    private String comments;
    private String configuration;
    private String emailrecipients;
    private String cronschedule;
//    private String hospitalDepartmentTimeZone;


    private static Map<String, String> getFieldNames() {
        Map<String, String> keyValue = new HashMap<>();

        keyValue.put("status", "State");
        keyValue.put("comments", "Description");
//        keyValue.put("configuration", "Configuration");
        keyValue.put("emailrecipients", "Email Notification");
        keyValue.put("cronschedule", "Frequency");
//        keyValue.put("hospitalDepartmentTimeZone", "Timezone");

        return keyValue;
    }

    @Override
    public DiffResult<ReportInstanceDiffable> diff(ReportInstanceDiffable obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.JSON_STYLE)
                .append("status", this.status, obj.status)
                .append("comments", this.comments, obj.comments)
//                .append("configuration", this.configuration, obj.configuration)
                .append("emailrecipients", this.emailrecipients, obj.emailrecipients)
                .append("cronschedule", this.cronschedule, obj.cronschedule)
//                .append("hospitalDepartmentTimeZone", this.hospitalDepartmentTimeZone, obj.hospitalDepartmentTimeZone)
                .build();
    }

    @Override
    public String getTableName() {
        return "Reportinstanceconfig";
    }
}
