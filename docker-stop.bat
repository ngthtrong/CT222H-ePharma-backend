@echo off
echo ================================
echo   STOPPING VEGETA BACKEND
echo ================================

echo.
echo Stopping Docker containers...
docker-compose down

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to stop containers
    pause
    exit /b 1
)

echo.
echo ✅ Application stopped successfully!
echo.
echo To remove images: docker-compose down --rmi all
echo To remove volumes: docker-compose down -v
echo.
pause
