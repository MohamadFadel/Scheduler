package com.wavemark.scheduler.logging.recordlog.service.repository;

import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.logging.recordlog.entity.RecordLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RecordLogRepository extends JpaRepository<RecordLog, Integer> {

	String RECORDLOG_LOGID_NEXTVAL = "SELECT RECORDLOG_LOGID_SEQ.NEXTVAL AS GenericHistoryID FROM DUAL";
	@Query(nativeQuery = true, value = RECORDLOG_LOGID_NEXTVAL)
	BigDecimal getLogIdNextValueSequence();

	@LogPerformanceTime
	List<RecordLog> findFirst100ByLogIdInOrderByUpdatedDateDesc(List<Integer> logIds);

	String RECORDLOG_INSERT_QUERY = "INSERT INTO RECORDLOG VALUES (RECORDLOG_LOGID_SEQ.NEXTVAL,:tableName,:fieldName,:oldValue,:newValue,:updatedBy,:updatedDate,:logId,:wmComment,:status)";
	@Modifying
	@Query(nativeQuery = true, value = RECORDLOG_INSERT_QUERY)
	void insertIntoRecordLog(@Param("tableName") String tableName,
			@Param("fieldName") String fieldName,
			@Param("oldValue") String oldValue,
			@Param("newValue") String newValue,
			@Param("updatedBy") String updatedBy,
			@Param("updatedDate") Timestamp updatedDate,
			@Param("logId") Integer logId,
			@Param("wmComment") String wmComment,
			@Param("status") String status);
}
