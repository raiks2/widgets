package com.raiks.widgets.configuration.factory;

import javax.sql.DataSource;

import com.raiks.widgets.infrastructure.controller.PagingSimpleWidgetController;
import com.raiks.widgets.infrastructure.controller.SimpleWidgetController;
import com.raiks.widgets.core.application.service.IPagingWidgetRepository;
import com.raiks.widgets.core.application.service.IWidgetRepository;
import com.raiks.widgets.core.application.service.IPagingWidgetService;
import com.raiks.widgets.core.application.service.IWidgetService;
import com.raiks.widgets.core.application.service.SimpleWidgetService;
import com.raiks.widgets.core.application.service.PagingWidgetService;
import com.raiks.widgets.infrastructure.jdbi.logging.SimpleSqlLogger;
import com.raiks.widgets.infrastructure.repository.SqlWidgetRepository;
import com.raiks.widgets.infrastructure.repository.InMemoryWidgetRepository;
import com.raiks.widgets.infrastructure.repository.PagingWidgetRepository;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.SqlLogger;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

public class ApplicationFactory {
    @Configuration
    @Profile("sql")
    public static class SqlConfiguration {
        @Bean
        public SimpleWidgetController simpleWidgetController() {
            return new SimpleWidgetController(simpleWidgetService());
        }

        @Bean
        public IWidgetService simpleWidgetService() {
            return new SimpleWidgetService(sqlWidgetRepository());
        }

        @Bean
        public IPagingWidgetService pagingWidgetService() {
            return new PagingWidgetService(simpleWidgetService(), pagingWidgetRepository());
        }

        @Bean
        public IPagingWidgetRepository pagingWidgetRepository() {
            return new PagingWidgetRepository(sqlWidgetRepository());
        }

        @Bean
        public Jdbi jdbi() {
            Jdbi jdbi = Jdbi.create(dataSource());
            jdbi.setSqlLogger(sqlLogger());
            return jdbi;
        }

        @Bean
        public IWidgetRepository sqlWidgetRepository() {
            return new SqlWidgetRepository(jdbi());
        }

        @Bean
        public DataSource dataSource() {
            DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
            // Change to something like "jdbc:h2:file:/tmp/widgets" if you need a persistent storage
            // "/tmp" must be writable for the user starting the application
            dataSourceBuilder.url("jdbc:h2:mem:widgets");
            dataSourceBuilder.username("sa");
            dataSourceBuilder.password("");
            dataSourceBuilder.driverClassName("org.h2.Driver");
            return dataSourceBuilder.build();
        }

        @Bean
        public SqlLogger sqlLogger() {
            return new SimpleSqlLogger();
        }
    }

    @Configuration
    @Profile("in-memory")
    public static class InMemoryConfiguration {
        @Bean
        public SimpleWidgetController simpleWidgetController() {
            return new SimpleWidgetController(simpleWidgetService());
        }

        @Bean
        public IWidgetService simpleWidgetService() {
            return new SimpleWidgetService(inMemoryWidgetRepository());
        }

        @Bean
        public IPagingWidgetService pagingWidgetService() {
            return new PagingWidgetService(simpleWidgetService(), pagingWidgetRepository());
        }

        @Bean
        public IPagingWidgetRepository pagingWidgetRepository() {
            return new PagingWidgetRepository(inMemoryWidgetRepository());
        }

        @Bean
        public IWidgetRepository inMemoryWidgetRepository() {
            return new InMemoryWidgetRepository();
        }
    }
}
