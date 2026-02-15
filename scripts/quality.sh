#!/bin/bash
# Fast quality gate runner - use this instead of waiting for Claude's bash tool

set -e

echo "Running quality gate..."
cd .. & ./gradlew qualityGate 2>&1 | tee /tmp/quality-gate-output.txt

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Quality gate PASSED"
else
    echo ""
    echo "✗ Quality gate FAILED - check output above"
    exit 1
fi
