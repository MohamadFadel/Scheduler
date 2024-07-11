package com.wavemark.scheduler.logging.performancelogging.service.repository;

import java.sql.Timestamp;

import com.wavemark.scheduler.logging.performancelogging.entity.PerformanceTiming;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceTimingRepository extends JpaRepository<PerformanceTiming, String>
{
    @Modifying
    @Query(nativeQuery = true, value =  "INSERT INTO PerformanceTiming VALUES (:userId,:securityId,:requestId,:actionName,:dateTime,:logType,:logContext,:logDuration,:browser,:sessionId)")
    void insertIntoPerformanceTiming(@Param("userId") String userId,
                                     @Param("securityId") String securityId,
                                     @Param("requestId") String requestId,
                                     @Param("actionName") String actionName,
                                     @Param("dateTime") Timestamp dateTime,
                                     @Param("logType") String logType,
                                     @Param("logContext") String logContext,
                                     @Param("logDuration") Long logDuration,
                                     @Param("browser") String browser,
                                     @Param("sessionId") String sessionId) throws Exception;
}