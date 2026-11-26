# Tareas — 005 minimum-tests

GitHub Issue: #6 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/6 (open)  
Rama: `feature/005-minimum-tests`  
3 clases `@SpringBootTest` + MockMvc + H2; login HTTP + Bearer; sin Testcontainers.

## Tareas

- [x] **TEST-1** — Auth test support helper (RF-1, RF-2)  
  **Título (EN):** `[tests] Login helper for MockMvc JWT`  
  **Hecho cuando:** existe helper que hace `POST /api/auth/login` con `admin`/`admin` vía MockMvc y devuelve `accessToken`; usable desde tests A/B.  
  **Hecho:** 2026-11-26 — `AuthTestSupport.loginAdmin`.

- [x] **TEST-2** — CreateRoomAuthIT (RF-1)  
  **Título (EN):** `[tests] Create room 201 with JWT`  
  **Hecho cuando:** clase `@SpringBootTest` + `@AutoConfigureMockMvc`: login → `POST /api/rooms` con Bearer y body válido → **201**; nombre de sala único; auth sí (JWT).  
  **Hecho:** 2026-11-26 — `CreateRoomAuthIT` pasa.

- [x] **TEST-3** — ReservationOverlapIT (RF-1)  
  **Título (EN):** `[tests] Reservation overlap returns 409`  
  **Hecho cuando:** clase distinta: login → create room → create reservation ACTIVE → segunda reservation solapada (mismo Bearer) → **409**; auth sí.  
  **Hecho:** 2026-11-26 — `ReservationOverlapIT` pasa.

- [x] **TEST-4** — ListRoomsPublicIT (RF-1, RF-2)  
  **Título (EN):** `[tests] List rooms 200 without auth`  
  **Hecho cuando:** clase distinta: `GET /api/rooms` sin Authorization → **200**; auth no.  
  **Hecho:** 2026-11-26 — `ListRoomsPublicIT` pasa.

- [x] **TEST-5** — Verify mvn test green (RF-1, RF-2)  
  **Título (EN):** `[tests] Run mvn test suite`  
  **Hecho cuando:** `mvn test` ejecuta las 3 clases y termina en éxito (exit 0); sin Testcontainers.  
  **Hecho:** 2026-11-26 — `mvn test` exit 0 (3 IT + helper).
