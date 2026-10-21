@echo off
rem Compile the PoS system (JDK 17+, UTF-8 sources)
set "JAVAC=javac"
where javac >nul 2>nul
if errorlevel 1 set "JAVAC=..\_build\jdk\jdk-17.0.20.1+1\bin\javac.exe"
if not exist out mkdir out
"%JAVAC%" -encoding UTF-8 -d out src\pos\*.java
if %errorlevel%==0 echo Compilation OK.
pause
