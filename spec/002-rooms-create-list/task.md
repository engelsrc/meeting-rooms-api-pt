# Tareas — 002 rooms-create-list

GitHub Issue: #3 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/3 (open)  
Rama: `feature/002-rooms-create-list`  
Auth POST diferida a 006; `created_by=system`.

## Tareas

- [x] **DB-1** — Add web dependency and rooms Flyway migration (RF-1)  
  **Título (EN):** `[rooms] Add web dep and V2 rooms migration`  
  **Hecho cuando:** `pom.xml` incluye `spring-boot-starter-web` (y validation si aplica); existe `V2__create_rooms.sql` con `rooms` (`name` UNIQUE, `capacity`, `location`, columnas auditoría RF 001); sin romper V1.  
  **Hecho:** 2026-11-25 — web+validation; `V2__create_rooms.sql`.

- [x] **MODEL-1** — Domain Room and ports (RF-1)  
  **Título (EN):** `[rooms] Add Room domain and ports`  
  **Hecho cuando:** existe entidad de dominio `Room` y puertos de escritura/lectura bajo `domain`; sin dependencias de Spring/JPA en `domain`.  
  **Hecho:** 2026-11-25 — `Room`, `RoomCommandPort`, `RoomQueryPort`.

- [ ] **API-1** — ErrorMessages and app exceptions (RF-2, RF-3)  
  **Título (EN):** `[rooms] Add ErrorMessages and exceptions`  
  **Hecho cuando:** existe clase `ErrorMessages` (EN) con constantes de validación/409/paginación; excepciones de aplicación mapeables a 400/409; sin literales de mensaje en handlers futuros.

- [ ] **API-2** — CreateRoom command and handler (RF-2)  
  **Título (EN):** `[rooms] Implement CreateRoom command`  
  **Hecho cuando:** `CreateRoomCommand` + handler validan reglas (name, capacity, location, unicidad global); setean `created_by=system`; usan puertos; sin REST todavía o con wire mínimo si el adaptador ya existe — preferible command completo listo para entrypoint.

- [ ] **API-3** — ListRooms query and handler (RF-3)  
  **Título (EN):** `[rooms] Implement ListRooms query`  
  **Hecho cuando:** query/handler con `page`/`size` (default 0/20); rechaza inválidos; filtra `is_deleted=false`; devuelve items + total.

- [ ] **API-4** — JPA adapter for rooms (RF-1, RF-2, RF-3)  
  **Título (EN):** `[rooms] Implement JPA rooms adapter`  
  **Hecho cuando:** entity/repo/adapter JPA implementan los puertos; unicidad global vía UNIQUE o check; soft-delete respetado en list.

- [ ] **API-5** — REST POST and GET /api/rooms (RF-2, RF-3)  
  **Título (EN):** `[rooms] Expose POST GET api rooms`  
  **Hecho cuando:** `POST /api/rooms` → **201** + `{id,name,capacity,location}` (auth: no); `GET /api/rooms?page=&size=` → **200** `{items,page,size,total}` (auth: no); 400/409 vía advice + `ErrorMessages`; lista vacía 200.

- [ ] **API-6** — Smoke verify create and list (RF-2, RF-3)  
  **Título (EN):** `[rooms] Smoke verify create and list`  
  **Hecho cuando:** app arranca; evidencia de POST 201, GET 200 (vacío o con datos), al menos un 400 o 409 según smoke; documentado en la casilla.

## Notas
- Una Issue por RF; commits por tarea; sin merge/push hasta close.
