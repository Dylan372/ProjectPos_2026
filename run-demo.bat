@echo off
rem Run the scripted end-to-end demo (ideal for the demo video)
set "JAVA=java"
where java >nul 2>nul
if errorlevel 1 set "JAVA=..\_build\jdk\jdk-17.0.20.1+1\bin\java.exe"
"%JAVA%" -cp out pos.Main --demo
pause
