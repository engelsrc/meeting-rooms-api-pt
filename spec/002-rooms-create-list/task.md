# Tareas — 002 rooms-create-list

GitHub Issue: #3 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/3 (open)  
Rama: `feature/002-rooms-create-list`  
Auth POST diferida a 006; `created_by=system`.

## Tareas

- [x] **DB-1** — Add web dependency and rooms Flyway migration (RF-1)  
  **Hecho:** V2 + web/validation.

- [x] **MODEL-1** — Domain Room and ports (RF-1)  
  **Hecho:** `Room` + puertos.

- [x] **API-1** — ErrorMessages and app exceptions (RF-2, RF-3)  
  **Hecho:** `ErrorMessages` + excepciones.

- [x] **API-2** — CreateRoom command and handler (RF-2)  
  **Hecho:** CreateRoom command/handler.

- [x] **API-3** — ListRooms query and handler (RF-3)  
  **Hecho:** ListRooms query/handler.

- [x] **API-4** — JPA adapter for rooms (RF-1, RF-2, RF-3)  
  **Hecho:** JPA adapter.

- [x] **API-5** — REST POST and GET /api/rooms (RF-2, RF-3)  
  **Hecho:** RoomController + advice básico.

- [x] **API-6** — Smoke verify create and list (RF-2, RF-3)  
  **Hecho:** 201/400/409/200 documentados.

- [x] **API-7** — Map unique violation to 409 (RF-2)  
  **Título (EN):** `[rooms] Map DataIntegrityViolation to 409`  
  **Hecho cuando:** `DataIntegrityViolationException` (UNIQUE `name`) se traduce a **409** + `ErrorMessages.ROOM_NAME_ALREADY_EXISTS` en el advice REST; no responde 500 por esa carrera.  
  **Hecho:** 2026-11-26 — `RestExceptionHandler` mapea `DataIntegrityViolationException` → 409.
