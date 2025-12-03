package com.wangkang.wangkangdataetlservice.dao.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.wangkang.wangkangdataetlservice.dao.wkgwsales.repository",
        entityManagerFactoryRef = "daoEntityManagerFactoryWKGWSales",
        transactionManagerRef = "daoTransactionManagerWKGWSales"
)
public class DaoWKGWSalesConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.wk-gw-sales")
    public DataSource daoDataSourceWKGWSales() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean daoEntityManagerFactoryWKGWSales(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(daoDataSourceWKGWSales())
                .packages("com.wangkang.wangkangdataetlservice.dao.wkgwsales.model")
                .persistenceUnit("postgres")
                .build();
    }

    @Bean
    public PlatformTransactionManager daoTransactionManagerWKGWSales(
            @Qualifier("daoEntityManagerFactoryWKGWSales") EntityManagerFactory daoEntityManagerFactoryWKGWSales) {
        return new JpaTransactionManager(daoEntityManagerFactoryWKGWSales);
    }
}
