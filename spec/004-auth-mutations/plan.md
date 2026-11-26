# Plan — 004 auth-mutations

## Metadatos
- Carpeta: `spec/004-auth-mutations/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-26
- Rama: `feature/004-auth-mutations` (desde `dev`)
- Issue: #5 (open)
- Dependencia: RF 003 cerrado

## Enfoque y orden

1. **Deps (RF-2/3):** `spring-boot-starter-security` + librería JWT HS256 (p. ej. `jjwt` 0.12.x).
2. **DB (RF-1):** Flyway `V4__create_users.sql` — `users(id, username, password)`; seed BCrypt `admin`/`user`.
3. **Dominio/puertos auth (RF-1/2):** puerto lectura usuario por username; entity/adapter JPA.
4. **JWT + Security (RF-2/3):** emitir/validar token; `SecurityFilterChain` (GET + login públicos; resto `/api/**` autenticado); entry point/access denied → 401/403 con `ErrorMessages`.
5. **Login command + REST (RF-2):** `POST /api/auth/login`.
6. **Identidad (RF-4):** handlers create/cancel dejan de usar `'system'`; toman username del `SecurityContext`.
7. Smoke: login, GET sin token, POST sin token 403, Bearer malo 401, POST con token 201 + `created_by`/`responsible`.

## Clean Architecture (CA) + CQRS delgado

| Capa | Rol en 004 |
|------|------------|
| `domain.model` (+ port) | Usuario mínimo / `UserQueryPort` (findByUsername) |
| `application.command` | `LoginCommand` + handler (valida, emite JWT) |
| `infrastructure.adapters.persistence` | `UserJpa*` |
| `infrastructure.config` / security | `SecurityConfig`, `JwtService`, filtro Bearer |
| `infrastructure.entrypoints.rest` | `AuthController`; advice 401/403 |
| Handlers rooms/reservations | Solo cambio de origen del username (RF-4) |

Sin event bus. Filtro JWT **no** contiene reglas de salas/reservas.

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF |
|---------|-----------------|-----|
| `pom.xml` | security + jjwt | RF-2/3 |
| `db/migration/V4__create_users.sql` | tabla + seed BCrypt | RF-1 |
| `domain/.../User.java` + `UserQueryPort` | lookup case-sensitive | RF-1/2 |
| `…/persistence/UserJpaEntity` + repo + adapter | JPA users | RF-1 |
| `…/security/JwtService.java` | create/parse HS256, TTL 1h | RF-2/3 |
| `…/security/JwtAuthenticationFilter.java` | Bearer → Authentication | RF-3 |
| `…/config/SecurityConfig.java` | authorizeHttpRequests; CSRF off; stateless | RF-3 |
| `application/command/LoginCommand` + `Handler` | login 400/401 + token | RF-2 |
| `…/rest/AuthController` + DTOs | `POST /api/auth/login` | RF-2 |
| `shared/ErrorMessages.java` | AUTH_* constants EN | RF-2/3 |
| `RestExceptionHandler` (+ entry points) | 401/403 bodies | RF-2/3 |
| `application-local.yml` / `application-prod.yml` | `app.jwt.secret`, `app.jwt.expiration-ms: 3600000` | RF-2 |
| `CreateRoomHandler`, `CreateReservationHandler`, `CancelReservationHandler` (+ controllers si pasan username) | RF-4 identidad | RF-4 |

**Seed BCrypt:** hashes precomputados en SQL (o `@PostConstruct` solo si Flyway no puede insertar hash; preferir SQL con hashes conocidos de `admin`/`user`).

## Algoritmos

### Login (RF-2)

```text
PARSE body; IF null/unreadable → 400 VALIDATION_FAILED
IF username blank OR password blank → 400
user = findByUsernameExact(username)
IF user is null → 401 USER_NOT_FOUND   // mensaje distinto
IF !BCrypt.matches(password, user.password) → 401 INVALID_PASSWORD
token = JwtService.create(sub=username, exp=now+1h, HS256, secret)
RETURN 200 { accessToken: token }
```

### Filtro / cadena (RF-3)

```text
IF method == GET OR path == POST /api/auth/login → permitAll
ELSE:
  IF no Authorization OR not starting "Bearer " OR token blank → 403 UNAUTHENTICATED
  IF scheme != Bearer (e.g. Basic) → 403
  TRY parse/validate JWT
  IF fail (bad sig / expired / malformed) → 401 INVALID_TOKEN
  ELSE set SecurityContext(username) → continue
```

### Identidad (RF-4)

```text
username = SecurityContext.getAuthentication().getName()
createRoom: created_by = username
createReservation: created_by = responsible = username
cancel: cancelled_by = username
```

## Mensajes de error (`ErrorMessages` EN)

Añadir (literales exactos en implementación):

- login username/password required / blank
- user not found (401)
- invalid password (401)
- unauthenticated / missing bearer (403)
- invalid or expired token (401)

Reutilizar `VALIDATION_FAILED` para JSON ilegible si aplica.

## Perfiles `local` / `prod`

Ambos: propiedades JWT demo (`secret` distinto o mismo demo; **no** secretos reales). TTL `3600000` ms. H2 mem sin cambio de datasource.

## Estrategia de pruebas

Este RF: smoke curl (login, 403/401, GET público, create con token).  
≥3 tests formales → **005**.

## Fuera de alcance

Basic, register, refresh, RBAC, roles, tokenType/expiresIn, OAuth, tests suite, README.

## Trazabilidad

| Id | Cubierto por |
|----|----------------|
| RF-1 | V4 users + JPA |
| RF-2 | Login command + JWT emit + AuthController |
| RF-3 | SecurityConfig + JwtFilter + 401/403 |
| RF-4 | Handlers rooms/reservations username |

## Próximo paso

`/ssd-cmd-task` → `task.md` + GitHub Issue **open**.
