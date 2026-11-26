# Plan — 006 delivery-readme

## Metadatos
- Carpeta: `spec/006-delivery-readme/`
- RF origen: `rf.md` (`Estado: aprobada`)
- Fecha: 2026-11-26
- Rama: `feature/006-delivery-readme` (desde `dev`)
- Issue: #7 (open)
- Dependencia: RF 005 cerrado (suite mínima en `dev`)

## Enfoque y orden

1. **Esqueleto README** — secciones vacías/títulos: Ejecución, Endpoints, Flujo curl, Decisiones, Más tiempo, Uso de IA (placeholder), Tiempo (placeholder).
2. **RF-1 — Ejecutable** — requisitos, perfiles, arranque, placeholders/env, `http://localhost:8080`, tabla de endpoints, flujo mínimo curl, `mvn test`.
3. **RF-2 — Decisiones + más tiempo** — redactar desde lo implementado en `dev` (≥3 ítems “más tiempo”).
4. **RF-2 — Uso de IA** — **bloquear** hasta recibir bullets del humano; luego solo formatear (no inventar).
5. **RF-3 — Tiempo** — **última** tarea: rellenar t0 (`PRUEBA-INICIO.md`) + duración al cierre de este RF.

Sin tocar código de producto. Un solo artefacto de entrega: `README.md` raíz.

## Clean Architecture (CA) + CQRS delgado

Este RF es **Docs**: no crea capas nuevas. El README **describe** la estructura existente (entrypoints REST → application commands/queries → domain → adapters JPA) sin event bus. No se documentan paquetes inventados.

## Archivos a crear / modificar

| Archivo | Responsabilidad | RF |
|---------|-----------------|-----|
| `README.md` (raíz) | Documento de entrega completo | RF-1, RF-2, RF-3 |
| `PRUEBA-INICIO.md` | Solo **lectura** para t0; no modificar | RF-3 |
| Controllers / `SecurityConfig` / YAML | Solo **lectura** para tabla y perfiles | RF-1 |

## Contenido README (estructura)

```text
# Meeting Rooms API
## Cómo ejecutarlo
  - Requisitos (Java ≥17, Maven)
  - Perfiles local / prod (default prod en application.yml)
  - Arranque (mvn spring-boot:run / -Dspring-boot.run.profiles=…)
  - Placeholders / secretos demo (sin secretos reales de prod)
  - URL base http://localhost:8080
## Endpoints (tabla)
  | Método | Ruta | Auth |
## Flujo mínimo (curl)
  login → POST room (Bearer) → GET rooms
## Tests
  mvn test
## Decisiones
## Qué haría con más tiempo (≥3)
## Cómo usé la IA (bullets del humano, formateados)
## Tiempo (última task)
## Repo
  https://github.com/engelsrc/meeting-rooms-api-pt
```

## Tabla de endpoints (fuente: código actual)

| Método | Ruta | Auth |
|--------|------|------|
| POST | `/api/auth/login` | no |
| POST | `/api/rooms` | sí (JWT) |
| GET | `/api/rooms` | no |
| POST | `/api/reservations` | sí (JWT) |
| GET | `/api/reservations` | no |
| POST | `/api/reservations/cancel` | sí (JWT) |

(GET `/api/**` y login públicos según `SecurityConfig`; resto de `/api/**` autenticado.)

## Algoritmos / reglas a documentar (no reimplementar)

```text
ARRANQUE:
  default profile = prod → H2 mem + JWT demo en application-prod.yml
  local → application-local.yml (también H2 demo en estado actual)
  documentar que secretos en YAML son demo; no pegar secretos reales

FLUJO CURL:
  POST /api/auth/login {admin, admin} → accessToken
  POST /api/rooms Authorization: Bearer … → 201
  GET /api/rooms → 200

SOLAPE (en decisiones):
  reserva ACTIVE, intervalo [start, end) → conflicto 409

IA:
  ESPERAR bullets humano
  FORMATEAR en sección (qué pedí / acepté / corregí)
  NO inventar hechos

TIEMPO (última task):
  LEER t0 de PRUEBA-INICIO.md
  CALCULAR / anotar duración hasta cierre RF 006
  ESCRIBIR sección Tiempo en README
```

## Mensajes de error

El README puede **mencionar** que la API responde mensajes en inglés vía `ErrorMessages`. No se añaden constantes nuevas en este RF.

## Perfiles `local` / `prod`

- Documentar `spring.profiles.active: prod` por defecto.
- Documentar diferencia `local` vs `prod` según YAML actuales (H2, JWT demo, consola H2 en local).
- Placeholders: indicar nombres de propiedades (`spring.datasource.*`, `app.jwt.secret`) y que no deben subirse secretos reales; valores actuales = demo de la prueba.

## Estrategia de pruebas

Este RF **no** añade tests. La suite de RF 005 (`mvn test`) se documenta en el README (RF-1).

## Fuera de alcance

OpenAPI/Swagger, Docker Compose (salvo “más tiempo”), cambios de código/API, más tests, README en inglés, CI, inventar bullets IA, renombrar carpeta SDD.

## Trazabilidad

| Id | Cubierto por |
|----|----------------|
| RF-1 | Secciones ejecución, endpoints, curl, tests en `README.md` |
| RF-2 | Decisiones, más tiempo, IA (tras bullets) |
| RF-3 | Sección Tiempo en última task |

## Dependencia humana

Antes de cerrar la task de IA: el humano debe entregar bullets. Sin ellos, esa task no se marca hecha.

## Próximo paso

`/ssd-cmd-task` → `task.md` + GitHub Issue **open**.
