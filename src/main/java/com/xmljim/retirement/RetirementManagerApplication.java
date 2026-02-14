package com.xmljim.retirement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application entry point for the Retirement Manager API.
 *
 * <p>This class bootstraps the Spring application context and starts
 * the embedded web server.</p>
 *
 * @since 1.0
 */
@SpringBootApplication
public final class RetirementManagerApplication {

    /**
     * Private constructor to prevent instantiation.
     */
    private RetirementManagerApplication() {
        // Spring Boot entry point - not instantiated directly
    }

    /**
     * Application entry point.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(final String[] args) {
        SpringApplication.run(RetirementManagerApplication.class, args);
    }
}
