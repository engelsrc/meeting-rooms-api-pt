# Tareas — 008 extra-postman-collection

GitHub Issue: #9 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/9 (open)  
Rama: `feature/008-extra-postman-collection`  
Extra DX: Postman Collection v2.1 (6× positivo+negativo) + README extras + smoke.

## Tareas

- [x] **DOCS-1** — Scaffold Postman collection file and variables (RF-1)  
  **Título (EN):** `[docs] Scaffold Postman collection JSON`  
  **Hecho cuando:** existe `docs/postman/Meeting-Rooms-API.postman_collection.json` (Collection v2.1) con variables de colección `baseUrl`=`http://localhost:8080`, `token`, `roomId`, `reservationId`; sin `.postman_environment.json`; carpetas Auth / Rooms / Reservations presentes (pueden estar vacías de requests hasta DOCS-2/3).  
  **Hecho:** 2026-11-26 — scaffold v2.1 + variables + carpetas Auth/Rooms/Reservations.

- [x] **DOCS-2** — Happy-path requests and capture scripts (RF-1, RF-2)  
  **Título (EN):** `[docs] Postman positive requests and scripts`  
  **Hecho cuando:** la collection incluye los 6 positivos del plan:  
  - `POST /api/auth/login` body `{username,password}` seed `admin`/`admin` → script guarda `accessToken`→`token` (auth **no**).  
  - `POST /api/rooms` Bearer `Authorization: Bearer {{token}}` body name/capacity/location → script `id`→`roomId` (auth **sí**).  
  - `GET /api/rooms?page=0&size=20` (auth **no**).  
  - `POST /api/reservations` Bearer + `roomId`/`startAt`/`endAt` → script `id`→`reservationId` (auth **sí**).  
  - `GET /api/reservations` con query `roomId`/`date`/`page`/`size` (auth **no**).  
  - `POST /api/reservations/cancel` Bearer + `{reservationId}` (auth **sí**).  
  Sin rutas inventadas (register/refresh/etc.).  
  **Hecho:** 2026-11-26 — P1–P6 + scripts token/roomId/reservationId.

- [ ] **DOCS-3** — Representative negative requests (RF-1)  
  **Título (EN):** `[docs] Postman negative requests per endpoint`  
  **Hecho cuando:** hay exactamente **1** negativo representativo por cada uno de los 6 endpoints, segúnados al plan:  
  - Login password incorrecto → **401** (auth **no**).  
  - `POST /api/rooms` sin Authorization → **403**.  
  - `GET /api/rooms` `page`/`size` inválidos → **400** (auth **no**).  
  - `POST /api/reservations` solape ACTIVE → **409** (auth **sí**; mismo intervalo que el positivo).  
  - `GET /api/reservations` `date=not-a-date` → **400** (auth **no**).  
  - `POST /api/reservations/cancel` id inexistente → **404** (auth **sí**).  
  Sin matriz amplia ni Environment aparte.

- [ ] **DOCS-4** — README extras (API docs + Postman path) (RF-3)  
  **Título (EN):** `[docs] README extras API docs and Postman`  
  **Hecho cuando:** `README.md` menciona (cambio mínimo): acciones **extras** con el tiempo restante de la prueba; incorporación de **documentación de la API**; **Postman Collection** con ruta `docs/postman/Meeting-Rooms-API.postman_collection.json`; no reescribe secciones obligatorias §7 de RF 006.

- [ ] **DOCS-5** — Smoke happy path and all negatives (RF-4)  
  **Título (EN):** `[docs] Smoke Postman happy and negatives`  
  **Hecho cuando:** con la app arrancada en localhost:8080, se ejecuta el orden del plan (P1→P2→P3→P4→**N4**→P5→P6→N1→N2→N3→N5→N6) obteniendo los HTTP esperados (2xx en feliz; 401/403/400/409/404 en negativos); evidencia breve (status codes) anotada al cerrar esta tarea / para review; sin Newman/CI.
