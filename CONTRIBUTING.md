# Contributing to Retirement Manager API

## Quality Standards

### Testing Requirements

**Coverage Target: 60%+ line coverage**

| Layer | Test Type | Tools |
|-------|-----------|-------|
| Repository | Integration | `@DataJpaTest`, Testcontainers |
| Service | Unit | JUnit 5, Mockito |
| Controller | Unit + Integration | MockMvc, TestRestTemplate |

Run all tests:
```bash
./gradlew test
```

### Quality Gate

**Must pass before any PR merge:**

```bash
./gradlew qualityGate
```

This runs:
- PMD 7.16.0 - Static analysis
- SpotBugs 4.9.8 - Bug detection
- Checkstyle (Sun) - Code style
- JaCoCo - Coverage enforcement (60% minimum)
- JUnit 5 - All tests

### Code Style

Follow modern Java 25 idioms:
- Use records for DTOs and value objects
- Use pattern matching where applicable
- Prefer `var` for local variables with obvious types
- Use sealed classes/interfaces where appropriate
- Prefer Stream API over imperative loops
- Use `Optional` for nullable return values (never for parameters)

### PR Review Checklist

Before requesting review:
- [ ] `./gradlew qualityGate` passes
- [ ] New code has tests
- [ ] OpenAPI spec updated (run app, check /api-docs)
- [ ] No TODOs without linked issues
- [ ] Commit messages follow conventional commits

Reviewer checklist:
- [ ] Code follows modern Java idioms
- [ ] Tests are meaningful (not just coverage padding)
- [ ] API design is RESTful and consistent
- [ ] No security vulnerabilities (injection, etc.)
- [ ] Database migrations are reversible

## Issue Workflow

We use `beads` (bd) for issue tracking:

```bash
# Find available work
bd ready

# Start working on an issue
bd update <id> --status in_progress

# When done
bd close <id>

# Sync changes to git
bd sync
```

### Branch Naming

```
feature/<issue-id>-short-description
bugfix/<issue-id>-short-description
```

Example: `feature/retirement-manager-api-zqf.1-persons-migration`

## Getting Help

- Check existing issues: `bd list`
- Ask in PR comments for clarification
- Reference the design spec in `docs/design/`
