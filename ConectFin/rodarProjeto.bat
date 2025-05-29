@echo off
cls
echo Compilando o projeto...

cd /d "%~dp0"

echo Limpando diretorio bin...
if exist bin rmdir /s /q bin
mkdir bin

echo Compilando classes do modelo...
javac -d bin -cp "lib/*" src\model\*.java

echo Compilando utilitarios...
javac -d bin -cp "lib/*;bin" src\util\*.java

echo Compilando interfaces e implementacoes do repositorio...
javac -d bin -cp "lib/*;bin" src\repository\*.java

echo Compilando servicos...
javac -d bin -cp "lib/*;bin" src\service\*.java

echo Compilando classe principal...
javac -d bin -cp "lib/*;bin" src\Main.java

if %errorlevel% neq 0 (
    echo.
    echo Houve um erro na compilacao. Verifique seu codigo!
    pause
    exit /b
)

echo.
echo Projeto compilado com sucesso!
echo.
echo Iniciando a aplicacao...
java -cp "lib/*;bin" Main
pause
