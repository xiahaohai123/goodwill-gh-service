package com.wangkang.wangkangdataetlservice.dao.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FlywayMigrationRunner {
    private final Flyway flywayWKToolStation;
    private final Flyway flywayWKSales;

    public FlywayMigrationRunner(@Qualifier("flywayWKToolStation") Flyway flywayWKToolStation,
                                 @Qualifier("flywayWKSales") Flyway flywayWKSales) {
        this.flywayWKToolStation = flywayWKToolStation;
        this.flywayWKSales = flywayWKSales;
    }


    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        flywayWKToolStation.migrate();
        flywayWKSales.migrate();
    }
}
