# Tareas — 001 persistence-migration

GitHub Issue: #2 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/2 (open)  
Rama de trabajo: `feature/001-persistence-migration`  
Sin endpoints de salas/reservas; sin tablas de negocio.  
Convención: auditoría + soft delete en **todas** las tablas (RF-5).

## Tareas

- [x] **DB-1** — Crear el proyecto Java (Maven + Spring Boot) (RF-1)  
  **Título (EN):** `[persistence] Create Java Maven Spring Boot project`  
  **Hecho cuando:** existe el proyecto Java: `pom.xml` con `groupId=com.meetingrooms`, `artifactId=meeting-rooms-api`, Java 17+; `src/main/java` y `src/main/resources`; clase `@SpringBootApplication` que compila; sin secretos; `.gitignore` cubre `target/` e IDE.  
  **Hecho:** 2026-11-25 — scaffold en rama; compile OK.

- [x] **DB-2** — Add JPA H2 Flyway dependencies (RF-3)  
  **Título (EN):** `[persistence] Add JPA H2 Flyway dependencies`  
  **Hecho cuando:** `pom.xml` incluye Spring Data JPA, H2 y Flyway; dependencias resuelven / build OK.  
  **Hecho:** 2026-11-25 — dependencias en `pom.xml`.

- [x] **DB-3** — Configure local and prod H2 profiles (RF-2)  
  **Título (EN):** `[persistence] Configure local prod H2 mem profiles`  
  **Hecho cuando:** `application.yml` (default `prod`), `application-local.yml` y `application-prod.yml`; ambos H2 mem con el **mismo** nombre de BD; `ddl-auto` seguro (`validate`/`none`); sin secretos reales.  
  **Hecho:** 2026-11-25 — H2 mem `meetingrooms`; default `prod`.

- [x] **DB-4** — Add Flyway placeholder migration (RF-3)  
  **Título (EN):** `[persistence] Add Flyway placeholder migration`  
  **Hecho cuando:** existe `V1__*.sql` con SQL mínimo no de negocio; sin tablas `rooms`/`reservations`.  
  **Hecho:** 2026-11-25 — `V1__schema_meta.sql` (pendiente ampliar con auditoría en DB-7).

- [x] **DB-5** — Create CA CQRS package skeleton (RF-4)  
  **Título (EN):** `[persistence] Create CA CQRS package skeleton`  
  **Hecho cuando:** existen paquetes `domain.model`, `application.command`, `application.query`, `infrastructure.entrypoints`, `infrastructure.adapters`, `infrastructure.config`; sin controllers de salas/reservas.  
  **Hecho:** 2026-11-25 — `package-info.java` en los 6 paquetes.

- [x] **DB-6** — Verify app starts with Flyway (RF-1, RF-2, RF-3)  
  **Título (EN):** `[persistence] Verify app starts with Flyway`  
  **Hecho cuando:** arranque default `prod` OK; Flyway aplica V1; opcional `local` OK.  
  **Hecho:** 2026-11-25 — verificado `prod` y `local` (antes del change de auditoría; re-verificar en DB-8).

- [x] **DB-7** — Add audit and soft-delete columns to schema_meta (RF-5)  
  **Título (EN):** `[persistence] Add audit soft-delete columns to schema_meta`  
  **Hecho cuando:** `V1__schema_meta.sql` (o migración equivalente en H2 mem) incluye en `schema_meta`: `created_by` VARCHAR(100) NOT NULL, `created_at`, `updated_by` nullable, `updated_at` nullable, `is_deleted` default false, `deleted_by` nullable, `deleted_at` nullable; el INSERT seed rellena `created_by` (p. ej. `system`), `created_at` e `is_deleted=false`; sin tablas rooms/reservations; la convención queda documentada en plan/RF para tablas futuras.  
  **Hecho:** 2026-11-25 — V1 con columnas de auditoría; seed `created_by=system`, `is_deleted=false`.

- [x] **DB-8** — Re-verify app starts after audit migration (RF-3, RF-5)  
  **Título (EN):** `[persistence] Re-verify app starts after audit migration`  
  **Hecho cuando:** `mvn -DskipTests spring-boot:run` con perfil default `prod` arranca sin error; Flyway aplica V1 con columnas de auditoría; evidencia en logs (Started + migration v1); opcionalmente `local` también OK.  
  **Hecho:** 2026-11-25 — `prod` y `local`: Flyway V1 + Started MeetingRoomsApiApplication OK.

## Notas
- Prefijos: `DB` = persistencia/scaffold.
- Issue **#2** ya open — no crear otra.
- Una sola rama `feature/001-persistence-migration`; commits por tarea; sin merge/push hasta `/ssd-cmd-close`.
