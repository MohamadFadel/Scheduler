package com.wavemark.scheduler.common.config;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.cardinalhealth.service.support.configuration.DatabaseConfigurationSupport;
import com.cardinalhealth.service.support.configuration.Datasource;
import com.cardinalhealth.service.support.configuration.DatasourceRoutingConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
//@EnableJpaRepositories("com.wavemark.scheduler.schedule.repository")
@EnableTransactionManagement
@Slf4j
public class DatabaseConfiguration extends DatabaseConfigurationSupport {

    public DatabaseConfiguration(Environment env) {
        super(env);
    }

    @Bean
    @QuartzDataSource
    @Primary
    public DatasourceRoutingConfiguration clientDatasource() throws NamingException {

        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource schedulerDatasource = generateDatasource("scheduler");

        targetDataSources.put(Datasource.SCHEDULER, schedulerDatasource);

        DatasourceRoutingConfiguration clientRoutingDatasource = new DatasourceRoutingConfiguration();
        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(schedulerDatasource);
        return clientRoutingDatasource;
    }

}
