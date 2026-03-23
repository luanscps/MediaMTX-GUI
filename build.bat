@echo off
echo Compilando MediaMTX GUI...

:: Cria pasta de saida e pasta para libs
mkdir out  2>nul
mkdir lib  2>nul

:: Verifica se os JARs existem; baixa automaticamente via curl se nao existirem
if not exist lib\flatlaf-3.4.jar (
    echo Baixando FlatLaf...
    curl -L -o lib\flatlaf-3.4.jar ^
      "https://repo1.maven.org/maven2/com/formdev/flatlaf/3.4/flatlaf-3.4.jar"
)
if not exist lib\miglayout-swing-11.3.jar (
    echo Baixando MigLayout...
    curl -L -o lib\miglayout-swing-11.3.jar ^
      "https://repo1.maven.org/maven2/com/miglayout/miglayout-swing/11.3/miglayout-swing-11.3.jar"
)

:: Lista todos os .java
dir /s /b src\*.java > sources.txt

:: Compila com os dois JARs no classpath
javac -cp "lib\flatlaf-3.4.jar;lib\miglayout-swing-11.3.jar" -d out @sources.txt

:: Empacota o JAR incluindo as libs dentro dele (fat JAR)
cd out
jar xf ..\lib\flatlaf-3.4.jar
jar xf ..\lib\miglayout-swing-11.3.jar
cd ..
jar cfm MediaMTX-GUI.jar MANIFEST.MF -C out .

del sources.txt
echo.
echo Build concluido!
echo Execute: java -jar MediaMTX-GUI.jar
pause
