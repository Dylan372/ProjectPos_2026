@echo off
rem Run the automated test suite (8 test groups)
set "JAVA=java"
where java >nul 2>nul
if errorlevel 1 set "JAVA=..\_build\jdk\jdk-17.0.20.1+1\bin\java.exe"
"%JAVA%" -cp out pos.TestSuite
pause
