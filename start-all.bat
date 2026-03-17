@echo off
title MeshGate - Start All Services
echo.
echo ============================================
echo   MeshGate - Starting All Services
echo ============================================
echo.

set ROOT=%~dp0

echo [1/6] Starting Docker infrastructure (Postgres, RabbitMQ)...
echo ----------------------------------------
cd /d "%ROOT%"
docker-compose up -d
echo Waiting 15 seconds for containers to initialize...
timeout /t 15 /nobreak >nul
echo.

echo [2/6] Starting Eureka Server (port 8761)...
echo ----------------------------------------
start "MeshGate - Eureka Server" cmd /c "cd /d "%ROOT%eureka-server" && mvn spring-boot:run"
echo Waiting 20 seconds for Eureka to be ready...
timeout /t 20 /nobreak >nul
echo.

echo [3/6] Starting API Gateway (port 8080)...
echo ----------------------------------------
start "MeshGate - API Gateway" cmd /c "cd /d "%ROOT%api-gateway" && mvn spring-boot:run"
timeout /t 5 /nobreak >nul
echo.

echo [4/6] Starting Auth Service (port 8086)...
echo ----------------------------------------
start "MeshGate - Auth Service" cmd /c "cd /d "%ROOT%auth-service" && mvn spring-boot:run"
timeout /t 3 /nobreak >nul
echo.

echo [5/6] Starting User Service (port 8082)...
echo ----------------------------------------
start "MeshGate - User Service" cmd /c "cd /d "%ROOT%user-service" && mvn spring-boot:run"
timeout /t 3 /nobreak >nul
echo.

echo [6/6] Starting Billing + Notification Services...
echo ----------------------------------------
start "MeshGate - Billing Service" cmd /c "cd /d "%ROOT%billing-service" && mvn spring-boot:run"
start "MeshGate - Notification Service" cmd /c "cd /d "%ROOT%notification-service" && mvn spring-boot:run"
echo.

echo ============================================
echo   ALL SERVICES LAUNCHED
echo ============================================
echo.
echo   Eureka Dashboard:    http://localhost:8761
echo   API Gateway:         http://localhost:8080
echo   RabbitMQ Management: http://localhost:15672
echo   Nginx Proxy:         http://localhost:80
echo.
echo   Each service runs in its own window.
echo   Close this window when done, or press
echo   any key to exit.
echo ============================================
pause
