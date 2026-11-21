#!/usr/bin/env sh
# Arranque en Linux / macOS. Usa ./mvnw si existe, si no el Maven instalado.
cd "$(dirname "$0")" || exit 1
if ! command -v java >/dev/null 2>&1; then
  echo "No se encontro Java. Instala JDK 17 o superior: https://adoptium.net"; exit 1
fi
if [ -f ./mvnw ]; then chmod +x ./mvnw; ./mvnw spring-boot:run
elif command -v mvn >/dev/null 2>&1; then mvn spring-boot:run
else echo "No se encontro Maven. Instalalo desde https://maven.apache.org o agrega el wrapper (ver README)."; exit 1; fi
