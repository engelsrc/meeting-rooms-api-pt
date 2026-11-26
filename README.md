# Meeting Rooms API

API REST para reservar salas de reuniones (prueba técnica).

Repositorio: https://github.com/engelsrc/meeting-rooms-api-pt

## Cómo ejecutarlo

### Requisitos

- Java **17+** (probado también con JDK más reciente)
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

Arranque:

```bash
mvn spring-boot:run
# o
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

URL base de los ejemplos: **`http://localhost:8080`**

Usuario seed (migración V4): `admin` / `admin` (también existe `user` / `user`).

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

_(Pendiente — DOCS-3)_

## Qué haría con más tiempo

_(Pendiente — DOCS-3)_

## Cómo usé la IA

_(Placeholder — DOCS-4: bullets del humano; el agente solo formatea.)_

## Tiempo

_(Placeholder — DOCS-5: t0 desde `PRUEBA-INICIO.md` + duración al cierre del RF 006.)_

## Repo

- https://github.com/engelsrc/meeting-rooms-api-pt
