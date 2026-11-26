# Review 001 — 001 persistence-migration

**Veredicto:** cumple  
**Fecha:** 2026-11-25  
**Issue:** #2 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/2 — **open**  
**Alcance revisado:** rama `feature/001-persistence-migration`, `pom.xml`, `application*.yml`, `V1__schema_meta.sql`, paquetes CA, `task.md` DB-1…DB-8, arranques documentados en DB-6/DB-8

## Por requisito

### RF-1 — Scaffold Spring Boot (Maven)
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| Maven Spring Boot Java 17+ | sí | `pom.xml`: parent 3.3.5, `java.version=17` |
| groupId/artifactId | sí | `com.meetingrooms` / `meeting-rooms-api` |
| Arranque default prod / activable local | sí | `application.yml` → `active: prod`; DB-8: Started con `prod` y `local` |
| Sin secretos de prod | sí | H2 `sa` / vacío; sin credenciales reales |

**Prueba del plan:** scaffold + arranque → **pasa**

### RF-2 — Perfiles local/prod H2 mem
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| application-local/prod + base | sí | tres YAML presentes |
| Mismo nombre H2 mem | sí | ambos `jdbc:h2:mem:meetingrooms;…` |
| Default prod | sí | `spring.profiles.active: prod` |
| Sin secretos reales | sí | demo H2 |

**Prueba del plan:** perfiles → **pasa**

### RF-3 — Flyway + JPA placeholder
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| Dependencias JPA/H2/Flyway | sí | `pom.xml` |
| Script V1 no de negocio | sí | `V1__schema_meta.sql` (tabla `schema_meta`) |
| Arranque aplica migración | sí | logs DB-8: Successfully applied 1 migration … v1 |
| Sin rooms/reservations | sí | no hay CREATE de esas tablas |
| ddl-auto no crea negocio | sí | `ddl-auto: validate` en `application.yml` |

**Prueba del plan:** Flyway V1 → **pasa**

### RF-4 — Paquetes CA + CQRS delgado
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| Seis paquetes base | sí | `domain.model`, `application.command/query`, `infrastructure.entrypoints/adapters/config` |
| Sin controllers salas/reservas | sí | solo `package-info` + Application |
| Sin event bus | sí | no hay infra CQRS pesada |

**Prueba del plan:** esqueleto paquetes → **pasa**

### RF-5 — Auditoría y soft delete
| Criterio | ¿Cubierto? | Evidencia |
|----------|------------|-----------|
| Columnas en schema_meta | sí | `created_by/at`, `updated_by/at`, `is_deleted`, `deleted_by/at` en V1 |
| Convención documentada para futuro | sí | `rf.md` + `plan.md` sección convención |
| Soft delete vía is_deleted | sí | RF + columna + seed `FALSE` |
| Arranque tras esquema | sí | DB-8: Flyway V1 + Started OK (`prod`/`local`) |
| Seed created_by/system | sí | INSERT con `'system'`, `is_deleted FALSE` |

**Prueba del plan:** ajustar V1 + re-verificar → **pasa**

## Contraste con constitución
- CA + CQRS delgado: esqueleto de paquetes; sin event bus — **OK**
- Perfiles `local`/`prod` — **OK**
- `ErrorMessages` / auth / ≥3 tests API: **N/A** (fuera de alcance de este RF)
- GitHub Issue #2 **open** — **OK** (cierre en `/ssd-cmd-close`)

## Fuera de alcance (correctamente no hecho)
- rooms/reservations, REST, auth, PostgreSQL/Docker, Liquibase, Gradle, README entrega, hard delete

## Huecos
Ninguno material respecto al RF aprobado.

## Veredicto
**cumple**
