# Tareas — 007 extra-api-doc

GitHub Issue: #8 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/8 (open)  
Rama: `feature/007-extra-api-doc`  
Extra OpenAPI/Swagger delgado; sin tests nuevos obligatorios; docs en `local` y `prod`.

## Tareas

- [x] **DOCS-1** — Springdoc dependency (RF-1)  
  **Título (EN):** `[docs] Add springdoc OpenAPI webmvc UI`  
  **Hecho cuando:** `pom.xml` incluye `springdoc-openapi-starter-webmvc-ui` con versión compatible Spring Boot 3.3.5 (p. ej. 2.6.x); el proyecto resuelve la dependencia (`mvn -q dependency:resolve` o arranque OK).  
  **Hecho:** 2026-11-26 — `springdoc.version=2.6.0`; `dependency:resolve` OK.

- [x] **SEC-1** — Public doc routes in Security (RF-2)  
  **Título (EN):** `[security] PermitAll swagger and api-docs`  
  **Hecho cuando:** `SecurityConfig` declara `permitAll` para `/swagger-ui/**`, `/swagger-ui/index.html` (si aplica) y `/v3/api-docs` (y `/v3/api-docs/**` si hace falta); mutaciones `/api/**` siguen con JWT; GET `/api/**` y `POST /api/auth/login` siguen públicos. Auth docs: no. Auth mutaciones: sí.  
  **Hecho:** 2026-11-26 — `permitAll` explícito para swagger-ui y v3/api-docs; reglas `/api/**` intactas.

- [ ] **API-1** — OpenAPI Bearer JWT scheme (RF-2)  
  **Título (EN):** `[docs] OpenAPI bearer JWT security scheme`  
  **Hecho cuando:** existe bean `OpenAPI` (p. ej. `OpenApiConfig`) con esquema HTTP Bearer JWT usable en Swagger UI («Authorize»); sin flujo OAuth2/password; sin documentar flujo login→token en código de este RF.

- [ ] **API-2** — Minimal operation coverage and error codes (RF-3, RF-5)  
  **Título (EN):** `[docs] Annotate controllers error responses`  
  **Hecho cuando:** en Swagger UI / `/v3/api-docs` aparecen al menos: `POST /api/auth/login`, `POST|GET /api/rooms`, `POST|GET /api/reservations`, `POST /api/reservations/cancel`; documentados de forma mínima códigos **400**, **401** y/o **403**, **409** donde aplique; sin ejemplos elaborados de body; sin operaciones inventadas.

- [ ] **DOCS-2** — README Swagger UI URL (RF-4)  
  **Título (EN):** `[docs] README link to Swagger UI`  
  **Hecho cuando:** `README.md` menciona `http://localhost:8080/swagger-ui/index.html`; no exige `/v3/api-docs`; no reescribe secciones obligatorias §7 de `006` (cómo ejecutar, decisiones, IA, tiempo).

- [ ] **DOCS-3** — Smoke docs on local/prod profiles (RF-1, RF-2)  
  **Título (EN):** `[docs] Smoke swagger-ui and api-docs`  
  **Hecho cuando:** con la app arrancada, `GET http://localhost:8080/swagger-ui/index.html` → **200** (auth no); `GET http://localhost:8080/v3/api-docs` → **200** (auth no); no hay deshabilitación de springdoc en `application-prod.yml`; evidencia breve (curl/log) en el cierre de la tarea.
