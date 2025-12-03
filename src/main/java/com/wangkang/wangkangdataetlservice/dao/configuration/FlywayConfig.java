package com.wangkang.wangkangdataetlservice.dao.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {


    @Bean
    public Flyway flywayWKToolStation(@Qualifier("daoDataSourceDataEtlService") DataSource dataSource) {
        return Flyway.configure().dataSource(dataSource).locations("classpath:db/migration/dataetlservice").load();
    }

    @Bean
    public Flyway flywayWKSales(@Qualifier("daoDataSourceWKGWSales") DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration/wkgwsales")
                .baselineOnMigrate(true)
                .load();
    }


}
