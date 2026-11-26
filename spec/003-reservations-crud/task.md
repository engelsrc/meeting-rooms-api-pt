# Tareas — 003 reservations-crud

GitHub Issue: #4 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/4 (open)  
Rama: `feature/003-reservations-crud`  
Auth create/cancel y `responsible` en body diferidos a 006; `responsible`/`created_by`/`cancelled_by` = `system`.  
Zona fija: `America/Mexico_City`.

## Tareas

- [x] **DB-1** — Add reservations Flyway migration (RF-1)  
  **Título (EN):** `[reservations] Add V3 reservations table`  
  **Hecho cuando:** existe `V3__create_reservations.sql` con `room_id` FK a `rooms`, `responsible`, `start_at`, `end_at`, `status`, auditoría RF 001, `cancelled_by`/`cancelled_at`; app arranca y Flyway aplica V3 sin romper V1/V2.  
  **Hecho:** 2026-11-26 — `V3__create_reservations.sql`; Flyway v3 OK al arrancar.

- [ ] **MODEL-1** — Domain Reservation and ports (RF-1)  
  **Título (EN):** `[reservations] Domain model and ports`  
  **Hecho cuando:** existen `Reservation`, `ReservationStatus` (ACTIVE/CANCELLED), `ReservationCommandPort` y `ReservationQueryPort` (save/find/overlap/lockRoom/list); sin lógica HTTP.

- [ ] **API-1** — ErrorMessages and NotFoundException (RF-2, RF-3, RF-4)  
  **Título (EN):** `[reservations] Error messages and NotFound`  
  **Hecho cuando:** `ErrorMessages` incluye constantes EN para intervalo inválido, solape, sala/reserva no encontrada, ya cancelada, date inválida, roomId list inválido, page/size (incl. size>100); existe `NotFoundException`; `RestExceptionHandler` mapea NotFound → **404**.

- [ ] **API-2** — JPA adapter with overlap and room lock (RF-1, RF-2, RF-3, RF-4)  
  **Título (EN):** `[reservations] JPA adapter lock and overlap`  
  **Hecho cuando:** entity/repo/adapter persisten reservas; detectan solape ACTIVE `[start,end)`; lock pesimista de sala en create; list orden `startAt ASC, id ASC` con filtros opcionales.

- [ ] **API-3** — CreateReservation command (RF-2)  
  **Título (EN):** `[reservations] Create reservation command`  
  **Hecho cuando:** handler valida `endAt > startAt` (400), sala inexistente/soft-deleted (404), solape ACTIVE (409), persiste ACTIVE con `responsible='system'`, `created_by='system'`; vía command (CQRS).

- [ ] **API-4** — CancelReservation command (RF-4)  
  **Título (EN):** `[reservations] Cancel reservation command`  
  **Hecho cuando:** handler con `reservationId`: inexistente → 404; ya CANCELLED → 409; ACTIVE → CANCELLED + `cancelled_by='system'` + `cancelled_at`; no muta soft-delete.

- [ ] **API-5** — ListReservations query (RF-3)  
  **Título (EN):** `[reservations] List reservations query`  
  **Hecho cuando:** query aplica `roomId`/`date` (día `startAt` en `America/Mexico_City`)/`page`/`size` (defaults 0/20); inválidos y `size>100` → 400; `roomId` sala inexistente → 400; resultado paginado ordenado.

- [ ] **API-6** — REST create list cancel (RF-2, RF-3, RF-4)  
  **Título (EN):** `[reservations] REST create list cancel`  
  **Hecho cuando:**  
  - `POST /api/reservations` body `{ roomId, startAt, endAt }` → **201** `{ id, roomId, responsible, startAt, endAt, status }`; auth **no**; 400/404/409 según RF.  
  - `GET /api/reservations?roomId=&date=&page=&size=` → **200** `{ items, page, size, total }`; auth **no**.  
  - `POST /api/reservations/cancel` body `{ reservationId }` → **200** DTO; auth **no**; 404/409 según RF.

- [ ] **API-7** — Smoke verify reservations flow (RF-2, RF-3, RF-4)  
  **Título (EN):** `[reservations] Smoke create list cancel overlap`  
  **Hecho cuando:** evidenciado (curl/log): create 201; solape secuencial 409; extremo a extremo OK; list filtros/paginación; cancel 200 luego 409; create tras cancel en mismo hueco 201.
