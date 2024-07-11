package com.wavemark.scheduler.logging.performancelogging.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wavemark.scheduler.logging.performancelogging.constant.LogPerformanceTime;
import com.wavemark.scheduler.logging.performancelogging.constant.LogType;
import com.wavemark.scheduler.logging.performancelogging.entity.PerformanceTiming;

import com.cardinalhealth.service.support.context.UserContextContextHolder;
import com.cardinalhealth.service.support.models.TypedValue;
import com.cardinalhealth.service.support.models.UserContext;
import com.cardinalhealth.service.support.security.SecurityUtilsV2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@ExtendWith(MockitoExtension.class)
class PerformanceTimingFactoryTest {

	@Mock
	private StopWatch stopWatch;
	@Mock
	private MethodSignature methodSignature;
	@Mock
	private ProceedingJoinPoint joinPoint;

	@InjectMocks
	private PerformanceTimingFactory performanceTimingFactory;

	private static MockedStatic<SecurityUtilsV2> securityUtils;

	private static MockedStatic<UserContextContextHolder> userContextHolder;

	@BeforeAll
	static void beforeAll() {
		securityUtils = Mockito.mockStatic(SecurityUtilsV2.class);
		userContextHolder = Mockito.mockStatic(UserContextContextHolder.class);

		Mockito.when(SecurityUtilsV2.getCurrentAuthUser()).thenReturn("username");
		Mockito.when(SecurityUtilsV2.getCurrentAuthDepartment()).thenReturn("deptId");
		Mockito.when(SecurityUtilsV2.getDepartmentConfig()).thenReturn(createDepartmentMap());
	}

	@AfterAll
	static void afterAll() {
		securityUtils.close();
		userContextHolder.close();
	}

	@BeforeEach
	public void setUp() {
		lenient().when(stopWatch.getTotalTimeMillis()).thenReturn(123L);
		lenient().when(methodSignature.getName()).thenReturn("function name");
		lenient().when(methodSignature.getDeclaringType()).thenReturn(PerformanceTimingFactoryTest.class);

		ReflectionTestUtils.setField(performanceTimingFactory, "moduleName", "Scheduler");
	}

	@Test
	void testConstructPerformanceTimingFactory()
	{
		ReflectionTestUtils.setField(performanceTimingFactory, "moduleName", null);
		assertThrows(RuntimeException.class, () -> performanceTimingFactory.constructPerformanceTimingFactory());
	}

	@Test
	void testThatGetInstanceWorksCorrectlyForDBLogging()
			throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithModuleName");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		UserContext userContext = mock(UserContext.class);
		Mockito.when(UserContextContextHolder.getUserContext()).thenReturn(userContext);
		Mockito.when(userContext.getContext()).thenReturn(createTypeValueList());

		PerformanceTiming performanceTiming = performanceTimingFactory.getInstance(stopWatch, joinPoint);

		Assertions.assertEquals("DB", performanceTiming.getLogType());
	}

	@Test
	void testThatGetInstanceWorksCorrectlyForAppLogging()
			throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithAnnotation");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		PerformanceTiming performanceTiming = performanceTimingFactory.getInstance(stopWatch, joinPoint);

		Assertions.assertEquals("APP", performanceTiming.getLogType());
	}

	@Test
	void TestThatInitializeAppPerformanceTimingWorksCorrectlyWithGetMapping()
			throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithAnnotation");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		PerformanceTiming performanceTiming = performanceTimingFactory.initializeAppPerformanceTiming(stopWatch, joinPoint);

		Assertions.assertEquals(" - API URL is: [/mockApiCall]", performanceTiming.getLogContext());
		Assertions.assertEquals("APP", performanceTiming.getLogType());
	}

	@Test
	void TestThatInitializeAppPerformanceTimingWorksCorrectlyWithPostMapping()
			throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithPostAnnotation");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		PerformanceTiming performanceTiming = performanceTimingFactory.initializeAppPerformanceTiming(stopWatch, joinPoint);

		Assertions.assertEquals(" - API URL is: [/mockApiCall]", performanceTiming.getLogContext());
		Assertions.assertEquals("APP", performanceTiming.getLogType());
	}

	@Test
	void TestThatInitializeAppPerformanceTimingWorksCorrectlyWithDeleteMapping()
			throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithDeleteAnnotation");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		PerformanceTiming performanceTiming = performanceTimingFactory.initializeAppPerformanceTiming(stopWatch, joinPoint);

		Assertions.assertEquals(" - API URL is: [/mockApiCall]", performanceTiming.getLogContext());
		Assertions.assertEquals("APP", performanceTiming.getLogType());
	}

	@GetMapping(value = "/mockApiCall")
	@LogPerformanceTime(logType = LogType.APP)
	public void methodMockedWithAnnotation() {
	}

	@PostMapping(value = "/mockApiCall")
	@LogPerformanceTime(logType = LogType.APP)
	public void methodMockedWithPostAnnotation() {
	}

	@DeleteMapping(path = "/mockApiCall")
	@LogPerformanceTime(logType = LogType.APP)
	public void methodMockedWithDeleteAnnotation() {
	}


	@Test
	void TestThatInitializeAppPerformanceTimingWorksCorrectlyWithoutAnnotation() throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithOutAnnotation");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		PerformanceTiming performanceTiming = performanceTimingFactory.initializeAppPerformanceTiming(stopWatch, joinPoint);

		Assertions.assertEquals("DEFAULT", performanceTiming.getLogContext());
		Assertions.assertEquals("APP", performanceTiming.getLogType());
	}

	public void methodMockedWithOutAnnotation() {
	}

	@Test
	void TestThatInitializeDBPerformanceTimingWorksCorrectlyWithDefaultContext()
			throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithModuleName");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		UserContext userContext = mock(UserContext.class);
		Mockito.when(UserContextContextHolder.getUserContext()).thenReturn(userContext);
		Mockito.when(userContext.getContext()).thenReturn(createTypeValueList());

		PerformanceTiming performanceTiming = performanceTimingFactory.initializeDBPerformanceTiming(stopWatch, joinPoint);

		Assertions.assertEquals("DB: SELECT * FROM MOCK_VIEW ", performanceTiming.getLogContext().split("-")[0]);
		Assertions.assertEquals("DB", performanceTiming.getLogType());
	}

	@Test
	void TestThatInitializeDBPerformanceTimingWorksCorrectlyWithQueryAnnotation()
			throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithQueryAnnotation");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		UserContext userContext = mock(UserContext.class);
		Mockito.when(UserContextContextHolder.getUserContext()).thenReturn(userContext);
		Mockito.when(userContext.getContext()).thenReturn(createTypeValueList());

		PerformanceTiming performanceTiming = performanceTimingFactory.initializeDBPerformanceTiming(stopWatch, joinPoint);

		Assertions.assertEquals("DB: methodMockedWithQueryAnnotation ", performanceTiming.getLogContext().split("-")[0]);
		Assertions.assertEquals("DB", performanceTiming.getLogType());
	}

	@Test
	void testThatInitializeCommonAttributesWorksCorrectly() {

		PerformanceTimingService declaringClass =  mock(PerformanceTimingService.class);

		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		when(methodSignature.getDeclaringType()).thenReturn(declaringClass.getClass());
		when(methodSignature.getName()).thenReturn("MethodName");

		PerformanceTiming performanceTiming = performanceTimingFactory.initializeCommonAttributes(stopWatch, joinPoint);

		Assertions.assertEquals("UNKNOWN", performanceTiming.getRequestId());
		Assertions.assertEquals("username", performanceTiming.getUserId());
		Assertions.assertEquals("deptId", performanceTiming.getSecurityId());
		Assertions.assertNull(performanceTiming.getSessionId());
		Assertions.assertEquals("Scheduler - PerformanceTimingService - MethodName()", performanceTiming.getActionName());
		Assertions.assertEquals(123L, performanceTiming.getLogDuration().longValue());
		Assertions.assertEquals("N/A", performanceTiming.getBrowser());
		Assertions.assertNotNull(performanceTiming.getDateTime());
	}

	@LogPerformanceTime
	@Query(value = "SELECT * FROM MOCK_VIEW")
	public void methodMockedWithQueryAnnotation() {
	}

	@LogPerformanceTime(queryName = "SELECT * FROM MOCK_VIEW")
	public void methodMockedWithModuleName() {
	}

	@Test
	void testThatInitializeDBPerformanceTimingWorksCorrectlyIfUserContextIsGreaterThan2000()
			throws NoSuchMethodException {
		Method mockMethod = this.getClass().getMethod("methodMockedWithModuleNameAndQueryNameGreaterThan2000");
		Mockito.when(joinPoint.getSignature()).thenReturn(methodSignature);
		Mockito.when(methodSignature.getMethod()).thenReturn(mockMethod);

		UserContext userContext = mock(UserContext.class);
		Mockito.when(UserContextContextHolder.getUserContext()).thenReturn(userContext);
		Mockito.when(userContext.getContext()).thenReturn(createTypeValueList());

		PerformanceTiming performanceTiming = performanceTimingFactory.initializeDBPerformanceTiming(stopWatch, joinPoint);

//		Assertions.assertEquals("DB: AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA...", performanceTiming.getLogContext());
		Assertions.assertEquals("DB", performanceTiming.getLogType());
	}

	@LogPerformanceTime(queryName = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" +
			"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
	public void methodMockedWithModuleNameAndQueryNameGreaterThan2000() {
	}

	private List<TypedValue> createTypeValueList() {
		List<TypedValue> typedValueList = new ArrayList<>();
		TypedValue hospitalDept = new TypedValue("ReportHospitalDept", "hospId", TypedValue.ValueType.STRING);
		TypedValue productLinkingEnabled = new TypedValue("isProductLinkingEnabled", "Y", TypedValue.ValueType.STRING);
		TypedValue isLocationLevel = new TypedValue("isLocationLevelOrdering", "1", TypedValue.ValueType.STRING);

		typedValueList.add(hospitalDept);
		typedValueList.add(productLinkingEnabled);
		typedValueList.add(isLocationLevel);

		return typedValueList;
	}

	private static Map<String, Boolean> createDepartmentMap() {
		Map<String, Boolean> conMap = new HashMap<>();
		conMap.put("isLocationLevelOrdering", true);
		conMap.put("isProductLinkingEnabled", false);
		conMap.put("isAutoReceiveOrder", false);
		return conMap;
	}
}