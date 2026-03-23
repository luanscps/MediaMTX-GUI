#!/bin/bash
# Build MediaMTX GUI - Linux/macOS
set -e

mkdir -p out lib

# Baixa JARs automaticamente se nao existirem
if [ ! -f lib/flatlaf-3.4.jar ]; then
    echo "Baixando FlatLaf..."
    curl -L -o lib/flatlaf-3.4.jar \
      "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4/flatlaf-3.4.jar"
fi

if [ ! -f lib/miglayout-swing-11.3.jar ]; then
    echo "Baixando MigLayout..."
    curl -L -o lib/miglayout-swing-11.3.jar \
      "https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar"
fi

echo "Compilando..."
find src -name "*.java" > sources.txt
javac -cp "lib/flatlaf-3.4.jar:lib/miglayout-swing-11.3.jar" -d out @sources.txt

echo "Empacotando JAR (fat jar)..."
# Extrai os JARs de dependencia dentro do out/ para embuti-los no JAR final
cd out
jar xf ../lib/flatlaf-3.4.jar
jar xf ../lib/miglayout-swing-11.3.jar
cd ..
jar cfm MediaMTX-GUI.jar MANIFEST.MF -C out .

rm sources.txt
echo ""
echo "Build concluido!"
echo "   Execute: java -jar MediaMTX-GUI.jar"
