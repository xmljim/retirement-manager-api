#!/usr/bin/env bash
# Pre-push quality checks for retirement-manager-api
# Runs full quality gate: tests, PMD, SpotBugs, Checkstyle, JaCoCo

set -e

echo "Running pre-push quality gate..."

# Get the project root
PROJECT_ROOT="$(git rev-parse --show-toplevel)"
cd "$PROJECT_ROOT"

# Check if Docker is running for tests (PostgreSQL needed)
if ! docker info >/dev/null 2>&1; then
    echo "Warning: Docker not running, skipping integration tests"
    echo "Running static analysis only..."
    ./gradlew pmdMain pmdTest spotbugsMain checkstyleMain checkstyleTest --quiet
else
    # Run full quality gate
    echo "  Running full quality gate (tests + static analysis)..."
    ./gradlew qualityGate --quiet
fi

echo "Pre-push quality gate passed!"
