# Tareas — 004 auth-mutations

GitHub Issue: #5 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/5 (open)  
Rama: `feature/004-auth-mutations`  
JWT HS256 TTL 1h; users seed admin/user; GET públicos; mutaciones Bearer.

## Tareas

- [x] **DB-1** — Add security deps and users Flyway migration (RF-1)  
  **Título (EN):** `[auth] Add V4 users table and security deps`  
  **Hecho cuando:** `pom.xml` incluye `spring-boot-starter-security` + JWT (jjwt); existe `V4__create_users.sql` con `id`, `username` UNIQUE, `password` (BCrypt); seed `admin`/`admin` y `user`/`user`; app arranca y Flyway aplica V4.  
  **Hecho:** 2026-11-26 — security+jjwt en pom; V4 users seed; Flyway v4 OK.

- [x] **MODEL-1** — User domain and query port (RF-1)  
  **Título (EN):** `[auth] User domain and query port`  
  **Hecho cuando:** existen modelo User mínimo, `UserQueryPort.findByUsername` (case-sensitive), JPA entity/repo/adapter; sin HTTP.  
  **Hecho:** 2026-11-26 — User + UserQueryPort + JPA adapter.

- [x] **SEC-1** — JwtService and ErrorMessages auth (RF-2, RF-3)  
  **Título (EN):** `[auth] JwtService and ErrorMessages`  
  **Hecho cuando:** `JwtService` emite/valida HS256 (`sub`=username, TTL 1h, secret desde config); `ErrorMessages` tiene constantes EN para user not found, invalid password, unauthenticated 403, invalid token 401, login validation; YAML local/prod con `app.jwt.secret` y expiration 3600000.  
  **Hecho:** 2026-11-26 — JwtService + ErrorMessages AUTH_* + YAML.

- [x] **SEC-2** — Security filter chain and JWT filter (RF-3)  
  **Título (EN):** `[auth] SecurityConfig and Jwt filter`  
  **Hecho cuando:** GET `/api/**` y `POST /api/auth/login` permitAll; resto `/api/**` autenticado; sin header / Bearer vacío / scheme ≠ Bearer → **403**; JWT inválido/expirado → **401** con `ErrorMessages`; CSRF off; session STATELESS.  
  **Hecho:** 2026-11-26 — SecurityConfig + JwtAuthenticationFilter.

- [x] **API-1** — Login command and POST /api/auth/login (RF-2)  
  **Título (EN):** `[auth] Login endpoint`  
  **Hecho cuando:** `POST /api/auth/login` body `{ username, password }` → **200** `{ accessToken }` (auth **no**); blank/ausente → **400**; JSON malo → **400**; user inexistente → **401** mensaje distinto; password malo → **401** mensaje distinto; vía command/handler.  
  **Hecho:** 2026-11-26 — AuthController + LoginHandler.

- [x] **API-2** — Wire identity into room and reservation mutations (RF-4)  
  **Título (EN):** `[auth] Propagate username to audit fields`  
  **Hecho cuando:** create room usa `created_by`=username del SecurityContext; create reservation `created_by`+`responsible`=username; cancel `cancelled_by`=username; ya no `'system'` en mutaciones autenticadas.  
  **Hecho:** 2026-11-26 — actorUsername en commands/handlers.

- [ ] **API-3** — Smoke verify JWT auth flow (RF-2, RF-3, RF-4)  
  **Título (EN):** `[auth] Smoke login protect and identity`  
  **Hecho cuando:** evidenciado: login 200 + token; GET rooms/reservations sin token 200; POST mutación sin token 403; Bearer inválido 401; POST con token 201/200 y username en created_by/responsible/cancelled_by.
