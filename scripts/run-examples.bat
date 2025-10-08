@echo off
REM GameAuth Examples Runner Script for Windows
REM This script compiles and runs the example programs

echo === GameAuth Examples Runner ===
echo Running GameAuth examples...

REM Check if Maven is installed
mvn --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Maven is not installed or not in PATH
    echo Please install Maven 3.6+ and try again
    exit /b 1
)

REM Compile the project first
echo Compiling project...
mvn compile

echo.
echo Running Basic Authentication Example...
echo ========================================
mvn exec:java -Dexec.mainClass="com.gamingroom.gameauth.examples.BasicAuthExample" -Dexec.classpathScope=compile

echo.
echo Running Advanced Authentication Example...
echo =========================================
mvn exec:java -Dexec.mainClass="com.gamingroom.gameauth.examples.AdvancedAuthExample" -Dexec.classpathScope=compile

echo.
echo === Examples completed successfully ===
