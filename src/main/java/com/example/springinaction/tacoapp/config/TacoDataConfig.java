package com.example.springinaction.tacoapp.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.example.springinaction.tacoapp.core.domain.repository.taco",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
public class TacoDataConfig {

    @Primary
    @Bean
    @ConfigurationProperties("app.datasource.taco")
    public DataSourceProperties tacoDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "tacoDataSource")
    public DataSource tacoDataSource() {
        return tacoDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "tacoLiquibase")
    public SpringLiquibase tacoLiquibase(@Qualifier("tacoDataSource") DataSource tacoDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(tacoDataSource);
        liquibase.setChangeLog("classpath:/db/changelog/db.changelog-taco-master.yaml");
        return liquibase;
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    @DependsOn("tacoLiquibase")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("tacoDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);

        return builder
                .dataSource(dataSource)
                .packages("com.example.springinaction.tacoapp.core.domain.entity.taco")
                .persistenceUnit("taco")
                .properties(properties)
                .build();
    }
}
