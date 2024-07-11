package com.wavemark.scheduler.schedule.repository;

import java.util.List;

import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
import com.wavemark.scheduler.schedule.domain.projection.TaskRunLogRP;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRunLogRepository extends JpaRepository<TaskRunLog, Integer> {

	String TASKRUNLOG_RP_QUERY = "SELECT TASKID, ENDPOINTID, TASKTYPE, DESCRIPTION, CRONEXPRESSION, "
			+ "TIMEZONE, RUNSTARTDATE, RUNSTATUS, RESPONSECODE, RESPONSEMESSAGE "
			+ "FROM TASKRUNLOG_RP WHERE ENDPOINTID = :endpointId ORDER BY RUNSTARTDATE DESC fetch first 100 rows only";

	@LogPerformanceTime
	@Query(nativeQuery = true, value = TASKRUNLOG_RP_QUERY)
	List<TaskRunLogRP> getTaskRunLogReport(@Param("endpointId") String endpointId);
}
