# Review 001 — 002 rooms-create-list

**Veredicto:** cumple  
**Fecha:** 2026-11-26  
**Issue:** #3 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/3 — **open**  
**Alcance:** rama `feature/002-rooms-create-list`; V2 rooms; CA command/query; REST; mapeo `DataIntegrityViolation` → 409; smoke en `task.md` API-6/API-7

## Por requisito

### RF-1 — Modelo y persistencia
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| Migración Flyway `rooms` sin romper V1 | sí | `V2__create_rooms.sql`; V1 `schema_meta` intacto |
| Columnas negocio | sí | `name`, `capacity`, `location` |
| Columnas auditoría | sí | created/updated/deleted + `is_deleted` |
| UNIQUE name global | sí | `CONSTRAINT uq_rooms_name UNIQUE (name)` |

**Prueba del plan:** V2 + JPA → **pasa**

### RF-2 — Crear sala
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| POST `/api/rooms` 201 + DTO mínimo | sí | `RoomController` + `RoomResponse`; smoke 201 |
| Sin auth | sí | sin security filter; RF deuda 006 |
| `created_by=system` | sí | `CreateRoomHandler.SYSTEM_USER` |
| location null / `""` / valor | sí | handler no normaliza `""` a null |
| 400 validación ErrorMessages | sí | handler + smoke name vacío 400 |
| 409 duplicado (check) | sí | `existsByName` + smoke 409 |
| DataIntegrityViolation → 409 | sí | `RestExceptionHandler.handleDataIntegrity` |
| CQRS command | sí | `CreateRoomCommand` + handler |

**Prueba del plan:** CreateRoom + REST + mapeo unique → **pasa**

### RF-3 — Listar paginado
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| page/size default 0/20 | sí | `ListRoomsHandler` |
| inválidos → 400 | sí | `page < 0 \|\| size < 1` |
| GET query, no headers/body | sí | `@RequestParam` |
| solo no borradas | sí | `findByDeletedFalse` |
| items DTO mínimo | sí | `RoomMapper` |
| vacío 200 | sí | smoke GET `items:[]` `total:0` |
| query CQRS | sí | `ListRoomsQuery` + handler |

**Prueba del plan:** ListRooms + GET → **pasa**

## Contraste con constitución
- CA + CQRS delgado: **OK**
- `ErrorMessages` EN: **OK**
- Auth mutación: deuda explícita a 006 — **OK** según RF
- Issue #3 **open**: **OK**
- ≥3 tests formales: **N/A** (RF 007)

## Fuera de alcance (correcto)
Auth, PUT/DELETE, reservas, Page Spring al cliente, PostgreSQL.

## Huecos
Ninguno material respecto al RF aprobado (incluido mapeo concurrente UNIQUE → 409).

## Veredicto
**cumple**
