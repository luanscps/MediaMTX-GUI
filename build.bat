@echo off
echo Compilando MediaMTX GUI...
mkdir out 2>nul
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
jar cfm MediaMTX-GUI.jar MANIFEST.MF -C out .
del sources.txt
echo.
echo Build concluido!
echo Execute: java -jar MediaMTX-GUI.jar
pause
