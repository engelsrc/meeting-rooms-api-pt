# RF — Crear, listar y cancelar reservas

Estado: aprobada

## Metadatos
- Carpeta: `spec/003-reservations-crud/`
- Slug: `reservations-crud`
- Orden: `003`
- Tipo: `API` | `Model` | `DB`
- Origen: change — resolve clarify (soft-delete, zona, list, cancel body, lock solape)
- Fecha: 2026-11-26
- GitHub Issue: #4 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/4

## Problema / oportunidad

Con salas ya expuestas (RF 002), falta el núcleo del enunciado: **crear, listar y cancelar reservas**, con solape → **409**, validación → **400**, y listado con filtro por sala y por fecha más paginación. Sin esto no se cumple el producto de reserva de salas.

## Objetivo

Exponer **solo** los endpoints que pide el enunciado para reservas: crear, listar (filtro `roomId` + `date` + paginación) y cancelar; persistencia H2 + Flyway; solape en aplicación vs reservas **ACTIVE** con intervalos `[start, end)` bajo zona fija `America/Mexico_City`; lock/transacción para carreras; Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado; mensajes en inglés vía `ErrorMessages`. Auth de mutaciones diferida a **006** (deuda explícita §5). `responsible` fijo `'system'` hasta 006 (deuda explícita §1).

## Alcance

### Incluye
- Tabla `reservations` (Flyway) con negocio + auditoría RF 001 (incl. soft-delete **heredado sin uso** en este RF) + `cancelled_by` / `cancelled_at`.
- Dominio / commands (create, cancel) / query list / adapters JPA / REST + `ErrorMessages`.
- `POST /api/reservations` — body JSON; **201** + DTO de reserva.
- `GET /api/reservations?roomId=&date=&page=&size=` — **200** con `{ items, page, size, total }`.
- `POST /api/reservations/cancel` — body `{ reservationId }`; **200** + DTO (o **409** si ya CANCELLED; **404** si no existe / id inválido).
- Solape en app: misma sala, solo vs `ACTIVE`, `[startAt, endAt)`; tiempo compartido → **409**; extremos iguales OK.
- Creates concurrentes solapados: transacción + lock de sala; el perdedor → **409**.
- Zona fija de negocio: `America/Mexico_City` (filtro `date` y comparaciones de instante/día).
- Validación → **400** (`ErrorMessages`).
- Dependencia: RF 002 cerrado (`close.md`).

### Excluye
- Auth JWT/Basic en create/cancel (**006**; deuda §5).
- `responsible` en body del cliente (**006**; deuda §1 — valor `'system'`).
- GET por id; cancel con id en path; PUT/PATCH; hard delete; EXCLUDE gist.
- Uso de soft-delete en reservas (`is_deleted` no se escribe ni filtra en 003).
- Comportamiento de reservas si la sala se soft-deletea después (sin API de borrado de sala; fuera de alcance).
- Envelope Spring Data Page; tests ≥3 (007); README (008).
- Filtros distintos a `roomId` + `date` (día de `startAt` en zona fija); rangos `from`/`to`.
- Event bus / CQRS “enterprise”.

## Actores
- Cliente API: crea, lista y cancela reservas.
- Humano: aprueba RF; Issue open antes de implementar.
- Agente: implementa tras plan/task.

## Requisitos funcionales

### RF-1 — Modelo y persistencia de reserva
**CUANDO** se persista una reserva, **EL SISTEMA** guarda `room_id`, `responsible`, `start_at`, `end_at`, `status` (`ACTIVE` | `CANCELLED`) más columnas de auditoría/soft delete de RF 001 y `cancelled_by` / `cancelled_at` (null mientras ACTIVE). Soft-delete **no se usa** en operaciones de este RF.

#### Criterios de aceptación
- [ ] Migración Flyway crea `reservations` sin romper V1/V2.
- [ ] FK a `rooms` (sala debe existir).
- [ ] Columnas de negocio: `room_id`, `responsible`, `start_at`, `end_at`, `status`.
- [ ] Auditoría RF 001: `created_by`, `created_at`, `updated_by`, `updated_at`, `is_deleted`, `deleted_by`, `deleted_at` (presentes; `is_deleted` default false; cancel **no** las muta).
- [ ] `cancelled_by`, `cancelled_at` (nullable).
- [ ] H2-compatible; solape en aplicación + lock (no EXCLUDE gist).

### RF-2 — Crear reserva
**CUANDO** un cliente envía `POST /api/reservations` con JSON válido y sin solape ACTIVE, **EL SISTEMA** crea la reserva **ACTIVE** y responde **201** con `{ id, roomId, responsible, startAt, endAt, status }`.

#### Criterios de aceptación
- [ ] Body: `roomId` (required), `startAt` / `endAt` (required, ISO-8601 `OffsetDateTime`).
- [ ] `responsible` persistido como `'system'` (deuda hasta 006; no va en body).
- [ ] Auth **no** requerida (deuda hasta 006); `created_by = 'system'`.
- [ ] `endAt > startAt` (comparación en instantes; zona de negocio `America/Mexico_City` documentada); si no → **400**.
- [ ] Sala inexistente (o soft-deleted) → **404**.
- [ ] Solape con otra **ACTIVE** misma sala (intersección de `[startAt, endAt)`) → **409**.
- [ ] Extremo a extremo (`endAt` de A = `startAt` de B) → **no** es solape.
- [ ] Create concurrente solapado: con lock/transacción sobre la sala, el perdedor → **409** (nunca dos ACTIVE solapadas).
- [ ] Campos inválidos / ausentes → **400** vía `ErrorMessages`.
- [ ] Respuesta **201** solo con el DTO acordado (sin auditoría).
- [ ] Mutación vía **command** (CQRS delgado).

### RF-3 — Listar reservas (filtro sala + fecha + paginación)
**CUANDO** un cliente llama `GET /api/reservations` con query opcional, **EL SISTEMA** responde **200** con `{ "items": [...], "page": N, "size": N, "total": N }`.

#### Criterios de aceptación
- [ ] Query: `roomId` (opcional), `date` (opcional, `yyyy-MM-dd`: reservas cuyo `startAt` cae en ese **día en `America/Mexico_City`**), `page`, `size`.
- [ ] Defaults: `page=0`, `size=20`.
- [ ] `page < 0`, `size < 1`, `size > 100` → **400**.
- [ ] `date` mal formado → **400**.
- [ ] `roomId` presente pero sala inexistente → **400**.
- [ ] Orden estable: `startAt ASC`, empate `id ASC`.
- [ ] Lista **ACTIVE y CANCELLED** (no filtra por `is_deleted`).
- [ ] Sin body en GET; filtros/paginación solo en query.
- [ ] Cada item: `id`, `roomId`, `responsible`, `startAt`, `endAt`, `status`.
- [ ] Lista vacía (filtros OK, sin filas) → **200** / `items: []` / `total: 0`.
- [ ] Lectura vía **query** (CQRS delgado); sin auth.

### RF-4 — Cancelar reserva
**CUANDO** un cliente llama `POST /api/reservations/cancel` con body válido sobre una reserva ACTIVE, **EL SISTEMA** pasa a `CANCELLED`, rellena `cancelled_by` / `cancelled_at`, y responde **200** con el DTO actualizado.

#### Criterios de aceptación
- [ ] Body: `{ "reservationId": <id> }` (required).
- [ ] `reservationId` ausente / no parseable / inexistente → **404**.
- [ ] Ya `CANCELLED` → **409**.
- [ ] Auth **no** requerida (deuda hasta 006); `cancelled_by = 'system'` hasta 006.
- [ ] No soft-delete ni hard-delete; queda listable con `status=CANCELLED`.
- [ ] Tras cancelar, ese intervalo **no** cuenta para solape.
- [ ] Mutación vía **command**.

## Reglas de negocio
- Reserva = sala + responsable + inicio + fin; responsable `'system'` hasta 006 (deuda).
- Status: `ACTIVE` | `CANCELLED`; soft-delete columnas presentes sin uso operativo.
- Zona fija: `America/Mexico_City`.
- Solape: misma sala, solo ACTIVE, `[start, end)`; lock en create concurrente → 409 al perdedor.
- Listado: `roomId` / `date` opcionales + paginación; orden `startAt ASC, id ASC`.
- Contrato: POST con JSON body; GET con query params; página `{ items, page, size, total }`.
- Auth mutaciones diferida a 006 (deuda §5).
- Mapa 000: solape/validación/filtros básicos viven aquí; RF 004/005 pueden reducirse después.

## Casos límite
- Lista vacía → 200 / `items: []` / `total: 0`.
- `page`/`size` inválidos o `size > 100` → 400.
- `date` mal formado → 400; `roomId` de sala inexistente en GET → 400.
- Sala soft-deleted → 404 al **crear**.
- Cancel: id inválido/inexistente → 404; ya cancelada → 409.
- Dos creates concurrentes solapados → uno 201, otro **409** (lock).
- Sala soft-deleted *después* de crear reserva: **fuera de alcance** 003.

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado; sin event bus.
- Mensajes de API en inglés vía `ErrorMessages`.
- Perfiles Spring `local` / `prod`; H2 mem (decisión repo RF 001).
- Chat/SDD en español; código en inglés.
- Una GitHub Issue **open** antes de implementar; cierre en `/ssd-cmd-close`.

## Preguntas abiertas
- Ninguna tras AskQuestions post-clarify.

## Impacto en plan/task (aún no editar)
- No existen `plan.md` / `task.md` en esta carpeta: se crearán tras aprobación del RF vía `/ssd-cmd-plan` y `/ssd-cmd-task`.

## Siguiente paso
`Próxima acción: IMPLEMENTAR, ejecutar /ssd-cmd-implement spec/003-reservations-crud/rf.md`
