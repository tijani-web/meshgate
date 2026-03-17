@echo off
title MeshGate - Build All Services
echo.
echo ============================================
echo   MeshGate - Clean ^& Build All Services
echo ============================================
echo.

set ROOT=%~dp0
set FAILED=0

echo [1/5] Building eureka-server...
echo ----------------------------------------
cd /d "%ROOT%eureka-server"
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [FAIL] eureka-server build failed!
    set FAILED=1
) else (
    echo [OK]   eureka-server
)
echo.

echo [2/5] Building api-gateway...
echo ----------------------------------------
cd /d "%ROOT%api-gateway"
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [FAIL] api-gateway build failed!
    set FAILED=1
) else (
    echo [OK]   api-gateway
)
echo.

echo [3/5] Building auth-service...
echo ----------------------------------------
cd /d "%ROOT%auth-service"
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [FAIL] auth-service build failed!
    set FAILED=1
) else (
    echo [OK]   auth-service
)
echo.

echo [4/5] Building user-service...
echo ----------------------------------------
cd /d "%ROOT%user-service"
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [FAIL] user-service build failed!
    set FAILED=1
) else (
    echo [OK]   user-service
)
echo.

echo [5/5] Building billing-service...
echo ----------------------------------------
cd /d "%ROOT%billing-service"
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [FAIL] billing-service build failed!
    set FAILED=1
) else (
    echo [OK]   billing-service
)
echo.

echo [6/6] Building notification-service...
echo ----------------------------------------
cd /d "%ROOT%notification-service"
call mvn clean package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [FAIL] notification-service build failed!
    set FAILED=1
) else (
    echo [OK]   notification-service
)
echo.

echo ============================================
if %FAILED% equ 1 (
    echo   BUILD COMPLETED WITH ERRORS
) else (
    echo   ALL SERVICES BUILT SUCCESSFULLY
)
echo ============================================
echo.
cd /d "%ROOT%"
pause
