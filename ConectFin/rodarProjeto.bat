@echo off
cls
echo Compilando o projeto...

cd /d "%~dp0"

echo Compilando classes do modelo...
javac -d bin -cp "lib/*" src\model\*.java

echo Compilando interfaces e implementações do repositório...
javac -d bin -cp "lib/*;bin" src\repository\*.java

echo Compilando serviços...
javac -d bin -cp "lib/*;bin" src\service\*.java

echo Compilando utilitários...
javac -d bin -cp "lib/*;bin" src\util\*.java

echo Compilando classe principal...
javac -d bin -cp "lib/*;bin" src\Main.java

if %errorlevel% neq 0 (
    echo.
    echo Houve um erro na compilacao. Verifique seu código!
    pause
    exit /b
)

echo.
echo Projeto compilado com sucesso!
echo.
echo Iniciando a aplicacao...
java -cp "lib/*;bin" Main
pause
