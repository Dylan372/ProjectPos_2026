@echo off
rem Run the interactive PoS console (run compile.bat first if out\ is missing)
set "JAVA=java"
where java >nul 2>nul
if errorlevel 1 set "JAVA=..\_build\jdk\jdk-17.0.20.1+1\bin\java.exe"
"%JAVA%" -cp out pos.Main
pause
