//package com.wavemark.scheduler.schedule.repository;
//
//import java.util.List;
//
//import com.wavemark.scheduler.schedule.domain.entity.TaskRunLog;
//import com.wavemark.scheduler.schedule.domain.projection.SchdTaskRunLog;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface SchdTaskRunLogRepository extends JpaRepository<TaskRunLog, Integer> {
//
//	String QUERY = "SELECT TASKID, ENDPOINTID, TASKTYPE, DESCRIPTION, CRONEXPRESSION, "
//			+ "TIMEZONE, RUNSTARTDATE, RUNENDDATE, RUNSTATE, RESPONSEMESSAGE "
//			+ "FROM SCHD_TASKRUNLOG_R "
//			+ "WHERE ENDPOINTID = :endpointId";
//
//	@Query(nativeQuery = true, value = QUERY)
//	List<SchdTaskRunLog> getSchdTaskRunLog(@Param("endpointId") String endpointId);
//}
