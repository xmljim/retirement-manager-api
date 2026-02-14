package com.xmljim.retirement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for the Spring Boot application context.
 *
 * <p>Verifies that the application context loads correctly with
 * a PostgreSQL test container.</p>
 *
 * @since 1.0
 */
@SpringBootTest
@Testcontainers
class RetirementManagerApplicationTests {

    /** PostgreSQL test container for integration tests. */
    @Container
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("retirement")
                    .withUsername("retirement")
                    .withPassword("retirement");

    /** The Spring application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Configures dynamic properties for the test container.
     *
     * @param registry the dynamic property registry
     */
    @DynamicPropertySource
    static void configureProperties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    /**
     * Verifies that the Spring application context loads successfully.
     */
    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }
}
