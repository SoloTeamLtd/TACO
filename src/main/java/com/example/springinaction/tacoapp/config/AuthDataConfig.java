package com.example.springinaction.tacoapp.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        basePackages = "com.example.springinaction.tacoapp.core.domain.repository.auth",
        entityManagerFactoryRef = "authEntityManagerFactory",
        transactionManagerRef = "authTransactionManager"
)
public class AuthDataConfig {

    @Bean
    @ConfigurationProperties("app.datasource.auth")
    public DataSourceProperties authDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "authDataSource")
    public DataSource authDataSource() {
        return authDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean(name = "authTransactionManager")
    public PlatformTransactionManager authTransactionManager(
            @Qualifier("authEntityManagerFactory") EntityManagerFactory authEntityManagerFactory) {
        return new JpaTransactionManager(authEntityManagerFactory);
    }

    @Bean(name = "authLiquibase")
    public SpringLiquibase authLiquibase(@Qualifier("authDataSource") DataSource authDataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(authDataSource);
        liquibase.setChangeLog("classpath:/db/changelog/db.changelog-auth-master.yaml");
        return liquibase;
    }

    @Bean(name = "authEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean authEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("authDataSource") DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);

        return builder
                .dataSource(dataSource)
                .packages("com.example.springinaction.tacoapp.core.domain.entity.auth")
                .persistenceUnit("auth")
                .properties(properties)
                .build();
    }
}
