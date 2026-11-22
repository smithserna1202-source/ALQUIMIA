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
| Func. | Método | Ruta | Qué hace | Errores |
|---|---|---|---|---|
| F1 | POST | `/process` | Calcula con los 3 dispositivos (sin guardar) | 400, 422 |
| F2 | POST | `/devices` | Registra un dispositivo (201) | 400, 409 si ya existe |
| F3 | GET | `/devices`, `/devices/{id}` | Consulta lo registrado | 404 |
| F3 | GET | `/calculate` | Procesa lo acumulado | 422 si faltan datos |
| F4 | PUT | `/devices/{id}` | Reemplazo: `distance` y `messageFragment` obligatorios | 400, 404 |
| F5 | PATCH | `/devices/{id}` | Cambia solo lo enviado (mínimo un campo) | 400, 404 |
| F6 | DELETE | `/devices/{id}` | Elimina (204) | 404 |

Errores: `{"error": "mensaje"}`. 400 = dato inválido o dispositivo desconocido, 404 = dispositivo sin información,
409 = ya registrado, 422 = datos insuficientes o geometría degenerada.

## Decisiones de diseño
- **Dominio** (`domain`): `KnownDevice` (catálogo y posiciones), `DeviceInfo` (inmutable, valida), servicios
  `TrilaterationCalculator` y `MessageReconstructor`, y excepciones propias. Sin Spring ni HTTP.
- **Aplicación** (`application`): un puerto de entrada por funcionalidad (`port.in`), puerto de salida
  `DeviceRepositoryPort` (`port.out`) y `AlquimiaService`, sin anotaciones de framework.
- **Infraestructura** (`infrastructure`): controlador REST con DTOs, `ApiExceptionHandler` (dominio -> HTTP),
  repositorio en memoria y `BeanConfig`, que conecta todo.
- **Mensaje:** cada dispositivo aporta un fragmento de texto; los vacíos se ignoran y se unen en el orden fijo
  STAR Labs, CCPD, S.T.A.R. Satellite (no en el de llegada). El enunciado no define un formato más detallado.
- **Registrar** un dispositivo que ya existe es error (409); para cambiarlo están PUT y PATCH.

## Tests
`mvn test`

## Cr�ditos
Proyecto desarrollado a partir de la base de Jeison (https://github.com/jeison12021989/operacion-alquimia), con su autorizaci�n.
