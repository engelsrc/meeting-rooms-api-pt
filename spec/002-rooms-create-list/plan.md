# Plan — 002 rooms-create-list

## Metadatos
- Carpeta: `spec/002-rooms-create-list/`
- RF origen: `rf.md` (`Estado: aprobada`; change unique→409)
- Fecha: 2026-11-26
- Rama: `feature/002-rooms-create-list`
- Issue: #3 (open)

## Enfoque y orden

RF-1…RF-3 ya implementados en la rama. Pendiente del change:

1. En `RestExceptionHandler` (o capa de persistencia): mapear `DataIntegrityViolationException` por UNIQUE `rooms.name` → **409** + `ErrorMessages.ROOM_NAME_ALREADY_EXISTS`.
2. Smoke rápido: duplicado sigue 409 (check previo); el mapeo cubre carrera/UNIQUE.

Resto del plan original (V2, command/query, REST) se mantiene como ya hecho.

## Clean Architecture (CA) + CQRS delgado

Sin cambio de capas. El mapeo de violación de restricción vive en **entrypoint** (advice) o adapter; no en dominio.

## Archivos a modificar

| Archivo | Cambio | RF |
|---------|--------|-----|
| `…/entrypoints/rest/RestExceptionHandler.java` | `@ExceptionHandler(DataIntegrityViolationException)` → 409 | RF-2 |
| `task.md` | Tarea API-7 | RF-2 |

## Algoritmo

```text
ON DataIntegrityViolationException:
  IF causa/mensaje sugiere unique rooms.name (o siempre en create rooms context):
    RETURN 409 { message: ErrorMessages.ROOM_NAME_ALREADY_EXISTS }
  ELSE:
    rethrow / 500 genérico (opcional: solo rooms en este RF → siempre 409 para unique name)
```

En esta prueba: mapear **toda** `DataIntegrityViolationException` en la API de rooms a 409 con ese mensaje (única UNIQUE de negocio actual).

## Mensajes de error

Reutilizar `ErrorMessages.ROOM_NAME_ALREADY_EXISTS`.

## Pruebas

Smoke: POST duplicado → 409 (ya cubierto por existsByName; el handler cubre path UNIQUE).

## Trazabilidad

| Id | Cubierto por |
|----|----------------|
| RF-1 | Hecho (V2 + JPA) |
| RF-2 | Hecho + mapeo DataIntegrityViolation |
| RF-3 | Hecho |

## Próximo paso

Implementar API-7 en la rama del RF.
