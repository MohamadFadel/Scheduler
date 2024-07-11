package com.wavemark.scheduler.logging.errorlog.service.repository;

import java.sql.Timestamp;

import com.wavemark.scheduler.logging.errorlog.entity.ErrorLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long>  {

	String ERRORLOG_INSERT_QUERY = "INSERT INTO ERRORLOG VALUES (ERRORLOG_ErrorID_SEQ.NEXTVAL,:errorDateTime,:wmPackage,:source,:message,:errorCode,:stackMessage,:severity,:username,:deviceId)";
	@Modifying
	@Query(nativeQuery = true, value = ERRORLOG_INSERT_QUERY)
	void insertIntoErrorLog(@Param("errorDateTime") Timestamp errorDateTime,
			@Param("wmPackage") String wmPackage,
			@Param("source") String source,
			@Param("message") String message,
			@Param("errorCode") String errorCode,
			@Param("stackMessage") byte[] stackMessage,
			@Param("severity") String severity,
			@Param("username") String username,
			@Param("deviceId") String deviceId) throws Exception;
}
