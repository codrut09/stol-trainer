@echo off
setlocal enabledelayedexpansion

REM Incearca sa gaseasca JAVA_HOME din IntelliJ
for /d %%i in ("C:\Program Files\JetBrains\IntelliJ IDEA*") do (
    if exist "%%i\jbr\bin\java.exe" (
        set "JAVA_HOME=%%i\jbr"
        goto found
    )
)

REM Incearca alt path pentru IntelliJ
for /d %%i in ("C:\Program Files (x86)\JetBrains\IntelliJ IDEA*") do (
    if exist "%%i\jbr\bin\java.exe" (
        set "JAVA_HOME=%%i\jbr"
        goto found
    )
)

REM Daca nu gaseste, incearca AppData
for /d %%i in ("%APPDATA%\Local\JetBrains\IntelliJ*") do (
    if exist "%%i\jbr\bin\java.exe" (
        set "JAVA_HOME=%%i\jbr"
        goto found
    )
)

:found
if not defined JAVA_HOME (
    echo ERROR: Java nu s-a gasit. Verifica ca ai IntelliJ instalat.
    pause
    exit /b 1
)

echo JAVA_HOME gasit: !JAVA_HOME!
cd /d E:\Projects\stol-trainer
call mvnw.cmd clean compile
pause
