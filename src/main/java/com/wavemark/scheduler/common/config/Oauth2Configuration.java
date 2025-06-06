package com.wavemark.scheduler.common.config;

import com.cardinalhealth.service.support.configuration.HttpConfiguration;
import com.cardinalhealth.service.support.configuration.Oauth2ConfigurationSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
@Import(Oauth2ConfigurationSupport.class)
public class Oauth2Configuration extends ResourceServerConfigurerAdapter {

    private final HttpConfiguration httpConfiguration;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        httpConfiguration.configure(http, "/api/**", "hasAnyRole('WebappHospital','SSOWebappHospital')");
        httpConfiguration.configure(http, "/v2/api-docs/**", "permitAll");
    }
}