# Review 001 — 004 auth-mutations

**Veredicto:** cumple  
**Fecha:** 2026-11-26  
**Issue:** #5 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/5 — **open**  
**Alcance:** rama `feature/004-auth-mutations`; V4 users; JWT; Security; login; identidad; smoke API-3

## Por requisito

### RF-1 — Usuarios persistidos
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| Flyway `users` id/username/password | sí | `V4__create_users.sql`; Flyway v4 |
| Sin auditoría RF 001 / sin role | sí | DDL solo 3 columnas |
| Seed admin/user BCrypt | sí | INSERT + login smoke 200 |
| Password no en respuestas | sí | `LoginResponse` solo `accessToken` |
| Username case-sensitive | sí | `findByUsername` exacto (sin smoke Admin≠admin) |

**Prueba del plan:** V4 + JPA → **pasa**

### RF-2 — Login JWT
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| POST `/api/auth/login` 200 `{accessToken}` | sí | smoke 200 |
| blank → 400 ErrorMessages | sí | `LoginHandler` AUTH_USERNAME/PASSWORD_REQUIRED (sin curl blank) |
| JSON ilegible → 400 | sí | `RestExceptionHandler` VALIDATION_FAILED |
| user inexistente → 401 mensaje distinto | sí | smoke `User not found` |
| password incorrecto → 401 distinto | sí | smoke `Invalid password` |
| Público | sí | SecurityConfig permitAll login |
| HS256, sub, TTL 1h, secret YAML | sí | `JwtService` + `application-*.yml` expiration 3600000 |
| Command/handler | sí | `LoginCommand` + `LoginHandler` |

**Prueba del plan:** Login + JWT → **pasa**

### RF-3 — Proteger mutaciones
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| GET sin auth 200 | sí | smoke GET rooms 200 |
| Sin Authorization → 403 | sí | smoke POST 403 |
| Bearer vacío / ≠Bearer → 403 | sí | `JwtAuthenticationFilter` (sin curl Basic/vacío) |
| JWT inválido → 401 | sí | smoke Bearer malo 401 |
| Bearer válido → negocio OK | sí | smoke create room/reservation 201, cancel 200 |
| GET con token inválido no bloquea | sí | filtro `shouldNotFilter` GET |

**Prueba del plan:** SecurityConfig + filtro → **pasa**

### RF-4 — Identidad
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| create room `created_by`=username | sí | handler `actorUsername` (DTO room no expone campo; código) |
| reservation `created_by`+`responsible` | sí | smoke `responsible=admin` |
| cancel `cancelled_by` | sí | handler set cancelledBy=actor (DTO no expone; status CANCELLED smoke) |
| Cierra deuda system | sí | ya no SYSTEM_USER en handlers |

**Prueba del plan:** propagación username → **pasa**

## Contraste con constitución
- §5 JWT en create/cancel; consultas sin auth: **OK**
- CA + CQRS delgado: **OK** (Login command; filtro sin reglas de negocio salas)
- `ErrorMessages` EN: **OK**
- Perfiles local/prod JWT demo: **OK**
- Issue #5 **open**: **OK**
- ≥3 tests formales: **N/A** (RF 005)

## Fuera de alcance (correcto)
Basic, register, refresh, RBAC, tokenType/expiresIn, suite tests, README.

## Huecos
- Smoke no cubrió blank login 400, Bearer vacío/Basic 403, ni lectura DB de `created_by`/`cancelled_by` (sí `responsible` en response).
Ninguno material frente al RF aprobado.

## Veredicto
**cumple**
