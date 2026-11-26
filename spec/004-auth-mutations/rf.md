# RF — Autenticación JWT en mutaciones

Estado: aprobada

## Metadatos
- Carpeta: `spec/004-auth-mutations/`
- Slug: `auth-mutations`
- Orden: `004` (ex mapa `006-auth-mutations`; mapa `000` realineado)
- Tipo: `Sec` | `API` | `DB`
- Origen: change — clarify (users schema, TTL, login 400/401, Authorization 403/401)
- Fecha: 2026-11-26
- GitHub Issue: #5 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/5

## Problema / oportunidad

El enunciado exige autenticación en endpoints que **crean o cancelan**; las consultas son públicas. Hoy las mutaciones están abiertas (deuda 002/003) y `responsible`/`created_by`/`cancelled_by` usan `'system'`.

## Objetivo

Introducir **JWT** (HS256, TTL 1 h): login, usuarios en BD, proteger mutaciones; GET públicos; propagar el **username** del token a `created_by`, `cancelled_by` y `responsible`. Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado; mensajes EN vía `ErrorMessages`.

## Alcance

### Incluye
- Tabla `users`: `id`, `username` UNIQUE, `password` (BCrypt) — **sin** auditoría RF 001 ni `role`.
- Seed: `admin`/`admin`, `user`/`user` (matching **case-sensitive**).
- `POST /api/auth/login` → **200** `{ "accessToken": "<jwt>" }` (público).
- JWT: HS256; claim `sub` = username; TTL **1 hora**; secret + TTL en `application-local.yml` / `application-prod.yml` (demo).
- Spring Security + filtro JWT: `/api/**` autenticado salvo **GET** y `POST /api/auth/login`.
- Mutaciones protegidas (API actual): `POST /api/rooms`, `POST /api/reservations`, `POST /api/reservations/cancel`.
- Auth mutación: sin header / Bearer vacío / scheme ≠ Bearer → **403**; JWT inválido o expirado → **401**.
- Login: body incompleto/blank o JSON ilegible → **400**; usuario inexistente → **401** (mensaje distinto); password incorrecto → **401** (mensaje distinto).
- Username del principal → `created_by`, `responsible`, `cancelled_by`.

### Excluye
- HTTP Basic; registro; refresh; RBAC / columna `role`.
- `tokenType` / `expiresIn` en respuesta login.
- OAuth; HTTPS obligatorio en local.
- Suite ≥3 tests (005); README (006).
- Cambiar contrato de GET; PostgreSQL.

## Actores
- Cliente API: login + mutaciones con Bearer.
- Humano: aprueba RF; Issue open antes de implementar.
- Agente: implementa tras plan/task.

## Requisitos funcionales

### RF-1 — Usuarios persistidos
**CUANDO** arranca la app, **EL SISTEMA** tiene tabla `users` con seed `admin` y `user` (passwords BCrypt de `admin` y `user`).

#### Criterios de aceptación
- [ ] Migración Flyway: `users` con `id`, `username` VARCHAR UNIQUE NOT NULL, `password` VARCHAR NOT NULL (hash BCrypt).
- [ ] Sin columnas de auditoría RF 001; sin `role`.
- [ ] Seed: `admin`/`admin`, `user`/`user`.
- [ ] Passwords nunca en claro en respuestas API.
- [ ] Lookup de username **case-sensitive**.

### RF-2 — Login JWT
**CUANDO** un cliente envía `POST /api/auth/login` con credenciales válidas, **EL SISTEMA** responde **200** con `{ "accessToken": "<jwt>" }`.

#### Criterios de aceptación
- [ ] Body: `username`, `password` required.
- [ ] Ausentes/blank → **400** vía `ErrorMessages`.
- [ ] JSON ilegible → **400** vía `ErrorMessages`.
- [ ] Username inexistente → **401** con mensaje `ErrorMessages` **distinto** al de password incorrecto.
- [ ] Password incorrecto → **401** con mensaje `ErrorMessages` distinto.
- [ ] Endpoint **público**.
- [ ] Token HS256; `sub`=username; expira a **1 h**; secret desde config perfil.
- [ ] Login vía command/handler de aplicación (sin lógica de salas/reservas en el filtro).

### RF-3 — Proteger mutaciones / consultas públicas
**CUANDO** un cliente llama un endpoint bajo `/api/**`, **EL SISTEMA** exige JWT salvo **GET** y `POST /api/auth/login`.

#### Criterios de aceptación
- [ ] GET `/api/rooms`, GET `/api/reservations` sin auth → **200** (como hoy).
- [ ] POST mutación **sin** `Authorization`, con `Bearer` vacío, o scheme ≠ `Bearer` → **403** + `ErrorMessages`.
- [ ] POST con Bearer **inválido o expirado** → **401** + `ErrorMessages`.
- [ ] POST con Bearer válido → comportamiento de negocio 002/003 (201/200/400/404/409).
- [ ] Formato: `Authorization: Bearer <token>`.
- [ ] GET con token inválido: **no bloquea** (consulta pública).

### RF-4 — Identidad en escritura
**CUANDO** una mutación autenticada crea o cancela, **EL SISTEMA** persiste el username del token (no `'system'`).

#### Criterios de aceptación
- [ ] Create room: `created_by` = username.
- [ ] Create reservation: `created_by` y `responsible` = username.
- [ ] Cancel reservation: `cancelled_by` = username.
- [ ] Cierra deuda auth/responsable de 002/003.

## Reglas de negocio
- Enunciado §5: create/cancel con auth; consultas sin.
- Dos usuarios demo; mismo permiso de mutar (sin roles).
- **403** = no autenticado (ausencia / scheme incorrecto / Bearer vacío).
- **401** = autenticación fallida (JWT malo/expirado; login user/password).
- Username case-sensitive; secret JWT demo en repo.

## Casos límite
- Login JSON malo / campos blank → 400.
- User no existe vs password malo → 401 mensajes distintos.
- Mutación sin header o `Authorization: Basic …` → 403.
- Token malformado / firma incorrecta / expirado → 401.
- Sin leeway de reloj documentado (expiración estricta).
- Usuario del token ya no en BD: fuera de alcance (solo seed fijo en este RF).

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado; sin event bus.
- Mensajes de API en inglés vía `ErrorMessages`.
- Perfiles Spring `local` / `prod`; H2 mem.
- Chat/SDD en español; código en inglés.
- Una GitHub Issue **open** antes de implementar; cierre en `/ssd-cmd-close`.

## Preguntas abiertas
- Ninguna tras AskQuestions post-clarify.

## Impacto en plan/task (aún no editar)
- No existen `plan.md` / `task.md`: se crearán tras APROBADO vía `/ssd-cmd-plan` y `/ssd-cmd-task`.

## Siguiente paso
`Próxima acción: REVISAR EL RESULTADO, ejecutar /ssd-cmd-reviewer spec/004-auth-mutations/rf.md`
