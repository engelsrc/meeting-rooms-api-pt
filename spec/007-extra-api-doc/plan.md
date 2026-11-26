# Plan — 007 extra-api-doc

## Metadatos
- Carpeta: `spec/007-extra-api-doc/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-26
- Rama prevista: `feature/007-extra-api-doc` (desde `dev`)
- Issue: #8 (open)
- Dependencia: README raíz ya existe (`006`); API + auth en `dev`

## Enfoque y orden

1. **Dependencia (RF-1):** añadir `springdoc-openapi-starter-webmvc-ui` (línea 2.6.x) en `pom.xml` + propiedad de versión.
2. **Seguridad docs (RF-2):** explicitar `permitAll` para `/swagger-ui/**`, `/swagger-ui/index.html`, `/v3/api-docs` (y redirecciones springdoc si hace falta); no tocar la semántica de mutaciones `/api/**`.
3. **OpenAPI Bearer (RF-2):** bean `OpenAPI` con esquema HTTP Bearer JWT (sin flujo OAuth2/password).
4. **Cobertura + errores mínimos (RF-3, RF-5):** auto-descubrimiento de controllers; anotaciones **mínimas** (`@Tag` opcional; `@ApiResponse` / `@Operation` solo para declarar 400, 401/403, 409 donde aplique). Sin ejemplos elaborados de body.
5. **README (RF-4):** añadir URL canónica `http://localhost:8080/swagger-ui/index.html`; no exigir `/v3/api-docs`; no reescribir secciones §7 de `006`.
6. **Smoke manual:** arrancar `local` (y comprobar que `prod` no deshabilita springdoc); abrir UI y `/v3/api-docs`.

## Clean Architecture (CA) + CQRS delgado

| Capa | Rol en 007 |
|------|------------|
| `domain` / `application` | **Sin cambios** (ni commands ni queries nuevos) |
| `infrastructure.config` | Bean OpenAPI (Bearer); ajuste mínimo `SecurityConfig` para rutas docs |
| `infrastructure.entrypoints.rest` | Anotaciones OpenAPI mínimas en controllers existentes |
| `infrastructure.security` | Solo si el filtro JWT bloquea docs: ampliar `shouldNotFilter` para paths de springdoc (GET ya suele bastar) |
| README / `pom.xml` | Documentación y dependencia |

Sin event bus. Sin nuevos puertos ni handlers. Documentación = adaptador de entrada / config, no dominio.

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF |
|---------|-----------------|-----|
| `pom.xml` | Dep `springdoc-openapi-starter-webmvc-ui` + `springdoc.version` (~2.6.0) | RF-1 |
| `…/config/OpenApiConfig.java` (nuevo) | Bean `OpenAPI`: info breve + `SecurityScheme` Bearer JWT | RF-2 |
| `…/config/SecurityConfig.java` | `requestMatchers` `permitAll` para `/swagger-ui/**`, `/v3/api-docs` (+ path index si hace falta) | RF-2 |
| `…/security/JwtAuthenticationFilter.java` | Solo si hace falta excluir paths docs no-GET; preferir no tocar si GET + Security bastan | RF-2 |
| `…/rest/AuthController.java` | Tags/respuestas mínimas (p. ej. 400, 401) | RF-3, RF-5 |
| `…/rest/RoomController.java` | Tags/respuestas mínimas (400, 401/403 en POST, 409 name si aplica) | RF-3, RF-5 |
| `…/rest/ReservationController.java` | Tags/respuestas mínimas (400, 401/403, 409 solape en create) | RF-3, RF-5 |
| `README.md` | Mencionar `http://localhost:8080/swagger-ui/index.html`; opcional alinear ítem «OpenAPI» de «más tiempo» (ya no pendiente) | RF-4 |

**No modificar** (salvo necesidad demostrada de seguridad docs): handlers, dominio, migraciones, `ErrorMessages` (sin literales nuevos de este RF), perfiles YAML para deshabilitar UI en `prod`.

## Algoritmos / reglas

### Arranque docs (RF-1)

```text
ADD dependency springdoc-openapi-starter-webmvc-ui (Boot 3.3 compatible)
ON application start (profile local OR prod):
  GET /swagger-ui/index.html → 200 (UI)
  GET /v3/api-docs → 200 (OpenAPI JSON)
NO springdoc disable in application-prod.yml
```

### Seguridad docs vs API (RF-2)

```text
SecurityFilterChain:
  permitAll: /swagger-ui/**, /v3/api-docs[, /v3/api-docs/**]
  KEEP: GET /api/** permitAll
  KEEP: POST /api/auth/login permitAll
  KEEP: other /api/** authenticated
OpenAPI bean:
  components.securitySchemes["bearer-jwt"] = http, scheme=bearer, bearerFormat=JWT
  (optional) security requirement global for documented mutations — without changing runtime auth
NO OAuth2 password flow in OpenAPI
```

### Cobertura endpoints (RF-3)

```text
Controllers already mapped:
  POST /api/auth/login
  POST /api/rooms, GET /api/rooms
  POST /api/reservations, GET /api/reservations, POST /api/reservations/cancel
springdoc scans @RestController → operations appear in UI
IF gaps: add @Tag / @Operation name only — no fake operations
```

### Códigos de error mínimos (RF-5)

```text
FOR each relevant operation (or shared @ApiResponses):
  document status 400
  document status 401 and/or 403 (mutations / auth failures)
  document status 409 where business conflict applies (overlap, unique name if already returned)
NO response body examples per status
Runtime behavior unchanged (RestExceptionHandler / Security remain source of truth)
```

### README (RF-4)

```text
EDIT README.md:
  ADD line/section pointing to http://localhost:8080/swagger-ui/index.html
  DO NOT require /v3/api-docs mention
  DO NOT rewrite how-to-run / decisions / IA / time from 006
```

## Mensajes de error (`ErrorMessages` EN)

- **Sin literales nuevos** en este RF.
- OpenAPI solo **referencia códigos HTTP**; los textos de error siguen saliendo de `ErrorMessages` en runtime.
- No inventar mensajes distintos en anotaciones `@ApiResponse` (descripción breve en inglés OK, sin duplicar contratos de negocio).

## Perfiles `local` / `prod`

- Docs **habilitadas en ambos** (RF aprobado).
- No añadir `springdoc.api-docs.enabled: false` ni ocultar UI en `prod`.
- No hace falta propiedad nueva en YAML salvo que springdoc lo exija para path canónico (preferir defaults que den `/swagger-ui/index.html` y `/v3/api-docs`).

## Estrategia de pruebas

- Este RF **no** exige nuevos tests automatizados (extra de docs).
- Verificación: smoke manual / curl a UI y `/v3/api-docs` tras arranque; mutación `/api/**` sin token sigue 403/401 como hoy.
- No ampliar suite MockMvc salvo regresión clara de Security al abrir paths docs.

## Fuera de alcance

- OpenAPI enterprise, codegen, export CI de `openapi.json`.
- Automatizar login → token en Swagger (OAuth2/password).
- Mencionar `/v3/api-docs` en el README.
- Nuevos endpoints o cambios de contrato HTTP de negocio.
- Springfox, Docker Compose, métricas.
- Deshabilitar Swagger solo en `prod`.
- Event bus / capas CQRS nuevas.

## Trazabilidad RF → plan

| RF | Cubierto en |
|----|-------------|
| RF-1 | Dep Maven + smoke UI `/swagger-ui/index.html` + `/v3/api-docs` en `local`/`prod` |
| RF-2 | `SecurityConfig` (+ filtro si hace falta) + bean Bearer; sin flujo login documentado |
| RF-3 | Controllers existentes + anotaciones mínimas opcionales |
| RF-4 | `README.md` URL canónica UI |
| RF-5 | `@ApiResponse` mínimos 400 / 401\|403 / 409 |

## Siguiente paso

`Próxima acción: CREAR LAS TAREAS, ejecutar /ssd-cmd-task spec/007-extra-api-doc/rf.md`
