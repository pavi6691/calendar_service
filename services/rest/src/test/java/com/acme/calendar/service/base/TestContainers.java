package com.acme.calendar.service.base;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class TestContainers {
    
    protected static boolean testContainers = false;
    private static final String POSTGRESQL_IMAGE = "postgres:15.5-alpine";
    @Container
    public static final PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer(POSTGRESQL_IMAGE)
            .withDatabaseName("acme")
            .withUsername("postgresql-username")
            .withPassword("postgresql-password");
    
    static {

        // postgresql
        postgresqlContainer
                .withReuse(true)
                .withNetworkAliases("postgresql");
        postgresqlContainer.start();
    }
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.username", () -> "postgresql-username");
        registry.add("spring.datasource.password", () -> "postgresql-password");
        registry.add("default.db.response.size", () -> 20);
        registry.add("service.thread.pool.size", () -> 8);
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        testContainers = true;
    }
}
