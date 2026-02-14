# CLAUDE.md

This file provides guidance to Claude Code when working with this repository.

## Project Overview

Retirement Manager API - Java 25 + Spring Boot 4.0 REST API for retirement lifecycle management.

## Build & Run

```bash
# Start PostgreSQL (Docker or Podman)
docker compose up -d
# Or with Podman:
podman-compose up -d

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Build
./gradlew build
```

## Quality Gates

**IMPORTANT:** Run quality gates before committing code changes.

```bash
# Run all quality checks (PMD, SpotBugs, Checkstyle, JaCoCo, tests)
./gradlew qualityGate

# Or run individual checks:
./gradlew pmdMain pmdTest           # Static analysis
./gradlew spotbugsMain spotbugsTest # Bug detection
./gradlew checkstyleMain checkstyleTest  # Code style
./gradlew test jacocoTestReport     # Tests with coverage
```

Quality tool configurations:
- PMD: `config/pmd/ruleset.xml`
- SpotBugs: `config/spotbugs/exclude.xml`
- Checkstyle: `config/checkstyle/checkstyle.xml`
- JaCoCo: Minimum 50% coverage threshold (temporarily 0% for bootstrap)

## Issue Tracking

This project uses **beads** for issue tracking.

```bash
bd ready              # Find available work
bd show <id>          # View issue details
bd update <id> --status in_progress  # Claim work
bd close <id>         # Complete work
bd sync               # Sync with git
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
- `config/` - Quality tool configurations

## API Documentation

When running locally:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs (used for client generation)

## Database

PostgreSQL 17 via Docker/Podman Compose on port 5433. Migrations managed by Flyway.

## Related Repository

Frontend: https://github.com/xmljim/retirement-manager-ui
