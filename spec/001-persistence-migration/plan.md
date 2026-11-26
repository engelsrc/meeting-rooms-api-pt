# Plan — 001 persistence-migration

## Metadatos
- Carpeta: `spec/001-persistence-migration/`
- RF origen: `rf.md` (`Estado: aprobada`; change auditoría RF-5)
- Fecha: 2026-11-25
- Rama de trabajo: `feature/001-persistence-migration`
- Build: Maven — `com.meetingrooms` / `meeting-rooms-api`
- Issue: #2 (open)

## Enfoque y orden

Scaffold **DB + app base** + **convención de auditoría**. Gran parte de RF-1…RF-4 ya está en la rama; el hueco del change es **RF-5**.

1. ~~Scaffold Maven Spring Boot~~ (**RF-1** — hecho en rama).
2. ~~Dependencias JPA / H2 / Flyway~~ (**RF-3** — hecho).
3. ~~Perfiles `local`/`prod`, H2 mem mismo nombre, default `prod`~~ (**RF-2** — hecho).
4. ~~Paquetes CA + CQRS delgado~~ (**RF-4** — hecho).
5. **Ajustar migración placeholder** para columnas de auditoría + soft delete en `schema_meta` (**RF-5**, impacta RF-3).
6. **Re-verificar arranque** (`prod` y opcional `local`) con Flyway V1 actualizado (**RF-1/2/3/5**).

Estrategia H2 mem: **reescribir `V1__schema_meta.sql`** (no hace falta V2; no hay historial persistente entre reinicios). Documentar la misma lista de columnas para rooms/reservations futuros.

## Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado

Paquetes bajo `com.meetingrooms` (ya creados con `package-info`):

| Paquete | Rol |
|---------|-----|
| `domain.model` | Futuras entidades; auditoría podrá vivir en mapped superclass / base más adelante |
| `application.command` / `application.query` | Vacíos en este RF |
| `infrastructure.entrypoints` | Sin controllers de negocio |
| `infrastructure.adapters` | Persistencia futura |
| `infrastructure.config` | YAML cubre perfiles |

Sin event bus. En este RF la auditoría es **solo esquema SQL** (no AuditingEntityListener obligatorio aún).

## Convención de auditoría (todas las tablas)

Columnas obligatorias (RF-5):

| Columna | Tipo | Notas |
|--------|------|--------|
| `created_by` | `VARCHAR(100) NOT NULL` | p. ej. seed `'system'` |
| `created_at` | `TIMESTAMP NOT NULL` | default `CURRENT_TIMESTAMP` |
| `updated_by` | `VARCHAR(100) NULL` | hasta primer update |
| `updated_at` | `TIMESTAMP NULL` | hasta primer update |
| `is_deleted` | `BOOLEAN NOT NULL DEFAULT FALSE` | soft delete |
| `deleted_by` | `VARCHAR(100) NULL` | |
| `deleted_at` | `TIMESTAMP NULL` | |

Aplicable a `schema_meta` ahora y a **rooms** / **reservations** en RFs posteriores.

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF | Estado |
|---------|-----------------|-----|--------|
| `pom.xml` | Maven, JPA, H2, Flyway | RF-1, RF-3 | Hecho |
| `…/MeetingRoomsApiApplication.java` | Bootstrap | RF-1 | Hecho |
| `application.yml` + `application-local.yml` + `application-prod.yml` | Perfiles H2 mem | RF-2 | Hecho |
| Paquetes `domain` / `application` / `infrastructure` | Esqueleto CA | RF-4 | Hecho |
| `db/migration/V1__schema_meta.sql` | Placeholder + **columnas auditoría** | RF-3, RF-5 | **Pendiente ajustar** |

## Algoritmo / procedimiento (pendiente RF-5)

```text
EDITAR V1__schema_meta.sql:
  CREATE TABLE schema_meta (
    id, meta_key, meta_value,
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100) NULL,
    updated_at TIMESTAMP NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_by VARCHAR(100) NULL,
    deleted_at TIMESTAMP NULL
  );
  INSERT ... VALUES (..., 'system', CURRENT_TIMESTAMP, NULL, NULL, FALSE, NULL, NULL);

ARRARCAR: mvn -DskipTests spring-boot:run          # perfil prod
ASSERT: Flyway V1 OK + Started MeetingRoomsApiApplication
(OPCIONAL) ARRARCAR con profiles=local → mismo OK
```

## Mensajes de error (`ErrorMessages`)

No aplica HTTP de negocio en este RF.

## Perfiles `local` / `prod`

Sin cambio respecto al plan anterior: H2 `jdbc:h2:mem:meetingrooms`; default `prod`; `ddl-auto=validate`.

## Estrategia de pruebas

Verificación: arranque + Flyway aplica V1 con columnas de auditoría.  
≥3 tests / integración → RF de tests posterior.

## Fuera de alcance

- Tablas rooms/reservations (heredarán la convención)
- REST, auth, hard delete como política
- PostgreSQL / Docker / Liquibase / Gradle
- `@Entity` / `AuditingEntityListener` (opcional futuro; no gate de este RF)
- README de entrega

## Trazabilidad RF → plan

| Id | Cubierto por |
|----|----------------|
| RF-1 | Scaffold + arranque (re-verificar tras V1) |
| RF-2 | `application*.yml` |
| RF-3 | JPA + Flyway + V1 (con auditoría) |
| RF-4 | Paquetes CA |
| RF-5 | Columnas auditoría/soft delete en V1 + convención documentada |

## Próximo paso

`/ssd-cmd-task` → regenerar `task.md` (marcar hechas las ya cumplidas; dejar pendiente(s) de auditoría) + Issue #2 ya **open**.
