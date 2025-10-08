#!/bin/bash

# GameAuth Examples Runner Script
# This script compiles and runs the example programs

set -e  # Exit on any error

echo "=== GameAuth Examples Runner ==="
echo "Running GameAuth examples..."

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed or not in PATH"
    echo "Please install Maven 3.6+ and try again"
    exit 1
fi

# Compile the project first
echo "Compiling project..."
mvn compile

echo ""
echo "Running Basic Authentication Example..."
echo "========================================"
mvn exec:java -Dexec.mainClass="com.gamingroom.gameauth.examples.BasicAuthExample" -Dexec.classpathScope=compile

echo ""
echo "Running Advanced Authentication Example..."
echo "========================================="
mvn exec:java -Dexec.mainClass="com.gamingroom.gameauth.examples.AdvancedAuthExample" -Dexec.classpathScope=compile

echo ""
echo "=== Examples completed successfully ==="
