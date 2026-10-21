@echo off
rem Compile the PoS system (JDK 17+, UTF-8 sources)
if not exist out mkdir out
javac -encoding UTF-8 -d out src\pos\*.java
if %errorlevel%==0 echo Compilation OK.
