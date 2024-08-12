package com.wavemark.scheduler.testing.util;

import com.wavemark.scheduler.cardinalhealth.scheduler.jobs.APPJob;
import com.cardinalhealth.service.support.constant.ContentType;
import com.cardinalhealth.service.support.models.Email;
import com.wavemark.scheduler.fire.constant.TriggerState;
import com.wavemark.scheduler.fire.http.property.HttpProperty;
import com.wavemark.scheduler.fire.http.response.HttpResponse;
import com.wavemark.scheduler.fire.task.ScheduledTask;
import com.wavemark.scheduler.logging.errorlog.entity.ErrorLog;
import com.wavemark.scheduler.logging.recordlog.entity.RecordLog;
import com.wavemark.scheduler.schedule.constant.State;
import com.wavemark.scheduler.schedule.domain.entity.*;
import com.wavemark.scheduler.schedule.domain.projection.TaskRunLogRP;
import com.wavemark.scheduler.schedule.dto.logdiffable.TaskLogDiffable;
import com.wavemark.scheduler.schedule.dto.request.ReportInstanceInput;
import com.wavemark.scheduler.schedule.dto.request.ReportSchedulerInput;
import com.wavemark.scheduler.schedule.dto.request.TaskFrequencyInput;
import com.wavemark.scheduler.schedule.dto.request.TaskInput;
import com.wavemark.scheduler.schedule.dto.response.TaskUpdateLogResponse;
import org.json.JSONObject;
import org.quartz.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.*;

import static com.wavemark.scheduler.common.constant.DataMapProperty.*;
import static com.wavemark.scheduler.schedule.service.quartz.JobDetailService.PARAM_REPORT_INSTANCE_ID;
import static com.wavemark.scheduler.schedule.service.quartz.JobDetailService.PARAM_USER_TOKEN;

public abstract class DataUtil {

    public static ReportSchedulerInput generateReportSchedulerInput(){
        return new ReportSchedulerInput(generateTaskInput(), generateReportInstanceInput());
    }

    public static ReportInstanceInput generateReportInstanceInput(){

        return ReportInstanceInput.builder()
                .reportInstanceId(1L)
                .reportName("Test Report")
                .actionName("Test Action")
                .className("TestClass")
                .reportInstanceName("Test Instance")
                .userId("TestUser")
                .endPointIdHospDept("Endpoint1")
                .reportState("state")
                .emailFormat("text")
                .emailRecipients("test@example.com")
                .comments("Test comments")
                .status("ACTIVE")
                .emailIfEmpty("test@example.com")
                .timezone("UTC")
                .language("en")
                .build();
    }


    public static ReportInstanceConfig generateReportInstanceConfig() {
        return ReportInstanceConfig.builder()
                .id(1L)
                .reportname("Test Report")
                .actionname("Test Action")
                .classname("TestClass")
                .reportinstancename("Test Instance")
                .userid("TestUser")
                .reportstate(new char[]{'A'})
                .cronschedule("0 0/5 * * * ?")
                .emailformat("text")
                .emailrecipients("test@example.com")
                .status("ACTIVE")
                .emailifempty("test@example.com")
                .lastupdateddate(Timestamp.from(Instant.now()))
                .comments("Test comments")
                .endpointid("Endpoint1")
                .timezonename("UTC")
                .wmcomment("WM Comment")
                .logId(1)
                .nextScheduledRun(Instant.now())
                .lastRunLogId(1)
                .lastSuccessfulRunLogId(1)
                .build();
    }

    public static TaskFrequencyInput generateTaskFrequencyInput() {
        return new TaskFrequencyInput("monthly", Collections.singletonList(30),
                Collections.singletonList(10), Collections.singletonList(0), Collections.singletonList("4"),
                Collections.singletonList("0"), "GMT+1");
    }

    public static TaskInput generateTaskInput() {
        return new TaskInput("Auto-Order", "testDescription", "userToken","111","testBodyParam", generateTaskFrequencyInput(), "test@test.com");
    }

    public static TaskInput generateTaskInputUpdated() {
        return new TaskInput("Auto-Order", "updatedTestDescription", "updatedUserToken","111",
                "updatedTestBodyParam", generateTaskFrequencyInput(), "updatedTest@test.com");
    }

    public static Task generateTask() {
        return Task.builder()
                .taskId(1)
                .taskName("testName")
                .sourceIdEndpointId("testEndpointId")
                .description("testDescription")
                .createdBy("testUser")
                .createdOn(Instant.now())
                .taskStatus(String.valueOf(State.ACTIVE))
                .configuration("testConfiguration".toCharArray())
                .emailToList("test@test.com")
                .cronExpression("0 0/1 * * * ?")
                .taskTypeId(1)
                .hospitalDepartmentTimeZone(TimeZone.getDefault())
                .nextScheduledRun(Instant.now())
                .lastRunLogId(2)
                .logId(2)
                .build();
    }

    public static HttpResponse generateResponse() {
        return HttpResponse.builder().success(false).code(401).message("Unexpected error").cause("Unexpected error").build();
    }

    public static HttpResponse generateSuccessResponse() {
        return HttpResponse.builder().success(true).code(200).build();
    }

    public static Optional<Task> generateOptionalTask(String state) {
        return Optional.ofNullable(Task.builder()
                .taskId(1)
                .taskName("testName")
                .sourceIdEndpointId("testEndpointId")
                .description("testDescription")
                .createdBy("testUser")
                .createdOn(Instant.now())
                .taskStatus(state)
                .configuration("testConfiguration".toCharArray())
                .emailToList("test@test.com")
                .cronExpression("")
                .taskTypeId(1)
                .build());
    }

    public static List<Task> generateTaskListWithState(String state) {
        List<Task> tasks = new ArrayList<>();

        Task task = Task.builder()
                .taskId(1)
                .taskName("testName")
                .sourceIdEndpointId("testEndpointId")
                .description("testDescription")
                .createdBy("testUser")
                .createdOn(Instant.now())
                .taskStatus(state)
                .configuration("testConfiguration".toCharArray())
                .emailToList("test@test.com")
                .cronExpression("")
                .taskTypeId(1)
                .build();

        tasks.add(task);

        return tasks;
    }

    public static List<Task> generateTaskList() {
        List<Task> taskList = new ArrayList<>();
        Task task1 = generateTask();
        Task task2 = generateTask();

        task1.setTaskName("testName1");
        task1.setTaskStatus(String.valueOf(State.ACTIVE));
        task2.setTaskName("testName2");
        task2.setTaskStatus(String.valueOf(State.DEACTIVATED));

        taskList.add(task1);
        taskList.add(task2);

        return taskList;
    }

    public static Optional<TaskType> generateTaskTypeOptional() {
        Optional<TaskType> taskType = Optional.of(new TaskType());

        taskType.get().setTaskTypeId(1);
        taskType.get().setTaskType("Auto-Order");
        taskType.get().setApiEndpoint("/test");

        return taskType;
    }

    public static TaskType generateTaskType() {
        TaskType taskType = new TaskType();

        taskType.setTaskTypeId(1);
        taskType.setTaskType("Auto-Order");
        taskType.setApiEndpoint("/test");

        return taskType;
    }

    public static JobDetail generateJobDetail() {
        return JobBuilder.newJob(ScheduledTask.class)
                .withIdentity("testName", CLUSTERED_JOBS_GROUP)
                .withDescription("testDescription")
                .usingJobData(NAME, "testName")
                .usingJobData(BODY_PARAM, "{ testBodyParam: test }")
                .requestRecovery()
                .storeDurably()
                .build();
    }
    public static JobDetail generateOldJobDetail() {
        return JobBuilder.newJob(APPJob.class)
                .withIdentity("testName", CLUSTERED_JOBS_GROUP)
                .withDescription("testDescription")
                .usingJobData(PARAM_REPORT_INSTANCE_ID, "1")
                .usingJobData(PARAM_USER_TOKEN, "testUserToken")
                .requestRecovery()
                .storeDurably()
                .build();
    }

    public static Trigger generateTrigger() {
        return TriggerBuilder.newTrigger()
                .withIdentity("testName" + "_TRG", CLUSTERED_JOBS_GROUP)
                .withDescription("testDescription")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")
                        .inTimeZone(TimeZone.getTimeZone("GMT+1"))
                        .withMisfireHandlingInstructionFireAndProceed())
                .forJob(generateJobDetail())
                .build();
    }

    public static JobDataMap generateJobDataMap() {
        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(NAME, "taskName");
        jobDataMap.put(BODY_PARAM, "{ testBodyParam: test }");
        jobDataMap.put(ENDPOINT_ID, "endpointId");
        jobDataMap.put(ENDPOINT_NAME, "endpointName");

        return jobDataMap;
    }

    public static HttpProperty generateHttpProperty() {
        return HttpProperty.builder()
                .taskName("taskName")
                .endpointName("endpointName")
                .url("http://localhost:8081/url")
                .bodyParam("{ testBodyParam: test }")
                .build();
    }

    public static HttpProperty generateOldHttpProperty() {
        return HttpProperty.builder()
                .taskName("1")
                .endpointName("endpointName")
                .url("http://localhost:8081/url")
                .bodyParam("{ testBodyParam: test }")
                .build();
    }

    public static HttpResponse generateSuccessHttpResponse() {
        return HttpResponse.builder()
                .success(true)
                .code(200)
                .phrase("Success")
                .series("Success")
                .message(generateAuthTokenResponse())
                .cause(generateAuthTokenResponse())
                .url("http://localhost:8081/url")
                .build();
    }

    public static HttpResponse generateSuccessHttpResponseWithTokenExpiresSoon() {
        return HttpResponse.builder()
                .success(true)
                .code(200)
                .phrase("Success")
                .series("Success")
                .message(generateAuthTokenResponseExpiresSoon())
                .cause(generateAuthTokenResponseExpiresSoon())
                .url("http://localhost:8081/url")
                .build();
    }

    public static HttpResponse generateBadHttpResponse() {
        return HttpResponse.builder()
                .success(false)
                .code(500)
                .phrase("Internal Server Error")
                .series("SERVER_ERROR")
                .message(generateAuthTokenResponse())
                .cause(generateAuthTokenResponse())
                .url("http://localhost:8081/url")
                .build();
    }

    public static List<? extends Trigger> generateTriggers() {
        return Collections.singletonList(generateTrigger());
    }

    public static TaskRunLog generateTaskRunLog() {
        TaskRunLog taskRunLog = new TaskRunLog();
        taskRunLog.setTaskRunLogId(2);

        return taskRunLog;
    }

    public static Email generateEmail() {
        return Email.builder()
                .fromEmailAddress("DONOTREPLY@TEST.com")
                .toEmailAddresses("customer@cardinalhealth.com")
                .emailSubject("[wavemark] (Task Failure)")
                .emailMessage("The scheduled task\n WaveMark Support Team")
                .build();
    }

    public static Email generateDeveloperEmail() {
        return Email.builder()
                .fromEmailAddress("DONOTREPLY@TEST.com")
                .toEmailAddresses("support@cardinalhealth.com")
                .emailSubject("[wavemark] (Task Failure)")
                .emailMessage("The scheduled task\n WaveMark Support Team")
                .emailContentType(ContentType.HTML)
                .build();
    }

    public static TaskRunLogRP generateSchdTaskRunLog() {
        return new TaskRunLogRP() {
            @Override
            public Integer getTaskId() {
                return 1;
            }

            @Override
            public String getEndpointId() {
                return "endpointId";
            }

            @Override
            public String getTaskType() {
                return "Auto-Order";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public String getCronExpression() {
                return "0 30 11 4 * ? *";
            }

            @Override
            public TimeZone getTimeZone() {
                return TimeZone.getDefault();
            }

            @Override
            public Instant getRunStartDate() {
                return Instant.now();
            }

            @Override
            public String getRunStatus() {
                return String.valueOf(TriggerState.SUCCESS);
            }

            @Override
            public int getResponseCode() {
                return 200;
            }

            @Override
            public String getResponseMessage() {
                return "Success";
            }
        };

    }

    public static TaskRunLogRP generateFailedSchdTaskRunLog() {
        return new TaskRunLogRP() {
            @Override
            public Integer getTaskId() {
                return 1;
            }

            @Override
            public String getEndpointId() {
                return "endpointId";
            }

            @Override
            public String getTaskType() {
                return "Auto-Order";
            }

            @Override
            public String getDescription() {
                return "description";
            }

            @Override
            public String getCronExpression() {
                return "0 30 11 4 * ? *";
            }

            @Override
            public TimeZone getTimeZone() {
                return TimeZone.getDefault();
            }

            @Override
            public Instant getRunStartDate() {
                return Instant.now();
            }

            @Override
            public String getRunStatus() {
                return String.valueOf(TriggerState.FAILURE);
            }

            @Override
            public int getResponseCode() {
                return 412;
            }

            @Override
            public String getResponseMessage() {
                return "Task has failed because your department is not configured to run this task type.";
            }
        };

    }

    public static List<RecordLog> generateRecordLogs() {
        return Collections.singletonList(generateRecordLog());
    }

    public static RecordLog generateRecordLog() {
        return RecordLog.builder()
                .fieldName("description").oldValue("description").newValue("edited description")
                .updatedDate(Instant.now()).updatedBy("userId")
                .logId(1).build();
    }

    public static HashMap<Task, List<RecordLog>> generateRecordLogsMap() {
        HashMap<Task, List<RecordLog>> recordLogMap = new HashMap<>();
        recordLogMap.put(generateTask(), Collections.singletonList(generateRecordLog()));

        return recordLogMap;
    }

    public static RecordLog generateCreatedStatusRecordLog() {
        return RecordLog.builder()
                .fieldName("taskStatus").oldValue("ACTIVE").newValue("CREATED")
                .updatedDate(Instant.now()).updatedBy("userId")
                .logId(1).build();
    }

    public static RecordLog generateDeletedStatusRecordLog() {
        return RecordLog.builder()
                .fieldName("taskStatus").oldValue("ACTIVE").newValue("DEACTIVATED")
                .updatedDate(Instant.now()).updatedBy("userId")
                .logId(1).build();
    }

    public static RecordLog generateFrequencyRecordLog() {
        return RecordLog.builder()
                .fieldName("cronExpression").oldValue("0 30 11 ? * 1,3,4 *").newValue("0 0 1 ? * 1 *")
                .updatedDate(Instant.now()).updatedBy("userId")
                .logId(1).build();
    }

    public static RecordLog generateConfigurationRecordLog() {
        return RecordLog.builder()
                .fieldName("configuration")
                .oldValue("{\"includeMissing\":false,\"includeScannedToCart\":false,\"orderReplacements30Days\":true}")
                .newValue("{\"includeMissing\":true,\"includeScannedToCart\":true,\"orderReplacements30Days\":true}")
                .updatedDate(Instant.now()).updatedBy("userId")
                .logId(1).build();
    }

    public static TaskLogDiffable generateActiveTaskUpdateLogDTO() {
        return TaskLogDiffable.builder()
                .description("testDescription")
                .taskStatus(String.valueOf(State.ACTIVE))
                .configuration("testConfiguration")
                .emailToList("test@test.com")
                .cronExpression("0 0/1 * * * ?")
                .taskTypeId(1)
                .hospitalDepartmentTimeZone(TimeZone.getDefault().getID())
                .build();
    }

    public static TaskLogDiffable generatePausedTaskUpdateLogDTO() {
        return TaskLogDiffable.builder()
                .description("testDescription")
                .taskStatus(String.valueOf(State.PAUSED))
                .configuration("testConfiguration")
                .emailToList("test@test.com")
                .cronExpression("0 0/1 * * * ?")
                .taskTypeId(1)
                .hospitalDepartmentTimeZone(TimeZone.getDefault().getID())
                .build();
    }

    public static TaskLogDiffable generateDeactivatedTaskUpdateLogDTO() {
        return TaskLogDiffable.builder()
                .description("testDescription")
                .taskStatus(String.valueOf(State.DEACTIVATED))
                .configuration("testConfiguration")
                .emailToList("test@test.com")
                .cronExpression("0 0/1 * * * ?")
                .taskTypeId(1)
                .hospitalDepartmentTimeZone(TimeZone.getDefault().getID())
                .build();
    }

    public static String generateAuthTokenResponse() {
        return "{\"access_token\":\"T1X-3jt11SYjIUfwGj0Rm1Y_Ym8\",\"token_type\":\"bearer\",\"refresh_token\":\"j3bQtY3kr41YBrmhOHoafJIsFRE\",\"expires_in\":7102,\"scope\":\"read write\"}";
    }

    public static String generateAuthTokenResponseExpiresSoon() {
        return "{\"access_token\":\"T1X-3jt11SYjIUfwGj0Rm1Y_Ym9\",\"token_type\":\"bearer\",\"refresh_token\":\"j3bQtY3kr41YBrmhOHoafJIsFRE\",\"expires_in\":300,\"scope\":\"read write\"}";
    }

    public static ErrorLog generateErrorLog()
    {
        return ErrorLog.builder()
                .errorId(1L)
                .deviceId("deviceId")
                .errorCode("500")
                .errorDateTime(new Timestamp(Calendar.getInstance().getTime().getTime()))
                .message("message")
                .severity("severity")
                .source("source")
                .stackMessage("stackMessage".getBytes())
                .username("username")
                .wmPackage("wmPackage")
                .build();
    }

    public static List<TaskUpdateLogResponse> genarateTaskUpdateLogResponseList()
    {
        return Collections.singletonList(TaskUpdateLogResponse.builder()
                .taskType("taskType").taskDescription("taskDescription").taskFrequency("taskFrequency")
                .identifier("description").previousValue("description").updatedField("edited description")
                .updatedOn("now").updatedBy("userId").build());
    }

}
