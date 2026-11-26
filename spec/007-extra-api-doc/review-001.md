# Review 001 — 007 extra-api-doc

Fecha: 2026-11-26  
Rama revisada: `feature/007-extra-api-doc`  
Issue: #8 (open) — https://github.com/engelsrc/meeting-rooms-api-pt/issues/8  

## Veredicto

**cumple**

## Resumen por RF

| RF | Prueba del plan / tarea | Resultado |
|----|-------------------------|-----------|
| RF-1 | DOCS-1 + DOCS-3 | cumple |
| RF-2 | SEC-1 + API-1 + DOCS-3 | cumple |
| RF-3 | API-2 | cumple |
| RF-4 | DOCS-2 | cumple |
| RF-5 | API-2 | cumple |

## Criterios de aceptación

### RF-1 — Dependencia y arranque
- [x] `pom.xml` con `springdoc-openapi-starter-webmvc-ui` **2.6.0** (Boot 3.3.5) — evidencia: `pom.xml` + DOCS-1 (`dependency:resolve` OK).
- [x] Swagger UI en `/swagger-ui/index.html` — DOCS-3: `GET` perfil `prod` → **200**.
- [x] `/v3/api-docs` disponible — DOCS-3: `GET` → **200**; sin `springdoc.*.enabled=false` en YAML.

### RF-2 — Docs públicas; mutaciones JWT
- [x] `permitAll` explícito `/swagger-ui/**`, index, `/v3/api-docs/**` — `SecurityConfig`.
- [x] Mutaciones `/api/**` siguen `.authenticated()`; GET `/api/**` y login públicos — `SecurityConfig` intacto en reglas de API.
- [x] Esquema Bearer JWT — `OpenApiConfig` (`bearer-jwt`, HTTP bearer).
- [x] Sin flujo login→token automatizado en OpenAPI — no hay OAuth2/password.

### RF-3 — Cobertura endpoints
- [x] Controllers anotados Auth / Rooms / Reservations con rutas existentes (login, rooms CRUD list/create, reservations create/list/cancel) — API-2; smoke DOCS-3 confirmó path login en api-docs.
- [x] Sin operaciones inventadas — solo controllers previos.
- [x] Anotaciones mínimas (`@Tag`, `@Operation`, `@ApiResponses`) — cumplen “mínimas”.

### RF-4 — README
- [x] Menciona `http://localhost:8080/swagger-ui/index.html`.
- [x] No exige `/v3/api-docs` en README.
- [x] Secciones §7 de `006` conservadas; solo se alineó «más tiempo» quitando ítem OpenAPI ya entregado.

### RF-5 — Códigos de error mínimos
- [x] Documentados **400**, **401** (login) y/o **403** (mutaciones), **409** (create room/reservation) vía `@ApiResponse`.
- [x] Sin ejemplos elaborados de body por status.
- [x] Sin cambio de comportamiento HTTP de negocio (solo anotaciones + config docs).

## Constitución

- Extra de documentación de API permitido si sobra tiempo — aplica.
- Clean Architecture (CA) + CQRS delgado: OpenAPI en `infrastructure.config` / entrypoints; sin dominio/application nuevos; sin event bus.
- `ErrorMessages`: sin literales nuevos de error en este RF.
- Perfiles `local`/`prod`: docs no deshabilitadas; smoke en `prod`.
- Auth mutaciones / consultas públicas: no debilitadas.
- Tests ≥3: fuera del alcance de este RF (ya en `005`); no se exigieron nuevos.
- GitHub Issue **#8 open** anotada en `rf.md` / `task.md`.

## Fuera de alcance (y estado)

| Ítem excluido | ¿Hecho de más? |
|---------------|----------------|
| OpenAPI enterprise / ejemplos elaborados | No |
| OAuth2/password en UI | No |
| `/v3/api-docs` en README | No |
| Nuevos endpoints / cambio contrato | No |
| Springfox / Docker / métricas | No |
| Deshabilitar Swagger en `prod` | No (correcto: docs en todos los perfiles) |

Nota: se eliminó el ítem OpenAPI de «Qué haría con más tiempo» del README (alineación opcional del plan); no introduce alcance fuera del RF.

## Huecos

Ninguno que impida el veredicto **cumple**. Smoke explícito solo registrado en perfil `prod` (suficiente: RF-1 es `local` o `prod`; no hay disable en `prod`).

## Commits en la rama (vs `dev`)

- `f579698` docs(openapi): agregar dependencia springdoc 2.6.0  
- `68b4bc3` sec(openapi): permitir rutas swagger y api-docs  
- `c1dae12` docs(openapi): esquema Bearer JWT en OpenAPI  
- `e472932` docs(openapi): anotar controllers y codigos de error  
- `6556b51` docs(readme): enlace a Swagger UI  
- `ac3cc84` docs(openapi): smoke swagger-ui y api-docs OK  
