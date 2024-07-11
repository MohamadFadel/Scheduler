package com.wavemark.scheduler.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Response;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Profile("!prod")
@Configuration
public class SwaggerConfiguration {

    public static final String SCHEDULER_TAG = "Scheduler";
    public static final String REPORT_TAG = "Reporting";
    public static final String REPORT_LOG_TAG = "Logging";
    public static final String TASK_STATE_TAG = "Task State";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.wavemark.scheduler"))
                .paths(PathSelectors.any())
                .build()
                .globalResponses(HttpMethod.GET, customResponses())
                .globalResponses(HttpMethod.POST, customResponses())
                .globalResponses(HttpMethod.PUT, customResponses())
                .globalResponses(HttpMethod.DELETE, customResponses())
                .apiInfo(metaData())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Collections.singletonList(apiKey()))
                .tags(new Tag(SCHEDULER_TAG, "APIs to schedule or remove tasks"),
                        new Tag(REPORT_TAG, "APIs to get scheduled tasks"),
                        new Tag(REPORT_LOG_TAG, "APIs to get tasks logs"),
                        new Tag(TASK_STATE_TAG, "APIs to update task state (ex: pause/resume)"));
    }

    private List<Response> customResponses() {
        return Arrays.asList(
                new ResponseBuilder()
                        .code("401")
                        .description("Invalid or missing authentication")
                        .build(),
                new ResponseBuilder()
                        .code("403")
                        .description("Unauthorized")
                        .build(),
                new ResponseBuilder()
                        .code("500")
                        .description("Internal server error (includes exception handling)")
                        .build());
    }

    private ApiInfo metaData() {
        return new ApiInfo(
                "Task Scheduler",
                "Wavemark Task Scheduler Service",
                "",
                "",
                ApiInfo.DEFAULT_CONTACT,
                "",
                "",
                new ArrayList<>());
    }

    private ApiKey apiKey() {
        return new ApiKey("bearerToken", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(operationContext -> true)
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                new SecurityReference("bearerToken", authorizationScopes));
    }

}