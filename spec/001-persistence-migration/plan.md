# Plan — 001 persistence-migration

## Metadatos
- Carpeta: `spec/001-persistence-migration/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-25
- Rama de trabajo prevista: `feature/001-persistence-migration`
- Build: Maven — `com.meetingrooms` / `meeting-rooms-api`

## Enfoque y orden

Scaffold **DB + app base** (sin API de negocio). Orden:

1. Crear proyecto Maven Spring Boot (Java 17+) con coordenadas acordadas (**RF-1**).
2. Dependencias: `spring-boot-starter-data-jpa`, H2, Flyway (**RF-3**).
3. Perfiles `local` / `prod` + default `prod`; H2 mem mismo nombre de BD (**RF-2**).
4. Migración Flyway placeholder (SQL mínimo no de negocio) (**RF-3**).
5. Paquetes base CA + CQRS delgado + clase principal en bootstrap (**RF-4**, **RF-1**).
6. Verificar arranque (perfil default `prod`; opcionalmente `local`).

Dependencias: RF-1 antes que RF-2/RF-3; RF-4 puede ir en paralelo al cableado de config.

## Clean Architecture (CA) + Command Query Responsibility Segregation (CQRS) delgado

Paquetes bajo `com.meetingrooms` (o `com.meetingrooms.api` si el plan de paquetes lo agrupa; raíz = groupId):

| Paquete | Rol en este RF |
|---------|----------------|
| `…domain.model` | Vacío / package-info; sin entidades de negocio aún |
| `…application.command` | Vacío; listo para mutaciones |
| `…application.query` | Vacío; listo para lecturas |
| `…infrastructure.entrypoints` | Vacío; sin controllers de salas/reservas |
| `…infrastructure.adapters` | Vacío; JPA adapters vendrán con tablas de negocio |
| `…infrastructure.config` | Si hace falta beans mínimos; perfiles viven en YAML |

- Sin event bus, sin read DB separada, sin microservicios.
- Command vs query: solo estructura; sin handlers todavía.
- Regla de dependencia: entrypoints/adapters → application → model (no invertir).

## Archivos a crear / modificar

| Archivo / ruta | Responsabilidad | RF |
|----------------|-----------------|-----|
| `pom.xml` | Maven, Java 17+, starters JPA/H2/Flyway, Spring Boot parent | RF-1, RF-3 |
| `src/main/java/…/MeetingRoomsApiApplication.java` (nombre exacto libre si coherente) | Bootstrap Spring Boot | RF-1 |
| `src/main/java/…/domain/model/` | Paquete base | RF-4 |
| `src/main/java/…/application/command/` | Paquete base | RF-4 |
| `src/main/java/…/application/query/` | Paquete base | RF-4 |
| `src/main/java/…/infrastructure/entrypoints/` | Paquete base | RF-4 |
| `src/main/java/…/infrastructure/adapters/` | Paquete base | RF-4 |
| `src/main/java/…/infrastructure/config/` | Paquete base | RF-4 |
| `src/main/resources/application.yml` | Default `spring.profiles.active=prod`; shared no secreto | RF-2 |
| `src/main/resources/application-local.yml` | Datasource H2 mem (mismo nombre) | RF-2 |
| `src/main/resources/application-prod.yml` | Datasource H2 mem (mismo nombre) | RF-2 |
| `src/main/resources/db/migration/V1__*.sql` | Placeholder SQL no de negocio (p. ej. `schema_meta`) | RF-3 |
| `.gitignore` (raíz, si faltan entradas Java/Maven/IDE) | Evitar `target/`, `.idea`, etc. | RF-1 |

No crear: controllers REST de negocio, entidades Room/Reservation, security, Docker PostgreSQL, README de entrega.

## Algoritmo / procedimiento (pseudocódigo)

```text
CREAR pom.xml (groupId=com.meetingrooms, artifactId=meeting-rooms-api, Java 17+)
AÑADIR dependencias: data-jpa, h2, flyway-core (vía starter)
CREAR Application main
CREAR paquetes vacíos domain/application/infrastructure (RF-4)
ESCRIBIR application.yml:
  spring.profiles.active=prod
  jpa.hibernate.ddl-auto=validate|none  # NO create/update de negocio
ESCRIBIR application-local.yml y application-prod.yml:
  spring.datasource.url=jdbc:h2:mem:<MISMO_NOMBRE>;DB_CLOSE_DELAY=-1;...
  username/password demo no secretos reales
ESCRIBIR V1__schema_meta.sql (o nombre equivalente):
  CREATE TABLE schema_meta (...);  -- NO rooms/reservations
ARRARCAR: mvn spring-boot:run  (perfil default prod)
ASSERT: contexto OK + Flyway applied V1
(OPCIONAL) ARRARCAR con -Dspring-boot.run.profiles=local → mismo resultado Flyway
```

## Mensajes de error (`ErrorMessages`)

No aplica cuerpos HTTP de negocio en este RF.  
Opcional diferido: crear clase vacía/stub `ErrorMessages` solo si no alarga el scaffold; **no obligatorio** aquí (RF lo difiere a APIs).

## Perfiles `local` / `prod`

| Perfil | Archivo | Datasource | Notas |
|--------|---------|------------|--------|
| `prod` (default) | `application-prod.yml` | H2 `jdbc:h2:mem:<mismo>` | Arranque sin flag extra |
| `local` | `application-local.yml` | H2 `jdbc:h2:mem:<mismo>` | Activar con `spring.profiles.active=local` |

Nombre de BD mem: un literal acordado en implementación (p. ej. `meetingrooms`); **idéntico** en ambos YAML.  
Sin secretos de prod real; credenciales H2 de demo OK.

## Estrategia de pruebas

Este RF **no** es el de ≥3 tests del enunciado.  
Verificación mínima: arranque + Flyway aplica V1 (manual o un smoke test solo si no desvía tiempo).  
Los ≥3 tests / ≥1 integración quedan para el RF de tests.

## Fuera de alcance

- Tablas `rooms` / `reservations`
- Endpoints REST, auth, filtros, 409/400 de negocio
- PostgreSQL / Docker / Liquibase / Gradle
- README de entrega
- Event bus / CQRS pesado
- `ddl-auto=create|update` como sustituto de Flyway para esquema de negocio

## Trazabilidad RF → plan

| Id | Cubierto por |
|----|----------------|
| RF-1 | `pom.xml` + Application + arranque |
| RF-2 | `application*.yml` H2 mem mismo nombre; default `prod` |
| RF-3 | JPA + Flyway + `V1__…` placeholder |
| RF-4 | Paquetes domain / application / infrastructure |

## Próximo paso

`/ssd-cmd-task` → `task.md` + **GitHub Issue open** del RF.
