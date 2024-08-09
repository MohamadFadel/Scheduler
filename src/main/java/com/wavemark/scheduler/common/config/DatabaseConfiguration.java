package com.wavemark.scheduler.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.cardinalhealth.service.support.configuration.DatabaseConfigurationSupport;
import com.cardinalhealth.service.support.configuration.Datasource;
import com.cardinalhealth.service.support.configuration.DatasourceRoutingConfiguration;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAutoConfiguration
//@EnableJpaRepositories("com.wavemark.scheduler.schedule.repository")
@EnableTransactionManagement
@Slf4j
public class DatabaseConfiguration {

    private static final String SPRING_DATASOURCE = "spring.datasource-";
    private final Environment env;

    public DatabaseConfiguration(Environment env) {
        this.env = env;
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

    protected DataSource generateDatasource(String datasourcePrefix) throws NamingException {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(this.env.getProperty("spring.datasource-" + datasourcePrefix + ".driver.class"));
        dataSource.setUrl(this.env.getProperty("spring.datasource-" + datasourcePrefix + ".url"));
        dataSource.setUsername(this.env.getProperty("spring.datasource-" + datasourcePrefix + ".username"));
        dataSource.setPassword(this.env.getProperty("spring.datasource-" + datasourcePrefix + ".password"));
        return dataSource;
    }

    @Bean(
            name = {"authorizationDs"},
            destroyMethod = ""
    )
    public DataSource authorizationDs() throws NamingException {
        return this.generateDatasource("authorization");
    }

}
