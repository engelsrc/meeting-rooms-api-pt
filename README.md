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

Documentación interactiva (Swagger UI): **`http://localhost:8080/swagger-ui/index.html`**

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

- **Stack:** Java 17+, Spring Boot 3.3, Maven; H2 + Flyway (migraciones V1–V4); JWT HS256 (no Basic).
- **Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado:** entrypoints REST → handlers de command/query → dominio → adapters JPA; un solo módulo; sin event bus ni microservicios (límite ~3 h).
- **Auth:** mutaciones (`POST` create/cancel) con Bearer; `GET` públicos; login `POST /api/auth/login`; seed `admin`/`admin` y `user`/`user`.
- **Reservas:** intervalo **`[start, end)`**; solape con otra ACTIVE en la misma sala → **409**; validaciones → **400**; mensajes en inglés vía `ErrorMessages`.
- **Listados:** salas y reservas paginados; reservas filtrables por `roomId` y `date`.
- **Perfiles:** `local` y `prod` con secretos **demo** en YAML (no secretos reales de producción en el repo).
- **Tests:** ≥3 integración MockMvc + H2 (`CreateRoomAuthTest`, `ReservationOverlapTest`, `ListRoomsPublicTest`); login HTTP real (no solo `@WithMockUser`).
- **Proceso:** SDD por RF (`spec/00N-…`), una rama `feature/<carpeta>`, una Issue GitHub por RF, merge a `dev` al close.

## Qué haría con más tiempo

1. **PostgreSQL real + Docker Compose** para `local`/`prod` (hoy ambos perfiles usan H2 demo).
2. **Más tests:** cancelación, filtros de reservas, unique name → 409, casos borde de solape.
3. **Hardening JWT:** secretos solo por env/secret manager; rotación; refresh tokens opcionales.
4. **Observabilidad mínima:** health/metrics y logging estructurado de conflictos de reserva.

## Cómo usé la IA

Hechos aportados por el autor de la prueba (formateados; sin ampliar):

- **Pedí:** scaffolding SDD + API de salas con Clean Architecture (CA) / Command Query Responsibility Segregation (CQRS) y JWT.
- **Acepté:** flujo por RF, H2 + Flyway, MockMvc con login real.
- **Corregí:** renombrar clases `*IT` → `*Test`; autenticación en tests con Bearer vía login HTTP en lugar de basarse solo en `@WithMockUser`.

## Tiempo

- **t0** (reloj de la prueba): **2026-11-25 22:55:49 -06:00** — ver `PRUEBA-INICIO.md` (este README no lo sustituye).
- **Duración orientativa al cerrar RF 006 (docs):** ~**2 h 52 min** (marca local ~2026-11-26 01:47 -06:00).
- Límite del enunciado: ≤ 3 horas.

## Repo

- https://github.com/engelsrc/meeting-rooms-api-pt
