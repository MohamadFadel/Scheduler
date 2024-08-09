package com.wavemark.scheduler.schedule.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportInstanceInput {
    private Long reportInstanceId;
    private String reportName;
    private String actionName;
    private String className;
    private String reportInstanceName;
    private String userId;
    private String endPointIdHospDept;
    private JSONObject reportState;
//    private String cronSchedule;
    private String emailFormat;
    private String emailRecipients;
    private String comments;
    private String status;
    private String emailIfEmpty;
    private String timezone;
    private String language;
}
