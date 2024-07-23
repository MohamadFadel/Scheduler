//package com.wavemark.scheduler.schedule.service.core;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.mockStatic;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.wavemark.scheduler.schedule.domain.projection.SchdTaskRunLog;
//import com.wavemark.scheduler.schedule.repository.SchdTaskRunLogRepository;
//
//import com.cardinalhealth.service.support.security.SecurityUtils;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class SchdTaskRunLogServiceTest {
//
//	@Mock
//	private SchdTaskRunLogRepository schdTaskRunLogRepository;
//
//	@InjectMocks
//	private SchdTaskRunLogService schdTaskRunLogService;
//
//
//	@Test
//	void testGetSchdTaskRunLog() {
//
//		Mockito.when(schdTaskRunLogRepository.getSchdTaskRunLog(any())).thenReturn(new ArrayList<>());
//
//		try (MockedStatic<SecurityUtils> securityUtils = mockStatic(SecurityUtils.class)) {
//			securityUtils.when(SecurityUtils::getCurrentAuthDepartment).thenReturn("testDep");
//			List<SchdTaskRunLog> schdTaskRunLogList = schdTaskRunLogService.getSchdTaskRunLog();
//
//			assertNotNull(schdTaskRunLogList);
//		}
//	}
//}