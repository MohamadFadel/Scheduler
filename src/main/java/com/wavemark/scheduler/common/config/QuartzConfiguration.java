package com.wavemark.scheduler.common.config;

import com.cardinalhealth.service.support.configuration.DatasourceRoutingConfiguration;
import com.wavemark.scheduler.fire.listener.JobHandlingListener;
import com.wavemark.scheduler.fire.listener.SchedulerHandlingListener;
import com.wavemark.scheduler.fire.listener.TriggerHandlingListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import java.util.Date;

@Slf4j
@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@RequiredArgsConstructor
public class QuartzConfiguration {

    public final ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        log.info("Bringing up the Clustered Quartz Scheduler");
    }

    @Bean
    public SchedulerFactoryBean scheduler(DatasourceRoutingConfiguration clientDatasource) throws SchedulerException {

        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        log.debug("Setting the Quartz Scheduler up at " + new Date());
        schedulerFactory.setGlobalJobListeners(new JobHandlingListener());
        schedulerFactory.setSchedulerListeners(new SchedulerHandlingListener());
        schedulerFactory.setGlobalTriggerListeners(new TriggerHandlingListener());

        schedulerFactory.setDataSource(clientDatasource);

        return schedulerFactory;
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();

        log.debug("Configuring Job factory");
        jobFactory.setApplicationContext(applicationContext);

        return jobFactory;
    }

}
