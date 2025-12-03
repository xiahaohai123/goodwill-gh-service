package com.wangkang.goodwillghservice.dao.configuration;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.wangkang.goodwillghservice.dao.goodwillghservice",
        entityManagerFactoryRef = "daoEntityManagerFactoryGoodwillGhService",
        transactionManagerRef = "daoTransactionManager"
)
public class DaoGwGhSrvConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.goodwill-gh-service")
    public DataSource daoDataSourceDataEtlService() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean daoEntityManagerFactoryGoodwillGhService(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(daoDataSourceDataEtlService())
                .packages("com.wangkang.goodwillghservice.dao.goodwillghservice")
                .persistenceUnit("postgres")
                .build();
    }

    @Bean
    public PlatformTransactionManager daoTransactionManager(
            @Qualifier("daoEntityManagerFactoryGoodwillGhService") EntityManagerFactory daoEntityManagerFactoryGoodwillGhService) {
        return new JpaTransactionManager(daoEntityManagerFactoryGoodwillGhService);
    }
}
