# Review 001 — 003 reservations-crud

**Veredicto:** cumple  
**Fecha:** 2026-11-26  
**Issue:** #4 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/4 — **open**  
**Alcance:** rama `feature/003-reservations-crud`; V3; CA command/query; REST create/list/cancel; smoke API-7 en `task.md`

## Por requisito

### RF-1 — Modelo y persistencia
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| Migración Flyway sin romper V1/V2 | sí | `V3__create_reservations.sql`; arranque Flyway v3 (DB-1) |
| FK a `rooms` | sí | `CONSTRAINT fk_reservations_room` |
| Columnas negocio | sí | `room_id`, `responsible`, `start_at`, `end_at`, `status` |
| Auditoría RF 001 | sí | created/updated/deleted + `is_deleted` default false |
| `cancelled_by` / `cancelled_at` | sí | columnas nullable en V3 |
| H2 + solape app + lock | sí | overlap query + `findByIdForUpdate` PESSIMISTIC_WRITE |

**Prueba del plan:** V3 + dominio + JPA → **pasa**

### RF-2 — Crear reserva
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| POST `/api/reservations` 201 + DTO | sí | `ReservationController` + smoke 201 |
| `responsible=system` sin body | sí | `CreateReservationHandler.SYSTEM_USER` |
| Sin auth / `created_by=system` | sí | sin security; handler |
| `endAt > startAt` o 400 | sí | handler + `ErrorMessages.RESERVATION_INTERVAL_INVALID` (código; sin curl 400 en smoke) |
| Sala inexistente/soft-deleted → 404 | sí | lockRoom + `isDeleted` → `NotFoundException` (código) |
| Solape ACTIVE → 409 | sí | smoke overlap 409 |
| Extremo a extremo OK | sí | smoke adjacent 201 |
| Create concurrente + lock → 409 | sí | `@Transactional` + `findByIdForUpdate` (código; sin smoke paralelo) |
| Inválidos → 400 ErrorMessages | sí | handler + constants |
| DTO sin auditoría | sí | `ReservationResponse` 6 campos |
| CQRS command | sí | `CreateReservationCommand` + handler |

**Prueba del plan:** Create + REST + lock/solape → **pasa**

### RF-3 — Listar
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| `roomId`/`date`/`page`/`size` | sí | `ListReservationsHandler` + GET; smoke list filtrado 200 |
| Defaults 0/20 | sí | handler |
| page/size inválidos o size>100 → 400 | sí | código (`INVALID_RESERVATION_PAGE_OR_SIZE`) |
| `date` mal formado → 400 | sí | código |
| `roomId` inexistente → 400 | sí | `roomExists` + `INVALID_LIST_ROOM_ID` (código) |
| Orden `startAt ASC, id ASC` | sí | adapter Sort |
| ACTIVE y CANCELLED | sí | query sin filtro status |
| GET query only | sí | `@RequestParam` |
| Items DTO mínimo | sí | mapper |
| Vacío 200 | sí | lógica PageResponse (sin curl vacío explícito) |
| Query CQRS sin auth | sí | `ListReservationsQuery` + handler |

**Prueba del plan:** List query + REST → **pasa**

### RF-4 — Cancelar
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| POST `/api/reservations/cancel` body `reservationId` | sí | controller + smoke 200 CANCELLED |
| Ausente/inexistente → 404 | sí | null → 404; find vacío → 404 (código; smoke no cubrió id malo) |
| Ya CANCELLED → 409 | sí | smoke cancel again 409 |
| Sin auth; `cancelled_by=system` | sí | handler |
| Sin soft-delete | sí | solo status + cancelled_* |
| Tras cancel no cuenta solape | sí | smoke recreate mismo hueco 201 |
| CQRS command | sí | `CancelReservationCommand` + handler |

**Prueba del plan:** Cancel + REST → **pasa**

## Contraste con constitución
- CA + CQRS delgado: **OK** (commands create/cancel, query list, ports/adapters)
- `ErrorMessages` EN: **OK**
- Perfiles `local`/`prod`: heredados RF 001 — **OK**
- Auth mutaciones: deuda explícita → 006 — **OK** según RF (igual patrón 002)
- `responsible` fijo system: deuda §1 documentada — **OK** según RF
- Issue #4 **open**: **OK**
- ≥3 tests formales: **N/A** (RF 007)

## Fuera de alcance (correcto)
Auth, responsible en body, GET by id, cancel path-id, EXCLUDE gist, soft-delete operativo, suite tests, README.

## Huecos
- Smoke no cubrió todos los 400/404 de borde (intervalo inválido, sala 404 create, page/size, date, roomId list, cancel id inválido) — quedan en **código**.
- Concurrencia real de dos creates no se midió en runtime; solo lock en código + solape secuencial.

Ninguno material frente al RF aprobado.

## Veredicto
**cumple**
