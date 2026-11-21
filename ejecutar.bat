@echo off
cd /d "%~dp0"
where java >nul 2>nul
if errorlevel 1 (
  echo No se encontro Java. Instala JDK 17 o superior: https://adoptium.net
  pause & exit /b 1
)
if exist mvnw.cmd (
  call mvnw.cmd spring-boot:run
) else (
  where mvn >nul 2>nul
  if errorlevel 1 (
    echo No se encontro Maven. Instalalo desde https://maven.apache.org o agrega el wrapper ^(ver README^).
    pause & exit /b 1
  )
  call mvn spring-boot:run
)
