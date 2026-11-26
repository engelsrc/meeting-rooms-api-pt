# Meeting Rooms API

API REST para reservar salas de reuniones (prueba técnica).

Repositorio: https://github.com/engelsrc/meeting-rooms-api-pt

## Cómo ejecutarlo

### Requisitos

- Java **17+**
- Maven 3.9+

### Perfiles Spring

| Perfil | Uso | Notas |
|--------|-----|--------|
| `prod` (default) | Demo rápida | H2 en memoria; JWT demo en `application-prod.yml` |
| `local` | Desarrollo local | H2 en memoria + consola H2; JWT demo en `application-local.yml` |

Perfil activo por defecto: `prod` (`src/main/resources/application.yml`).

Propiedades relevantes (valores actuales = **demo de la prueba**; no subas secretos reales):

- `spring.datasource.url` / `username` / `password`
- `app.jwt.secret` (mín. longitud HS256)
- `app.jwt.expiration-ms`

### Clonar el repositorio

Clona el repo y usa la rama `main` o `dev` (ambas sirven para ejecutar la demo):

```bash
git clone https://github.com/engelsrc/meeting-rooms-api-pt.git
cd meeting-rooms-api-pt
git checkout main   # o: git checkout dev
```

### Arranque

Luego arranca la aplicación:

```bash
mvn spring-boot:run
# o
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

URL base de los ejemplos: **`http://localhost:8080`**

Documentación interactiva (Swagger UI): **`http://localhost:8080/swagger-ui/index.html`**

Usuario seed (migración V4): `admin` / `admin` (también existe `user` / `user`).

### Extras (tiempo restante de la prueba)

Con el tiempo restante de la prueba se incorporaron:

- **Documentación de la API** (OpenAPI / Swagger UI en la URL anterior).
- **Postman Collection:** `docs/postman/Meeting-Rooms-API.postman_collection.json` (importar en Postman; flujo feliz + un negativo representativo por endpoint).

## Endpoints

| Método | Ruta | Auth |
|--------|------|------|
| POST | `/api/auth/login` | no |
| POST | `/api/rooms` | sí (JWT) |
| GET | `/api/rooms` | no |
| POST | `/api/reservations` | sí (JWT) |
| GET | `/api/reservations` | no |
| POST | `/api/reservations/cancel` | sí (JWT) |

Consultas `GET /api/**` y el login son públicos; el resto de mutaciones bajo `/api/**` requieren Bearer JWT.

Filtros de listado de reservas (query opcionales): `roomId`, `date`, `page`, `size`.

## Flujo mínimo

```bash
# 1) Login → accessToken
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin\"}"

# 2) Crear sala (mutación, auth sí) → 201
curl -s -X POST http://localhost:8080/api/rooms \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Sala Demo\",\"capacity\":6,\"location\":\"Piso 1\"}"

# 3) Listar salas (consulta, auth no) → 200
curl -s http://localhost:8080/api/rooms
```

Reserva (referencia): body `{ "roomId": 1, "startAt": "2026-12-15T10:00:00-06:00", "endAt": "2026-12-15T11:00:00-06:00" }` con Bearer → **201** (o **409** si solapa).  
Cancelar: `POST /api/reservations/cancel` con `{ "reservationId": 1 }` y Bearer.

## Tests

```bash
mvn test
```

Suite mínima: 3 clases `@SpringBootTest` + MockMvc (`CreateRoomAuthTest`, `ReservationOverlapTest`, `ListRoomsPublicTest`) + helper `AuthTestSupport`.

## Decisiones

- **Stack:** Java 17+, Spring Boot 3.3 y Maven. Uso H2 con Flyway (migraciones V1 a V4) y autenticación con JWT HS256 en lugar de Basic.
- **Arquitectura:** Clean Architecture (CA) con un Command Query Responsibility Segregation (CQRS) ligero. Las peticiones entran por los controladores REST, pasan a los handlers de comandos y consultas, luego al dominio y finalmente a los adaptadores JPA. Es un solo módulo, sin event bus ni microservicios, porque con unas 3 horas de plazo habría sido sobreingeniería.
- **Autenticación:** Las operaciones que modifican datos (crear y cancelar) requieren token Bearer; los GET son públicos. El login es `POST /api/auth/login` y dejé dos usuarios de prueba: `admin`/`admin` y `user`/`user`.
- **Reservas:** El intervalo es semiabierto `[inicio, fin)`, así que una reserva que termina a las 11:00 no choca con otra que empieza a las 11:00. Si una reserva se solapa con otra activa en la misma sala, devuelve **409**; si los datos no son válidos, **400**. Los mensajes de error están en inglés y centralizados en `ErrorMessages`.
- **Listados:** Salas y reservas vienen paginadas, y las reservas se pueden filtrar por `roomId` y `date`.
- **Perfiles:** `local` y `prod` usan secretos de demostración en el YAML. No hay secretos reales de producción en el repositorio.
- **Tests:** Hay al menos tres tests de integración con MockMvc y H2: `CreateRoomAuthTest`, `ReservationOverlapTest` y `ListRoomsPublicTest`. Todos hacen login HTTP real en vez de apoyarse solo en `@WithMockUser`.
- **Proceso:** Trabajé con SDD por requisito funcional (`spec/00N-…`): una rama `feature/<carpeta>` y una Issue de GitHub por cada RF, con merge a `dev` al cerrarla. Dentro de cada rama, hice un commit por cada task del requisito, de modo que el historial muestra el avance paso a paso y se puede revisar tarea por tarea.

## Qué haría con más tiempo

1. **Pasar a PostgreSQL real con Docker Compose.** Hoy los perfiles `local` y `prod` usan una base H2 de demo; con más tiempo los levantaría sobre PostgreSQL para que el entorno se parezca a uno de producción.
2. **Ampliar los tests hasta superar el 85 % de cobertura.** Agregaría pruebas para la cancelación de reservas, los filtros del listado, el 409 por nombre de sala duplicado y los casos borde del solape (por ejemplo, reservas que se tocan en el límite exacto de la hora). Mediría la cobertura con una herramienta como JaCoCo y la mantendría como criterio mínimo.
3. **Reforzar la seguridad del JWT.** Los secretos vendrían solo de variables de entorno o de un gestor de secretos, nunca del repositorio. Además, implementaría rotación de claves y, opcionalmente, refresh tokens.
4. **Sumar observabilidad básica.** Habilitaría Spring Boot Actuator para exponer health y métricas, y registraría con logging estructurado los conflictos de reserva, para poder detectarlos y analizarlos fácilmente.
5. **Mejorar la documentación.** Completaría el README con ejemplos de uso de la API (peticiones y respuestas) y documentaría las clases con JavaDoc.

## Cómo usé la IA

Hechos aportados por el autor de la prueba (formateados; sin ampliar):

- **Qué organicé:** Antes de pedirle código, preparé el entorno de trabajo de la IA: definí comandos de Cursor (`ssd-cmd-*`), skills y una constitución del proyecto. Con esto acoté lo que la IA podía hacer, aceleré el flujo SDD y mantuve criterios consistentes a lo largo de todo el desarrollo.
- **Qué le pedí:** Le pedí que armara la estructura base del proyecto siguiendo un enfoque SDD (desarrollo guiado por especificaciones) y que construyera la API de reservas de salas con Clean Architecture (CA), separando comandos y consultas (Command Query Responsibility Segregation — CQRS), y con autenticación JWT.
- **Qué acepté:** Acepté su propuesta de ir avanzando requisito por requisito (RF), usar H2 con Flyway para las migraciones, y escribir los tests de integración con MockMvc haciendo un login real.
- **Qué corregí:** Cambié los nombres de las clases de test de `*IT` a `*Test`. También ajusté la autenticación en los tests: en lugar de depender solo de `@WithMockUser`, hago login por HTTP y uso el token Bearer en las peticiones, para que la prueba refleje mejor el flujo real.

## Tiempo

- **t0** (reloj de la prueba): **2026-11-25 22:55:49 -06:00** — ver `PRUEBA-INICIO.md` (este README no lo sustituye).
- **Duración orientativa al cerrar RF 006 (docs):** ~**2 h 52 min** (marca local ~2026-11-26 01:47 -06:00).
- Límite del enunciado: ≤ 3 horas.

## Repo

- https://github.com/engelsrc/meeting-rooms-api-pt
