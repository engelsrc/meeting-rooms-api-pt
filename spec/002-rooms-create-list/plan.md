# Plan — 002 rooms-create-list

## Metadatos
- Carpeta: `spec/002-rooms-create-list/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-25
- Rama prevista: `feature/002-rooms-create-list`
- Base: RF 001 cerrado (`dev` con Spring Boot + H2 + Flyway + paquetes CA)

## Enfoque y orden

1. Dependencia web: `spring-boot-starter-web` (+ validation si hace falta) (**RF-2/3**).
2. Migración `V2__create_rooms.sql` — tabla `rooms` + auditoría + `UNIQUE(name)` (**RF-1**).
3. Dominio `Room` + puerto(s) de escritura/lectura (**RF-1**, CA).
4. `ErrorMessages` + excepciones de dominio/aplicación (400/409) (**RF-2/3**).
5. Command `CreateRoom` + handler (**RF-2**).
6. Query `ListRooms` + handler (paginación propia) (**RF-3**).
7. Adapter JPA (entity/repo) implementando puertos (**RF-1/2/3**).
8. REST: DTOs, mapper, controller `POST/GET /api/rooms`, advice de errores (**RF-2/3**).
9. Verificar arranque + curl/Manual smoke create/list (**RF-2/3**).

## Clean Architecture (CA) + CQRS delgado

```text
domain.model          → Room (+ reglas mínimas si aplica)
domain.model...port   → RoomCommandPort / RoomQueryPort (nombres exactos en implement)
application.command   → CreateRoomCommand + CreateRoomHandler
application.query     → ListRoomsQuery + ListRoomsHandler
infrastructure.adapters     → JPA entity/repo/adapter
infrastructure.entrypoints  → RoomController + DTOs + ExceptionHandler
infrastructure.config       → (si hace falta); YAML ya en 001
```

- POST → command; GET → query.
- Sin event bus. DTO ↔ command/query/dominio en entrypoint/adapter.

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF |
|---------|-----------------|-----|
| `pom.xml` | Añadir `spring-boot-starter-web` (+ `validation`) | RF-2, RF-3 |
| `db/migration/V2__create_rooms.sql` | Tabla `rooms` + UNIQUE name + auditoría | RF-1 |
| `…/domain/model/Room.java` | Entidad de dominio | RF-1 |
| `…/domain/model/port/…` | Puertos escritura/lectura | RF-1–3 |
| `…/common/ErrorMessages.java` (o `…/shared`) | Constantes EN | RF-2, RF-3 |
| `…/application/command/CreateRoom*` | Alta | RF-2 |
| `…/application/query/ListRooms*` | Listado paginado | RF-3 |
| `…/infrastructure/adapters/…` | JPA | RF-1–3 |
| `…/infrastructure/entrypoints/…` | REST + DTO + handler errores | RF-2, RF-3 |

## Algoritmo / pseudocódigo

```text
POST /api/rooms:
  VALIDATE name not blank, length <= 100
  VALIDATE capacity > 0
  VALIDATE location == null OR length <= 255  // "" permitido
  IF existsByName(name): THROW conflict → 409 ErrorMessages.ROOM_NAME_ALREADY_EXISTS
  room = new Room(name, capacity, location)
  persist with created_by='system', created_at=now, is_deleted=false
  RETURN 201 { id, name, capacity, location }

GET /api/rooms?page&size:
  page = default 0 if missing; size = default 20 if missing
  IF page < 0 OR size < 1: THROW 400 ErrorMessages.INVALID_PAGE_OR_SIZE
  pageResult = findByIsDeletedFalse(page, size)  // orden estable p.ej. id ASC
  RETURN 200 { items: map(dto), page, size, total }

V2 SQL rooms:
  id, name VARCHAR(100) NOT NULL UNIQUE, capacity INT NOT NULL,
  location VARCHAR(255) NULL,
  created_by, created_at, updated_by, updated_at,
  is_deleted BOOLEAN NOT NULL DEFAULT FALSE, deleted_by, deleted_at
```

## Mensajes de error (`ErrorMessages`) — inglés

Ejemplos (nombres finales en implement):

| Constante | Uso |
|-----------|-----|
| `ROOM_NAME_REQUIRED` / `ROOM_NAME_TOO_LONG` | 400 name |
| `ROOM_CAPACITY_INVALID` | 400 capacity ≤ 0 |
| `ROOM_LOCATION_TOO_LONG` | 400 location > 255 |
| `ROOM_NAME_ALREADY_EXISTS` | 409 |
| `INVALID_PAGE_OR_SIZE` | 400 paginación |
| `VALIDATION_FAILED` | genérico si aplica |

Sin literales quemados en controllers/handlers.

## Perfiles `local` / `prod`

Sin cambio de datasource (H2 mem de RF 001). Flyway aplicará V2 al arrancar.

## Estrategia de pruebas

No es el RF de ≥3 tests. Smoke: arranque + POST 201 + GET 200 (vacío y con datos) + 409 duplicado + 400 validación. Tests formales → RF 007.

## Fuera de alcance

- Auth POST, update/delete salas, reservas, filtros fecha, PostgreSQL, Page de Spring en JSON, README, suite mínima enunciado.

## Trazabilidad RF → plan

| Id | Cubierto por |
|----|----------------|
| RF-1 | V2 + dominio + JPA + UNIQUE name |
| RF-2 | CreateRoom command + POST + ErrorMessages 400/409 |
| RF-3 | ListRooms query + GET paginado `{ items, page, size, total }` |

## Próximo paso

`/ssd-cmd-task` → `task.md` + GitHub Issue **open**.
