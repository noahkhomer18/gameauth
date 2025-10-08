@echo off
REM GameAuth Build Script for Windows
REM This script builds the GameAuth project with Maven

echo === GameAuth Build Script ===
echo Building GameAuth project...

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6+ and try again
    exit /b 1
)

echo Maven version:
mvn --version

echo.
echo Cleaning previous build...
mvn clean

echo.
echo Compiling project...
mvn compile

echo.
echo Running tests...
mvn test

echo.
echo Generating test coverage report...
mvn jacoco:report

echo.
echo Packaging application...
mvn package

echo.
echo === Build completed successfully ===
echo Artifacts generated in target/ directory
echo Test coverage report: target/site/jacoco/index.html
