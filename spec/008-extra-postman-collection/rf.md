# RF — Colección Postman de la API

Estado: aprobada

## Metadatos
- Carpeta: `spec/008-extra-postman-collection/`
- Slug: `extra-postman-collection`
- Orden: `008`
- Tipo: `Docs`
- Origen: change — AskQuestions (positivos+negativos, scripts token/IDs, README extras, smoke completo, ruta `docs/postman/…`)
- Fecha: 2026-11-26
- GitHub Issue: #9 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/9

## Problema / oportunidad

La API ya expone auth JWT, salas y reservas. El README (RF 006) documenta tabla y flujo curl mínimo; OpenAPI/Swagger (RF 007) cubre exploración en UI. Falta un artefacto **importable** en Postman con casos **positivos y negativos** por endpoint, y dejar constancia en el README de estos extras hechos con el tiempo restante de la prueba.

## Objetivo

Entregar una **Postman Collection v2.1** en `docs/postman/Meeting-Rooms-API.postman_collection.json` que cubra los 6 endpoints (1 positivo + 1 negativo representativo cada uno), con variables/scripts JWT e IDs, smoke documentado del feliz y de todos los negativos, y mención en el README junto a la documentación de API.

## Alcance

### Incluye
- Archivo **`docs/postman/Meeting-Rooms-API.postman_collection.json`** (Collection v2.1).
- Los **6** endpoints vigentes, cada uno con **1 request positivo** y **1 request negativo representativo**:
  - `POST /api/auth/login`
  - `POST /api/rooms`, `GET /api/rooms`
  - `POST /api/reservations`, `GET /api/reservations`, `POST /api/reservations/cancel`
- Variables de colección (sin Environment aparte): `baseUrl` = `http://localhost:8080`, `token`, `roomId`, `reservationId`.
- Script de test en Login positivo: guarda `accessToken` → `token`.
- Scripts en creates positivos: guardan `id` → `roomId` / `reservationId`.
- Mutaciones usan header exacto `Authorization: Bearer {{token}}` (sin “equivalente”).
- Bodies/query alineados a DTOs reales; list rooms con `page`/`size`; list reservations con `roomId`/`date`/`page`/`size` según el caso.
- Credenciales seed demo (`admin`/`admin`); sin secretos de producción.
- README raíz: sección/mención de **acciones extras** con el tiempo restante de la prueba, que incorpora **documentación de la API** y **Postman Collection** indicando la ruta `docs/postman/Meeting-Rooms-API.postman_collection.json` (cambio mínimo; no reabrir RF 006 completo).
- Smoke de cierre: flujo feliz **completo** + **todos** los requests negativos de la collection (evidencia en el cierre del RF / review).

### Excluye
- OpenAPI/Swagger, Newman/CI, mocks, monitores Postman cloud (OpenAPI es RF 007; este RF solo referencia “documentación de la API” en README).
- Environment Postman separado (`.postman_environment.json`).
- Matriz amplia de varios negativos por endpoint (solo 1 representativo).
- Nuevos endpoints o cambios de código de producto.
- Registro, refresh, logout, HTTP Basic, RBAC.
- Documentar H2 console como “API”.

## Actores
- Humano: aprueba RF; puede revisar smoke.
- Agente: genera JSON + scripts; actualiza README; ejecuta/documenta smoke.
- Revisor: importa collection y/o lee evidencia de smoke.

## Requisitos funcionales

### RF-1 — Colección importable (positivo + negativo)
**CUANDO** un revisor importa `docs/postman/Meeting-Rooms-API.postman_collection.json` en Postman, **EL ARTEFACTO** expone, para cada uno de los 6 endpoints, un request positivo y un negativo representativo contra `{{baseUrl}}`.

#### Criterios de aceptación
- [ ] Existe `docs/postman/Meeting-Rooms-API.postman_collection.json` (Collection v2.1).
- [ ] Incluye los 6 endpoints (método + path correctos).
- [ ] Por cada endpoint: ≥1 request positivo y ≥1 request negativo representativo (p. ej. login fallido 401; mutación sin Bearer 403; body/query inválido 400; solape reserva 409; cancel no encontrada 404 — según aplique al endpoint).
- [ ] `baseUrl` por defecto `http://localhost:8080`.
- [ ] Variables: `token`, `roomId`, `reservationId` (sin Environment aparte).
- [ ] Login positivo: body `{ "username", "password" }` seed `admin`/`admin`; script guarda `accessToken` → `token`.
- [ ] Mutaciones positivas: `Authorization: Bearer {{token}}` (header exacto).
- [ ] Creates positivos: scripts guardan `id` de respuesta en `roomId` / `reservationId`.

### RF-2 — Payloads y queries fieles a la API
**CUANDO** se ejecuta el flujo feliz de la collection contra la app arrancada, **EL SISTEMA** responde 2xx en login → crear sala → listar salas → crear reserva → listar reservas → cancelar, sin campos inventados.

#### Criterios de aceptación
- [ ] Create room: `name`, `capacity`, `location`.
- [ ] List rooms: query `page`, `size` (defaults coherentes con la API, p. ej. 0/20).
- [ ] Create reservation: `roomId` (variable), `startAt`, `endAt` (ISO-8601 OffsetDateTime).
- [ ] List reservations: query opcionales `roomId`, `date` (`yyyy-MM-dd`), `page`, `size`.
- [ ] Cancel: body `{ "reservationId" }` (variable).
- [ ] No incluye rutas inexistentes (register/refresh/etc.).

### RF-3 — README: extras (doc API + Postman)
**CUANDO** un revisor abre el README raíz, **EL DOCUMENTO** indica que con el tiempo restante de la prueba se incorporaron extras de documentación de API y la Postman Collection, con la ruta del JSON.

#### Criterios de aceptación
- [ ] README menciona acciones **extras** realizadas con el tiempo restante de la prueba.
- [ ] Menciona incorporación de **documentación de la API**.
- [ ] Menciona **Postman Collection** e indica la ruta `docs/postman/Meeting-Rooms-API.postman_collection.json`.
- [ ] Cambio mínimo al README; no reescribe RF 006 completo.

### RF-4 — Smoke feliz completo y todos los negativos
**CUANDO** se cierra este RF, **HAY EVIDENCIA** de smoke: flujo feliz completo y todos los requests negativos de la collection contra la app local.

#### Criterios de aceptación
- [ ] Smoke del flujo feliz completo (login → room → list rooms → reservation → list reservations → cancel) con códigos 2xx esperados.
- [ ] Smoke de **todos** los requests negativos incluidos en la collection (código HTTP esperado por cada uno).
- [ ] Evidencia registrada en review/close o notas de task (p. ej. status codes observados); no se exige Newman/CI.

## Reglas de negocio
- Extra de DX; no sustituye requisitos del enunciado.
- Refleja solo lo implementado en `dev` (6 endpoints + JWT).
- Un negativo “representativo” por endpoint: el que mejor ilustre el fallo típico de ese endpoint (auth, validación o conflicto), sin matriz exhaustiva.
- Mensajes/errores de API siguen en inglés vía `ErrorMessages` (la collection no los redefine).

## Casos límite
- App no arrancada: la collection es válida pero el smoke falla hasta arrancar.
- Token expirado (TTL 1 h): volver a ejecutar Login positivo antes del resto.
- Negativo de solape (409): requiere una reserva ACTIVE previa del flujo feliz o setup explícito en el request negativo.
- GET públicos: el negativo representativo no es “sin auth” (ya es público); usar validación de query (p. ej. `page`/`size`/`date` inválidos → 400).

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado: este RF **no** añade capas de aplicación.
- Mensajes de API en inglés vía `ErrorMessages`.
- Perfiles Spring `local` / `prod`.
- Sin event bus / infra de más en la ventana de 3 h.
- Chat/SDD en español; artefactos técnicos (JSON Postman, paths) en inglés.
- Una GitHub Issue **open** antes de implementar; cierre en `/ssd-cmd-close`.

## Preguntas abiertas
- Ninguna tras AskQuestions (change).

## Impacto en plan/task (aún no editar)
- Aún no existen `plan.md` / `task.md`: crear tras APROBADO con `/ssd-cmd-plan` y `/ssd-cmd-task`.
- El plan deberá incluir: (1) JSON en ruta fija + variables/scripts, (2) 6×(positivo+negativo) por carpetas Auth/Rooms/Reservations, (3) README extras doc API + Postman, (4) smoke feliz + todos los negativos.
- Si más adelante existieran plan/task y este RF cambia otra vez: marcarlos obsoletos y regenerar.

## Siguiente paso
`Próxima acción: IMPLEMENTAR, ejecutar /ssd-cmd-implement spec/008-extra-postman-collection/rf.md`
