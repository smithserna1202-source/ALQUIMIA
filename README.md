# Operación Alquimia – Protocolo Savitar

API REST (Java + Spring Boot) que ubica el laboratorio por **trilateración** con 3 dispositivos
(`STAR Labs`, `CCPD`, `S.T.A.R. Satellite`) y reconstruye el mensaje con los fragmentos recibidos.

## Requisitos
- **Java 17 o superior** (https://adoptium.net)
- **Maven 3.6+** (o el wrapper `mvnw`, ver abajo)

## Ejecutar
| Sistema | Comando |
|---|---|
| Windows | doble clic en `ejecutar.bat` |
| Linux / macOS | `./ejecutar.sh` |
| Cualquiera | `mvn spring-boot:run` |

Abre http://localhost:8080 para la página de prueba. Otro puerto: variable `PORT`.
Para no instalar Maven: con internet, ejecuta una vez `mvn wrapper:wrapper`.

## Dispositivos fijos
| Dispositivo | Posición (x, y) |
|---|---|
| STAR Labs | (-500, -200) |
| CCPD | (100, -100) |
| S.T.A.R. Satellite | (500, 100) |

## Endpoints (`/api/alquimia`)
| Método | Ruta | Qué hace |
|---|---|---|
| POST | `/process` | Calcula con una lista de dispositivos |
| POST | `/devices` | Registra un dispositivo |
| GET | `/calculate` | Calcula con los registrados |
| PUT | `/devices/{id}` | Reemplaza (requiere `distance`) |
| PATCH | `/devices/{id}` | Actualiza solo lo enviado |
| DELETE | `/devices/{id}` | Elimina |

```bash
curl -X POST localhost:8080/api/alquimia/process -H "Content-Type: application/json" -d '[
 {"deviceId":"STAR Labs","distance":538.52,"messageFragment":"Hola"},
 {"deviceId":"CCPD","distance":141.42,"messageFragment":"desde"},
 {"deviceId":"S.T.A.R. Satellite","distance":509.90,"messageFragment":"Alquimia"}]'
```

## Tests
`mvn test`
