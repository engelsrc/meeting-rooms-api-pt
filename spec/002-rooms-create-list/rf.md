# RF — Crear y listar salas

Estado: aprobada

## Metadatos
- Carpeta: `spec/002-rooms-create-list/`
- Slug: `rooms-create-list`
- Orden: `002`
- Tipo: `API` | `Model` | `DB`
- Origen: change tras clarify + AskQuestions (auth diferida, paginación, unicidad, location)
- Fecha: 2026-11-25
- GitHub Issue: #3 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/3

## Problema / oportunidad

Sin API de salas no se puede reservar: hace falta crear y listar salas sobre la persistencia ya cableada (RF 001).

## Objetivo

Exponer **crear sala** y **listar salas** (paginado, solo no borradas) vía REST, con modelo `name` + `capacity` + `location` opcional, Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado, mensajes de error en inglés vía `ErrorMessages`. Auth de mutaciones se difiere a **006** (deuda explícita respecto al enunciado hasta ese RF).

## Alcance

### Incluye
- Tabla `rooms` (Flyway) con campos de negocio + **auditoría/soft delete** (convención RF 001).
- Dominio / command create / query list / adapters JPA / REST entrypoints + `ErrorMessages`.
- `POST /api/rooms` — body JSON; **201** + sala `{ id, name, capacity, location }`.
- `GET /api/rooms?page=&size=` — sin body; query params; **200** con `{ items, page, size, total }`; excluir `is_deleted=true`; lista vacía → 200.
- Defaults: `page=0`, `size=20`. Inválidos → **400**.
- Validación → **400** (`ErrorMessages` EN).
- Nombre duplicado (**único global**, también soft-deleted) → **409**.
- `created_by = 'system'` en create hasta RF 006.
- Dependencia de scaffold RF 001 (`close.md` ya existe).

### Excluye
- Auth en POST (va en **006-auth-mutations**; deuda temporal vs enunciado).
- PUT/PATCH/DELETE de salas.
- Reservas, solape, filtros por fecha (RFs 003–005).
- GET con body; paginación/filtros en headers.
- Envelope Spring Data Page (`content`/`totalElements`); se usa el mínimo propio.
- PostgreSQL / Docker.
- README de entrega; suite ≥3 tests (RF 007).

## Actores
- Cliente API: crea y lista salas.
- Humano: aprueba RF; Issue open antes de implementar.
- Agente: implementa tras plan/task.

## Requisitos funcionales

### RF-1 — Modelo y persistencia de sala
**CUANDO** se persista una sala, **EL SISTEMA** guarda `name` (obligatorio, ≤100), `capacity` (entero > 0), `location` (string opcional, ≤255 si presente) más columnas de auditoría/soft delete de la convención RF 001, con `name` **único global**.

#### Criterios de aceptación
- [ ] Existe migración Flyway que crea tabla `rooms` (sin romper V1 `schema_meta`).
- [ ] Columnas de negocio: `name`, `capacity`, `location` (nullable).
- [ ] Columnas auditoría: `created_by`, `created_at`, `updated_by`, `updated_at`, `is_deleted`, `deleted_by`, `deleted_at`.
- [ ] Restricción/regla: `name` único en toda la tabla (incluye filas soft-deleted).

### RF-2 — Crear sala
**CUANDO** un cliente envía `POST /api/rooms` con JSON válido, **EL SISTEMA** crea la sala y responde **201** con `{ id, name, capacity, location }`.

#### Criterios de aceptación
- [ ] `POST /api/rooms` body: `name` (required), `capacity` (required, > 0), `location` (optional).
- [ ] Auth **no** requerida (deuda hasta 006).
- [ ] `created_by` persistido como `'system'` (hasta 006).
- [ ] `location` omitido o JSON `null` → columna `null`; `location: ""` se guarda como string vacío; con texto → ese valor.
- [ ] `name` vacío/blank, `name` > 100, `capacity` ≤ 0, `location` > 255 → **400** vía `ErrorMessages`.
- [ ] Nombre ya existente (aunque soft-deleted) → **409** vía `ErrorMessages`.
- [ ] Respuesta **201** solo con `id`, `name`, `capacity`, `location` (sin campos de auditoría en el DTO).
- [ ] Mutación vía **command** (CQRS delgado); entrypoint sin lógica de negocio.

### RF-3 — Listar salas paginado
**CUANDO** un cliente llama `GET /api/rooms?page=&size=`, **EL SISTEMA** devuelve **200** con `{ "items": [...], "page": N, "size": N, "total": N }` de salas no borradas.

#### Criterios de aceptación
- [ ] Query `page` y `size`; si se omiten → `page=0`, `size=20`.
- [ ] `page` < 0, `size` < 1 (u otros inválidos acordados) → **400** vía `ErrorMessages`.
- [ ] Sin body en GET; paginación no en headers.
- [ ] Solo `is_deleted = false`.
- [ ] Cada item: `id`, `name`, `capacity`, `location`.
- [ ] Lista vacía → **200** con `items: []` y `total: 0` (no 404).
- [ ] Lectura vía **query** (CQRS delgado); sin auth.

## Reglas de negocio
- Campos: `name` obligatorio ≤100; `capacity` > 0; `location` opcional (null / `""` / valor; ≤255 si no null).
- Soft delete: listados excluyen borradas; unicidad de `name` es **global**.
- Duplicado → 409; validación → 400.
- Contrato: `/api`; POST JSON; GET query; página `{ items, page, size, total }`.
- Auth create diferida a 006; `created_by='system'` hasta entonces.
- RF 005 (reservas): filtros en query — fuera de alcance, alineado.

## Casos límite
- Lista vacía → 200 / `items: []` / `total: 0`.
- `page`/`size` inválidos → 400 (no coerción).
- Soft-deleted: no en list; **no** se puede reusar el mismo `name` (único global).
- POST sin auth; `created_by='system'`.
- Concurrencia mismo `name`: uno gana; el otro 409 (o error de restricción mapeado a 409).

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado; sin event bus.
- Mensajes de API en inglés vía `ErrorMessages`.
- Perfiles Spring `local` / `prod`.
- Chat/SDD en español; código en inglés.
- Una GitHub Issue **open** antes de implementar; cierre en `/ssd-cmd-close`.

## Preguntas abiertas
- Ninguna tras AskQuestions del change.

## Siguiente paso
`Próxima acción: CREAR EL PLAN, ejecutar /ssd-cmd-plan spec/002-rooms-create-list/rf.md`
