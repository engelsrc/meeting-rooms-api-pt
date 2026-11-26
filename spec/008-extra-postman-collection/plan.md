# Plan — 008 extra-postman-collection

## Metadatos
- Carpeta: `spec/008-extra-postman-collection/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-26
- Rama prevista: `feature/008-extra-postman-collection` (desde `dev`)
- Issue: #9 (open)
- Dependencia: API + JWT en `dev`; README raíz existe (`006`); doc API / Swagger (`007`) referenciable desde README

## Enfoque y orden

1. **Scaffold collection (RF-1):** crear `docs/postman/` + `Meeting-Rooms-API.postman_collection.json` (v2.1) con variables `baseUrl`, `token`, `roomId`, `reservationId`.
2. **Requests positivos + scripts (RF-1, RF-2):** carpetas Auth / Rooms / Reservations; Login + creates con scripts; mutaciones con `Authorization: Bearer {{token}}`; listados con query params.
3. **Requests negativos (RF-1):** un negativo representativo por endpoint (códigos alineados a la API actual).
4. **README extras (RF-3):** mención mínima de extras (tiempo restante) + documentación de la API + ruta Postman.
5. **Smoke (RF-4):** app local → feliz completo → todos los negativos; registrar evidencia en review/close o notas de task.

Sin cambios de código de producto (controllers, handlers, seguridad, migraciones).

## Clean Architecture (CA) + CQRS delgado

| Capa | Rol en 008 |
|------|------------|
| `domain` / `application` | **Sin cambios** |
| `infrastructure.*` | **Sin cambios** |
| `docs/postman/*.json` | Artefacto DX (cliente HTTP); no es capa de aplicación |
| `README.md` | Documentación de extras |

Sin event bus, sin nuevos commands/queries/puertos. Este RF no toca Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) más allá de no romperlas.

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF |
|---------|-----------------|-----|
| `docs/postman/Meeting-Rooms-API.postman_collection.json` (nuevo) | Collection v2.1: 6×(positivo+negativo), variables, scripts | RF-1, RF-2, RF-4 |
| `README.md` | Sección/mención extras: doc API + Postman (ruta) | RF-3 |

**No modificar:** `pom.xml`, controllers, `SecurityConfig`, `ErrorMessages`, YAML de perfiles, código de tests de producto.

## Mapa de requests (RF-1, RF-2)

### Variables de colección

| Variable | Origen |
|----------|--------|
| `baseUrl` | Default `http://localhost:8080` |
| `token` | Script Login positivo ← `accessToken` |
| `roomId` | Script Create room positivo ← `id` |
| `reservationId` | Script Create reservation positivo ← `id` |

Sin `.postman_environment.json`.

### Positivos (flujo feliz)

| # | Request | Método / path | Notas |
|---|---------|---------------|--------|
| P1 | Login OK | `POST /api/auth/login` | body `admin`/`admin`; script → `token` |
| P2 | Create room | `POST /api/rooms` | Bearer; body name/capacity/location; script → `roomId` |
| P3 | List rooms | `GET /api/rooms?page=0&size=20` | público |
| P4 | Create reservation | `POST /api/reservations` | Bearer; `roomId` var; `startAt`/`endAt` OffsetDateTime; script → `reservationId` |
| P5 | List reservations | `GET /api/reservations?roomId={{roomId}}&date=…&page=0&size=20` | público; `date` `yyyy-MM-dd` |
| P6 | Cancel | `POST /api/reservations/cancel` | Bearer; `{ "reservationId": {{reservationId}} }` |

### Negativos representativos (1 por endpoint)

| # | Endpoint | Negativo | HTTP esperado |
|---|----------|----------|----------------|
| N1 | `POST /api/auth/login` | password incorrecto (user `admin`) | **401** |
| N2 | `POST /api/rooms` | sin header Authorization | **403** |
| N3 | `GET /api/rooms` | `page=-1` o `size=0` | **400** |
| N4 | `POST /api/reservations` | mismo horario solapado con ACTIVE (tras P4, **antes** de P6) | **409** |
| N5 | `GET /api/reservations` | `date=not-a-date` | **400** |
| N6 | `POST /api/reservations/cancel` | `reservationId` inexistente (p. ej. `999999`) | **404** |

Orden de smoke: ver sección Algoritmos (N4 depende de reserva ACTIVE).

## Algoritmos / reglas

### Scripts (RF-1)

```text
ON Login positive (200):
  json = response.json()
  collection.token = json.accessToken

ON Create room positive (201):
  collection.roomId = response.json().id

ON Create reservation positive (201):
  collection.reservationId = response.json().id
```

### Orden smoke feliz + negativos (RF-4)

```text
START app (local or prod demo H2)
RUN P1 → 200
RUN P2 → 201
RUN P3 → 200
RUN P4 → 201
RUN N4 (overlap same room/interval) → 409   // before cancel
RUN P5 → 200
RUN P6 → 200
RUN N1 → 401
RUN N2 → 403
RUN N3 → 400
RUN N5 → 400
RUN N6 → 404
RECORD status codes in review/close or task notes
NO Newman/CI required
```

### README extras (RF-3)

```text
ADD brief section or bullets under extras / más tiempo cumplido:
  - extras done with remaining test time
  - API documentation (Swagger UI already documented if 007 done; reference it)
  - Postman Collection path: docs/postman/Meeting-Rooms-API.postman_collection.json
MINIMAL edit; do not rewrite RF 006 sections
```

## Mensajes de error / ErrorMessages

- La collection **no** añade constantes ni literales nuevos.
- Los negativos esperan códigos HTTP ya emitidos por la API (`ErrorMessages` EN existentes).
- Bodies de error pueden ser `{ "message": "..." }` según handlers actuales; el smoke valida **status**, no el texto exacto (salvo que la evidencia lo cite).

## Perfiles `local` / `prod`

- La collection es agnóstica al perfil (`baseUrl` localhost).
- Smoke: arrancar con el perfil que use el repo para demo local (típicamente `local` o default H2); no crear YAML nuevo.

## Estrategia de pruebas

- **No** añade tests JUnit (eso es RF 005).
- Verificación = **smoke manual** Postman/curl equivalente (RF-4): feliz completo + los 6 negativos.
- Evidencia en `review-*.md` / `close.md` o notas de task al implementar.

## Fuera de alcance

- OpenAPI/Swagger implementación (RF 007).
- Environment Postman; Newman; CI; monitores cloud.
- Matriz amplia de negativos; register/refresh/logout/Basic/RBAC.
- Cambios de código de producto o H2 console como API.
- Event bus / nuevas capas CA+CQRS.

## Pendiente de aclarar

- Ninguno tras RF aprobada / AskQuestions.
