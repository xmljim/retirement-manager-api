#!/usr/bin/env bash
# Pre-commit quality checks for retirement-manager-api
# Runs fast checks: compile + checkstyle on staged files

set -e

echo "Running pre-commit quality checks..."

# Get the project root
PROJECT_ROOT="$(git rev-parse --show-toplevel)"
cd "$PROJECT_ROOT"

# Run compile check (fast)
echo "  Compiling..."
./gradlew compileJava compileTestJava --quiet

# Run checkstyle on main code (fast style check)
echo "  Running checkstyle..."
./gradlew checkstyleMain --quiet

echo "Pre-commit checks passed!"
