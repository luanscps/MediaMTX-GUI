#!/bin/bash
# Build MediaMTX GUI - Linux/macOS
set -e

mkdir -p out lib

# Baixa JARs automaticamente se nao existirem
if [ ! -f lib/flatlaf-3.7.1.jar ]; then
    echo "Baixando FlatLaf 3.7.1..."
    curl -L -o lib/flatlaf-3.7.1.jar \
      "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.7.1/flatlaf-3.7.1.jar"
fi

if [ ! -f lib/miglayout-swing-11.3.jar ]; then
    echo "Baixando MigLayout swing..."
    curl -L -o lib/miglayout-swing-11.3.jar \
      "https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar"
fi

if [ ! -f lib/miglayout-core-11.3.jar ]; then
    echo "Baixando MigLayout core..."
    curl -L -o lib/miglayout-core-11.3.jar \
      "https://repo1.maven.org/maven2/com/miglayout/miglayout-core/11.3/miglayout-core-11.3.jar"
fi

echo "Compilando..."
find src -name "*.java" > sources.txt
javac -cp "lib/flatlaf-3.7.1.jar:lib/miglayout-swing-11.3.jar:lib/miglayout-core-11.3.jar" -d out @sources.txt

echo "Empacotando JAR (fat jar)..."
cd out
jar xf ../lib/flatlaf-3.7.1.jar
jar xf ../lib/miglayout-swing-11.3.jar
jar xf ../lib/miglayout-core-11.3.jar
cd ..
jar cfm MediaMTX-GUI.jar MANIFEST.MF -C out .

rm sources.txt
echo ""
echo "Build concluido!"
echo "   Execute: java -jar MediaMTX-GUI.jar"
