package com.wavemark.scheduler.logging.performancelogging.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "PERFORMANCETIMING")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceTiming
{
    private String requestId;
    private String actionName;
    @Id
    private Timestamp dateTime;
    private String logContext;
    private Long logDuration;
    private String logType;
    private String browser;
    private String securityId;
    private String userId;
    private String sessionId;

//  @Override
//  public String toString() {
//    return "PerformanceTiming{" +
//            "requestId='" + requestId + '\'' +
//            ", actionName='" + actionName + '\'' +
//            ", dateTime=" + dateTime +
//            ", logContext='" + logContext + '\'' +
//            ", logDuration=" + logDuration +
//            ", logType='" + logType + '\'' +
//            ", browser='" + browser + '\'' +
//            ", securityId='" + securityId + '\'' +
//            ", userId='" + userId + '\'' +
//            ", sessionId='" + sessionId + '\'' +
//            '}';
//  }
}