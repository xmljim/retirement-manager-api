# CLAUDE.md

This file provides guidance to Claude Code when working with this repository.

## Project Overview

Retirement Manager API - Java 25 + Spring Boot REST API for retirement lifecycle management.

## Build & Run

```bash
# Start PostgreSQL
docker compose up -d

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Build
./gradlew build
```

## Key Locations

- `src/main/java/com/xmljim/retirement/` - Main source code
  - `api/` - REST controllers
  - `domain/` - Domain entities (records)
  - `service/` - Business logic
  - `repository/` - Data access (Spring Data JPA)
  - `rules/` - Contribution/distribution rules engine
  - `calc/` - Financial calculations
- `src/main/resources/db/migration/` - Flyway migrations
- `src/test/` - Tests (using Testcontainers)

## API Documentation

When running locally: http://localhost:8080/swagger-ui.html

## Database

PostgreSQL via Docker Compose. Migrations managed by Flyway.

## Related Repository

Frontend: https://github.com/xmljim/retirement-manager-ui
