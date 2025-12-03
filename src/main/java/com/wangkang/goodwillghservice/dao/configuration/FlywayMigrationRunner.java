package com.wangkang.goodwillghservice.dao.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FlywayMigrationRunner {
    private final Flyway flywayWKToolStation;

    public FlywayMigrationRunner(@Qualifier("flywayWKToolStation") Flyway flywayWKToolStation) {
        this.flywayWKToolStation = flywayWKToolStation;
    }


    @EventListener
    public void onApplicationStarted(ApplicationStartedEvent event) {
        flywayWKToolStation.migrate();
    }
}
