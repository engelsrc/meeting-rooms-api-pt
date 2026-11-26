# Plan — 003 reservations-crud

## Metadatos
- Carpeta: `spec/003-reservations-crud/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-26
- Rama: `feature/003-reservations-crud` (desde `dev`)
- Issue: #4 (open)
- Dependencia: RF 002 cerrado

## Enfoque y orden

1. **DB (RF-1):** Flyway `V3__create_reservations.sql` — tabla + FK `rooms` + auditoría + `cancelled_*`.
2. **Dominio + puertos (RF-1):** modelo `Reservation` / `ReservationStatus`; puertos command/query; reutilizar `RoomCommandPort`/`RoomQueryPort` (o puerto mínimo) para lock/existencia de sala.
3. **Persistencia (RF-1):** JPA entity/repo/adapter; consulta solape ACTIVE; `SELECT … FOR UPDATE` (o `LockModeType.PESSIMISTIC_WRITE`) sobre la fila `rooms` en create.
4. **Commands (RF-2, RF-4):** `CreateReservationHandler`, `CancelReservationHandler` + excepciones 400/404/409.
5. **Query (RF-3):** `ListReservationsHandler` — filtros `roomId`/`date` (día en `America/Mexico_City`), paginación, orden `startAt ASC, id ASC`.
6. **REST (RF-2…RF-4):** controller + DTOs; ampliar `ErrorMessages` + `RestExceptionHandler` (p. ej. `NotFoundException` → 404).
7. Smoke manual create / list / cancel / solape (sin suite ≥3 — RF 007).

## Clean Architecture (CA) + CQRS delgado

| Capa | Rol en 003 |
|------|------------|
| `domain.model` | `Reservation`, `ReservationStatus` (ACTIVE/CANCELLED); puertos |
| `application.command` | Create + Cancel (handlers orquestan validación, lock, solape, persistencia) |
| `application.query` | List (filtros + page) |
| `infrastructure.adapters.persistence` | JPA reservations (+ lock room) |
| `infrastructure.entrypoints.rest` | HTTP; mapeo DTO ↔ command/query; advice |
| `shared.ErrorMessages` | Constantes EN |

Sin event bus. Soft-delete columnas en tabla **sin** uso operativo (cancel solo `status` + `cancelled_*`).

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF |
|---------|-----------------|-----|
| `db/migration/V3__create_reservations.sql` | DDL `reservations` | RF-1 |
| `domain/model/Reservation.java` (+ status enum) | Entidad de dominio | RF-1 |
| `domain/model/port/ReservationCommandPort.java` | save / findById / existsOverlap / lockRoom | RF-1–2, RF-4 |
| `domain/model/port/ReservationQueryPort.java` | list filtrado paginado | RF-3 |
| `…/persistence/ReservationJpaEntity.java` | Mapeo JPA + auditoría | RF-1 |
| `…/persistence/ReservationJpaRepository.java` | Spring Data + queries | RF-1–3 |
| `…/persistence/ReservationPersistenceAdapter.java` | Implementa puertos; lock sala | RF-1–4 |
| `application/command/CreateReservationCommand.java` + `Handler` | Create + solape | RF-2 |
| `application/command/CancelReservationCommand.java` + `Handler` | Cancel | RF-4 |
| `application/query/ListReservationsQuery.java` + `Handler` + result | List | RF-3 |
| `application/NotFoundException.java` (nuevo) | 404 | RF-2, RF-4 |
| `…/rest/ReservationController.java` | POST /, GET /, POST /cancel | RF-2–4 |
| `…/rest/CreateReservationRequest.java`, `CancelReservationRequest.java`, `ReservationResponse.java` | DTOs | RF-2–4 |
| `…/rest/ReservationMapper.java` | DTO ↔ dominio/result | RF-2–4 |
| `shared/ErrorMessages.java` | Nuevas constantes EN | RF-2–4 |
| `…/rest/RestExceptionHandler.java` | `NotFoundException` → 404; reutilizar Conflict/Validation | RF-2–4 |
| Reusar `PageResponse.java` | Envelope list | RF-3 |

Constantes de zona: p. ej. `ZoneId.of("America/Mexico_City")` en query/handler (o config mínima compartida).

## Algoritmos

### Create (RF-2)

```text
VALIDATE body: roomId, startAt, endAt present
IF endAt <= startAt → 400
BEGIN TX
  LOCK room row FOR UPDATE (by roomId)
  IF room missing OR room.is_deleted → 404
  IF exists ACTIVE reservation same room
       WHERE startAt < newEnd AND endAt > newStart   -- [start,end)
     → 409
  INSERT reservation:
    status=ACTIVE, responsible='system', created_by='system',
    cancelled_*=null, is_deleted=false
COMMIT
RETURN 201 DTO
```

### List (RF-3)

```text
page default 0, size default 20
IF page < 0 OR size < 1 OR size > 100 → 400
IF date present AND not yyyy-MM-dd → 400
IF roomId present AND room not found → 400
dayStart/dayEnd = date at America/Mexico_City [00:00, +1 day)
FILTER optional roomId; optional startAt in [dayStart, dayEnd) converted to Instant/Offset
ORDER BY start_at ASC, id ASC
RETURN PageResponse(items, page, size, total)
```

### Cancel (RF-4)

```text
PARSE body.reservationId; IF missing/unparseable/not found → 404
IF status == CANCELLED → 409
SET status=CANCELLED, cancelled_by='system', cancelled_at=now
  (NO touch is_deleted / deleted_*)
RETURN 200 DTO
```

## Mensajes de error (`ErrorMessages` EN)

Añadir (nombres orientativos; implementación fija el literal exacto):

- intervalo inválido (`endAt` ≤ `startAt`)
- solape / sala ocupada → 409
- sala no encontrada (create) → 404
- reserva no encontrada / id cancel inválido → 404
- reserva ya cancelada → 409
- `roomId` de list inexistente → 400
- `date` inválida → 400
- page/size (incl. `size > 100`) → 400 (ampliar o complementar `INVALID_PAGE_OR_SIZE`)

Reutilizar `ConflictException` / `ValidationException`; añadir `NotFoundException`.

## Perfiles `local` / `prod`

Sin cambio de YAML obligatorio: H2 mem + Flyway aplica V3 en ambos. Zona de negocio en código (no perfil).

## Estrategia de pruebas

Este RF: smoke manual / curl (create, solape 409, list filtros, cancel).  
≥3 tests (≥1 integración) → **007**.

## Fuera de alcance

Auth; `responsible` en body; GET by id; cancel path-id; soft-delete operativo; EXCLUDE gist; Spring `Page` envelope; cascada sala borrada; RF 007/008.

## Trazabilidad

| Id | Cubierto por |
|----|----------------|
| RF-1 | V3 + dominio + JPA |
| RF-2 | Create command + REST + lock/solape |
| RF-3 | List query + REST filtros/paginación |
| RF-4 | Cancel command + `POST …/cancel` |

## Próximo paso

`/ssd-cmd-task` → `task.md` + GitHub Issue **open**.
