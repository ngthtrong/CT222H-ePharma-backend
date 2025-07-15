@echo off
echo ================================
echo    VEGETA BACKEND DOCKER SETUP
echo ================================

echo.
echo Building Docker image...
docker-compose build

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to build Docker image
    pause
    exit /b 1
)

echo.
echo Starting application...
docker-compose up -d

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to start application
    pause
    exit /b 1
)

echo.
echo ================================
echo Application is starting up...
echo ================================
echo.
echo Backend URL: http://localhost:8081
echo Health Check: http://localhost:8081/actuator/health
echo API Documentation: http://localhost:8081/swagger-ui.html
echo.
echo Checking application status...
timeout /t 30 /nobreak > nul

:check_health
curl -f http://localhost:8081/actuator/health > nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ Application is healthy and ready!
    echo.
    echo To view logs: docker-compose logs -f
    echo To stop: docker-compose down
    goto end
) else (
    echo Waiting for application to start...
    timeout /t 5 /nobreak > nul
    goto check_health
)

:end
pause
