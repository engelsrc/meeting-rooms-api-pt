# RF — Persistencia H2 + Flyway (perfiles local/prod)

Estado: aprobada

## Metadatos
- Carpeta: `spec/001-persistence-migration/`
- Slug: `persistence-migration`
- Orden: `001`
- Tipo: `DB` | `Model` (scaffold mínimo de app)
- Origen: change — auditoría + soft delete en todas las tablas
- Fecha: 2026-11-25
- GitHub Issue: #2 — https://github.com/engelsrc/meeting-rooms-api-pt/issues/2

## Problema / oportunidad

Sin aplicación Spring Boot ni persistencia cableada, no se pueden construir salas/reservas ni cumplir el enunciado (BD + migraciones + perfiles). Además, las tablas necesitan trazabilidad de alta/cambio/baja lógica.

## Objetivo

Dejar un **scaffold Spring Boot (Maven)** ejecutable con perfiles **`local`** y **`prod`**, ambos con **H2 en memoria** (mismo nombre de BD), **Spring Data JPA** + **Flyway** con migración placeholder (SQL mínimo no de negocio) que incluya **campos de auditoría y soft delete** en **todas** las tablas, y paquetes base Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado. Perfil por defecto: **`prod`**. PostgreSQL queda como **extra posterior** si sobra tiempo.

## Alcance

### Incluye
- Proyecto **Maven** Spring Boot (Java 17+): `groupId` **`com.meetingrooms`**, `artifactId` **`meeting-rooms-api`**.
- Dependencias: **Spring Data JPA**, H2, Flyway.
- `application.yml` (o base) con **`spring.profiles.active=prod`** por defecto; más `application-local.yml` y `application-prod.yml` (sin secretos reales).
- Ambos perfiles: datasource H2 **in-memory** con **el mismo** nombre de BD.
- ≥1 migración Flyway con SQL mínimo **no** de negocio (p. ej. `schema_meta`) **más columnas de auditoría** en esa tabla.
- **Convención de auditoría** obligatoria en **todas** las tablas (actuales y futuras de negocio):
  - `created_by` `VARCHAR(100)`
  - `created_at` (timestamp)
  - `updated_by` `VARCHAR(100)` (nullable hasta primer update)
  - `updated_at` (timestamp, nullable hasta primer update)
  - `is_deleted` (boolean / bit; default `false`)
  - `deleted_by` `VARCHAR(100)` (nullable)
  - `deleted_at` (timestamp, nullable)
- Paquetes base (vacíos o solo bootstrap; sin endpoints de negocio):
  - `domain.model`
  - `application.command` / `application.query`
  - `infrastructure.entrypoints`
  - `infrastructure.adapters`
  - `infrastructure.config`

### Excluye
- Tablas de negocio **rooms** / **reservations** (RFs posteriores; heredarán la misma convención de auditoría).
- Endpoints REST de salas/reservas.
- Autenticación JWT/Basic (el valor de `*_by` se cableará cuando exista auth).
- Hard delete como política de negocio (la baja es lógica vía `is_deleted`).
- PostgreSQL, Docker Compose de BD, Liquibase, Gradle.
- README de entrega final (RF docs).
- Tests de integración de API de negocio (RF tests); este RF solo debe poder arrancar y aplicar Flyway.

## Actores
- Humano: aprueba RF / change; reloj ya en `PRUEBA-INICIO.md`.
- Agente: implementa según plan/task tras Issue **open**.

## Requisitos funcionales

### RF-1 — Scaffold Spring Boot (Maven)
**CUANDO** se construya y arranque el proyecto con el perfil activo, **EL SISTEMA** es una app Spring Boot (Java 17+) Maven (`com.meetingrooms` / `meeting-rooms-api`) que inicia sin error de contexto.

#### Criterios de aceptación
- [ ] Existe proyecto Maven Spring Boot con Java 17 o superior.
- [ ] `groupId` = `com.meetingrooms`; `artifactId` = `meeting-rooms-api`.
- [ ] La aplicación arranca; el perfil por defecto es **`prod`** y se puede activar **`local`**.
- [ ] No hay secretos de producción en el repo.

### RF-2 — Perfiles local y prod con H2 en memoria (mismo nombre)
**MIENTRAS** se use el perfil `local` o `prod`, **EL SISTEMA** usa H2 in-memory con el **mismo** nombre de base en ambos perfiles.

#### Criterios de aceptación
- [ ] Existen `application-local.yml` y `application-prod.yml` (más base compartida).
- [ ] Ambos perfiles apuntan a H2 en memoria (`jdbc:h2:mem:…`) con el mismo nombre de BD.
- [ ] Por defecto arranca con perfil **`prod`** si no se fuerza otro.
- [ ] La configuración no hardcodea credenciales secretas de un entorno real.

### RF-3 — Flyway + JPA con migración placeholder
**CUANDO** arranca la aplicación, **EL SISTEMA** tiene Spring Data JPA y Flyway; Flyway aplica ≥1 migración con SQL mínimo **no** de negocio (sin rooms/reservations).

#### Criterios de aceptación
- [ ] Dependencias Spring Data JPA + Flyway presentes y habilitadas.
- [ ] Existe ≥1 script Flyway versionado con SQL mínimo no de negocio (p. ej. tabla técnica `schema_meta` o equivalente).
- [ ] El arranque aplica la migración sin fallo.
- [ ] No se crean tablas de negocio del enunciado (rooms/reservations).
- [ ] No se usa `ddl-auto` para crear esquema de negocio en lugar de Flyway.

### RF-4 — Paquetes base CA + CQRS delgado
**CUANDO** se revise el árbol de paquetes, **EL SISTEMA** expone la estructura acordada (sin endpoints de negocio ni event bus).

#### Criterios de aceptación
- [ ] Existen paquetes: `domain.model`, `application.command`, `application.query`, `infrastructure.entrypoints`, `infrastructure.adapters`, `infrastructure.config`.
- [ ] No hay controllers REST de salas/reservas en este RF.
- [ ] Sin event bus / infra CQRS “enterprise”.

### RF-5 — Auditoría y soft delete en todas las tablas
**MIENTRAS** exista una tabla de aplicación creada por migraciones Flyway, **EL SISTEMA** incluye en cada una las columnas de auditoría y baja lógica acordadas.

#### Criterios de aceptación
- [ ] Toda tabla creada en este RF (p. ej. `schema_meta`) tiene: `created_by` VARCHAR(100), `created_at`, `updated_by` VARCHAR(100) nullable, `updated_at` nullable, `is_deleted` (default false), `deleted_by` VARCHAR(100) nullable, `deleted_at` nullable.
- [ ] La convención queda explícita en el RF/plan para tablas futuras de negocio (rooms/reservations).
- [ ] Soft delete: no se exige borrado físico como política; la baja se marca con `is_deleted` (+ `deleted_by` / `deleted_at` cuando aplique).
- [ ] Tras el cambio de esquema, la app arranca y Flyway aplica sin fallo (nueva migración o V1 ajustado según plan; en H2 mem no hay historial persistente entre reinicios).

## Reglas de negocio
- Build: **Maven** (no Gradle).
- Coordenadas: `com.meetingrooms` / `meeting-rooms-api`.
- H2 en **ambos** perfiles; modo **memoria**; **mismo** nombre de BD.
- Perfil por defecto: **`prod`**.
- Persistencia: **Spring Data JPA** + **Flyway**.
- Migración: SQL mínimo no de negocio + **auditoría en todas las tablas**.
- `*_by`: `VARCHAR(100)`; `is_deleted` default `false`.
- Paquetes: estructura A del agente.
- Extra futuro (fuera de este RF): PostgreSQL en `prod` + Docker si sobra tiempo.

## Casos límite
- Si el arranque falla por Flyway/JPA: no marcar RF-3/RF-5 como hechos.
- Si `ddl-auto` crea tablas de negocio: fuera de alcance; no aceptar en review.
- Mismo nombre H2 mem en `local`/`prod`: aceptado; no exigir URLs distintas.
- Filas seed del placeholder: deben rellenar al menos `created_by` / `created_at` / `is_deleted=false` (p. ej. `system`).
- `updated_*` / `deleted_*` pueden ser NULL hasta el primer update/baja.
- Cambio a PostgreSQL: solo con RF/change nuevo o extra explícito.

## Restricciones heredadas
- Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado; sin event bus / infra de más en la ventana de 3 h.
- Mensajes de API en inglés vía `ErrorMessages` (cuando existan APIs; no aplica cuerpos HTTP de negocio aquí).
- Perfiles Spring `local` / `prod`.
- Chat/SDD en español; código en inglés.
- Una GitHub Issue **open** antes de implementar; se cierra en `/ssd-cmd-close`.

## Preguntas abiertas
- Ninguna tras AskQuestions (soft delete `is_deleted`; columnas A; alcance todas las tablas; `*_by` VARCHAR(100)).

## Siguiente paso
`Próxima acción: RECREAR EL PLAN, ejecutar /ssd-cmd-plan spec/001-persistence-migration/rf.md`
