# Tareas — 001 persistence-migration

GitHub Issue: #2 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/2 (open)  
Rama de trabajo: `feature/001-persistence-migration`  
Sin endpoints de salas/reservas; sin tablas de negocio.

## Tareas

- [x] **DB-1** — Crear el proyecto Java (Maven + Spring Boot) (RF-1)  
  **Título (EN):** `[persistence] Create Java Maven Spring Boot project`  
  **Hecho cuando:** existe el proyecto Java en la raíz del repo: `pom.xml` con `groupId=com.meetingrooms`, `artifactId=meeting-rooms-api`, Java 17+; estructura `src/main/java` y `src/main/resources`; clase `@SpringBootApplication` que compila; no hay secretos; `.gitignore` cubre `target/` e IDE si faltaba. (Dependencias JPA/H2/Flyway = DB-2.)  
  **Hecho:** 2026-11-25 — `pom.xml` + `MeetingRoomsApiApplication`; `mvn -DskipTests compile` OK; Issue #2 open.

- [ ] **DB-2** — Add JPA H2 Flyway dependencies (RF-3)  
  **Título (EN):** `[persistence] Add JPA H2 Flyway dependencies`  
  **Hecho cuando:** `pom.xml` incluye Spring Data JPA, H2 y Flyway (starters Spring Boot); el proyecto resuelve dependencias (`mvn dependency:resolve` o build OK).

- [ ] **DB-3** — Configure local and prod H2 profiles (RF-2)  
  **Título (EN):** `[persistence] Configure local prod H2 mem profiles`  
  **Hecho cuando:** existen `application.yml` (default `spring.profiles.active=prod`), `application-local.yml` y `application-prod.yml`; ambos perfiles usan `jdbc:h2:mem:…` con el **mismo** nombre de BD; sin credenciales secretas reales; `ddl-auto` no crea esquema de negocio (`validate`/`none` o equivalente seguro).

- [ ] **DB-4** — Add Flyway placeholder migration (RF-3)  
  **Título (EN):** `[persistence] Add Flyway placeholder migration`  
  **Hecho cuando:** existe `src/main/resources/db/migration/V1__*.sql` con SQL mínimo **no** de negocio (p. ej. `schema_meta`); no existen tablas `rooms`/`reservations` en migraciones.

- [ ] **DB-5** — Create CA CQRS package skeleton (RF-4)  
  **Título (EN):** `[persistence] Create CA CQRS package skeleton`  
  **Hecho cuando:** existen paquetes `domain.model`, `application.command`, `application.query`, `infrastructure.entrypoints`, `infrastructure.adapters`, `infrastructure.config` bajo el base package; no hay controllers REST de salas/reservas.

- [ ] **DB-6** — Verify app starts with Flyway (RF-1, RF-2, RF-3)  
  **Título (EN):** `[persistence] Verify app starts with Flyway`  
  **Hecho cuando:** la app arranca con perfil default `prod` sin error de contexto; Flyway aplica V1; opcionalmente se verifica arranque con `local`; evidencia en consola/logs o comando documentado en la casilla al marcar.

## Notas
- Prefijos: `DB` = persistencia/scaffold.
- Una sola rama `feature/001-persistence-migration`; commits por tarea; Issue **open** hasta `/ssd-cmd-close`.
- Reloj: ya marcado en `PRUEBA-INICIO.md`.
