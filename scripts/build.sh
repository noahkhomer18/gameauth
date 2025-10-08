#!/bin/bash

# GameAuth Build Script
# This script builds the GameAuth project with Maven

set -e  # Exit on any error

echo "=== GameAuth Build Script ==="
echo "Building GameAuth project..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6+ and try again"
    exit 1
fi

# Check Maven version
echo "Maven version:"
mvn --version

echo ""
echo "Cleaning previous build..."
mvn clean

echo ""
echo "Compiling project..."
mvn compile

echo ""
echo "Running tests..."
mvn test

echo ""
echo "Generating test coverage report..."
mvn jacoco:report

echo ""
echo "Packaging application..."
mvn package

echo ""
echo "=== Build completed successfully ==="
echo "Artifacts generated in target/ directory"
echo "Test coverage report: target/site/jacoco/index.html"
