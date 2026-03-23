#!/bin/bash
# Build MediaMTX GUI - Linux/macOS
set -e
mkdir -p out
echo "Compilando..."
find src -name "*.java" > sources.txt
javac -d out @sources.txt
echo "Empacotando JAR..."
jar cfm MediaMTX-GUI.jar MANIFEST.MF -C out .
rm sources.txt
echo ""
echo "Build concluido!"
echo "   Execute: java -jar MediaMTX-GUI.jar"
